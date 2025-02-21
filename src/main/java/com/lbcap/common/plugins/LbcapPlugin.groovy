package com.lbcap.common.plugins

import com.lbcap.common.gradletasks.buildversion.BuildVersionInfo
import com.lbcap.common.gradletasks.buildversion.IncrementAndSaveVersionNumber
import com.lbcap.common.gradletasks.test.*
import org.apache.commons.lang3.StringUtils
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.Copy
import org.gradle.internal.logging.text.StyledTextOutputFactory

import static org.gradle.internal.logging.text.StyledTextOutput.Style

abstract class LbcapPlugin {
    // Universally applicable configuration for all lbcap gradle projects
    void applyDefault(Project project) {
        if (project.group == null || project.group == "") {
            project.group = 'com.lbcap'
        }

        if (project.version == null || project.version == "" || project.version == "unspecified") {
            BuildVersionInfo.setBuildVersionFromFile(project.extensions.extraProperties, project.rootDir.absolutePath)
            project.version = project.extensions.extraProperties.buildVersionInfo.versionNumber
        }

        project.configurations.all {
            resolutionStrategy.cacheDynamicVersionsFor 0, 'seconds'
        }

        configureLbcapGithubRepos(project)
    }

    private void configureLbcapGithubRepos(Project project) {
        // Adding lbcap-common automatically enables gradle to find other lbcap github packages like lbcap-common-consume, lbcap-common-dropwizard etc. via maven-metadata.xml
        if (!project.hasProperty("REPOS")) {
            project.extensions.extraProperties.set("REPOS", "mavenCentral,lbcap-common")
        }

        for (String repo : project.properties.get("REPOS").trim().split("\\s*,\\s*").flatten().findAll { StringUtils.isNotBlank(it) }) {
            switch (repo) {
                case "mavenCentral":
                    project.repositories.mavenCentral()
                    break
                case "mavenLocal":
                    project.repositories.mavenLocal()
                    break
                default:
                    project.repositories.maven {
                        name = repo
                        url = "https://maven.pkg.github.com/lbcap/" + repo
                        credentials {
                            username = project.properties.get("GITHUB_PKGS_USERID")
                            password = project.properties.get("GITHUB_PKGS_ACCESS_TOKEN")
                        }
                    }
                    break
            }
        }
    }

    void applyJarConfiguration(Project project) {
        project.java {
            sourceCompatibility JavaVersion.VERSION_11
            targetCompatibility JavaVersion.VERSION_11
        }

        project.tasks.getByName("jar").configure {
            manifest.attributes(
                    'Implementation-Version': project.getVersion(),
                    'Implementation-Title': "$project.group.$project.name",
                    'Implementation-Vendor': "Lbcap, Inc."
            )
        }

        if (project.hasProperty("ENABLE_SHADOW_CONFIG")) {
            if (project.pluginManager.findPlugin('com.github.johnrengelman.shadow') == null) {
                throw new GradleException("Add plugin: id 'com.github.johnrengelman.shadow' version '7.0.0'")
            }

            //For library JARs, use version in JAR name as it is published to Gitlab.
            //For SVC Jars, use version independent name as its packaged to docker
            def customArchiveVersion = project.pluginManager.findPlugin('java-library') == null ? "" : project.version

            project.tasks.getByName('shadowJar').configure {
                archiveBaseName.set(project.name)
                archiveClassifier.set('')
                archiveVersion.set(customArchiveVersion)
                mergeServiceFiles()
                zip64(true)
            }

            //Shadow jar automatically runs for `application` plugin but not for java-library
            if (project.pluginManager.findPlugin('java-library') != null) {
                project.tasks.build.dependsOn project.tasks.shadowJar
            }
        } else {
            println "Gradle lbcap plugin is not configuring shadow."
        }

        //This task needs 'build' stage which is available after java plugin in applied
        configureHelperInfo(project)
    }

    //Applicable for all projects that use java gradle plugin
    private static void configureHelperInfo(Project project) {
        project.tasks.findByPath('build').doLast {
            def resolvedPlugin = project.buildscript.configurations.findByName('classpath').resolvedConfiguration.firstLevelModuleDependencies.stream().filter(ht -> ht.name.contains('tasks')).collect { lt -> lt.name }
            def out = services.get(StyledTextOutputFactory).create("colors")
            out.withStyle(Style.Success).println "Lbcap Gradle dependency applied: " + resolvedPlugin
            out.withStyle(Style.Info).println "Use './gradlew " + Utils.HELP_TASK_NAME + "' for more information about plugin."
            out.withStyle(Style.Info).println("To find specific dependency: './gradlew :svc:dependencies --configuration runtimeClasspath | grep common-data'")
        }
    }

    //Applicable for all projects that apply java plugin
    void applyTestConfiguration(Project project) {
        if (!project.properties.hasProperty('DISABLE_TEST_CONFIG')) {
            project.tasks.getByName('test').configure {
                String customTags = System.getProperty("onlyTags") ? System.getProperty("onlyTags") : 'UnitTests'
                useJUnitPlatform {
                    includeTags(customTags)
                }
                testLogging {
                    events = ["passed", "skipped", "failed", "standardOut", "standardError"]
                }
                afterSuite { desc, result ->
                    if (!desc.parent) { // will match the outermost suite
                        def output = "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped)"
                        def startItem = '|  ', endItem = '  |'
                        def repeatLength = startItem.length() + output.length() + endItem.length()
                        println('\n' + ('-' * repeatLength) + '\n' + startItem + output + endItem + '\n' + ('-' * repeatLength))
                    }
                }
            }
        } else {
            println "Gradle lbcap plugin is not configuring test config."
        }

        project.tasks.create("incrementAndSaveVersionNumber", IncrementAndSaveVersionNumber.class)
        project.tasks.create("integrationTests", IntegrationTests.class)
        project.tasks.create("preDeployIT", PreDeployIntegrationTests.class)
        project.tasks.create("postDeployIT", PostDeployIntegrationTests.class)
        project.tasks.create("regressionTests", RegressionTests.class)
        project.tasks.create("unitTests", UnitTests.class)
        project.tasks.create("allTests", AllTests.class)
    }

    void configureLibraryProject(Project project) {
        project.plugins.apply('java-library')
        project.plugins.apply('maven-publish')
        applyTestConfiguration(project)
        applyJarConfiguration(project)
        applyCommonDependencies(project)

        project.java {
            withJavadocJar()
            withSourcesJar()
        }

        project.tasks.findByPath('publish').finalizedBy 'incrementAndSaveVersionNumber'
        project.publishing {
            repositories {
                maven {
                    name = "GitHubPackages"
                    url = project.properties.get('GITHUB_PKGS_URL')

                    credentials {
                        username = System.getenv("GITHUB_ACTOR")
                        password = System.getenv("GITHUB_TOKEN")
                    }
                }
            }
            publications {
                gpr(MavenPublication) {
                    from project.components.java
                }
            }
        }
    }

    void applyCommonDependencies(Project project) {
        PluginDependencies.getCommonDependencies().each { type, dep ->
            dep.each { dependency -> project.dependencies.add(type, dependency) }
        }
    }

    void configurePluginHelperTask(String pluginId, Project project, Map<String, List<String>> dependencies) {
        project.tasks.register(Utils.HELP_TASK_NAME) { task ->
            def out = services.get(StyledTextOutputFactory).create("colors")
            out.withStyle(Style.SuccessHeader).println(pluginId + " applied to '$project.name'.")
            out.withStyle(Style.Info).println("Dependencies added by plugin:")
            dependencies.keySet().forEach { it ->
                out.withStyle(Style.Info).println(it)
                dependencies.get(it).forEach(d -> println(" - " + d))
            }
        }
    }

    void configurePluginGitHookTask(Project project) {
        project.tasks.register(Utils.GITHOOKS_TASK_NAME, Copy) {
            if (! System.properties['os.name'].toLowerCase().contains('windows')) {
                from("$project.rootDir/.githooks")
                into("$project.rootDir/.git/hooks")
                fileMode(0777)
            }
        }
    }

}
