package com.lbcap.common.plugins

import org.apache.commons.lang3.StringUtils
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin applied to Service project that contains REST service.
 */
class LbcapServicePlugin extends LbcapPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.extraProperties.set('ENABLE_SHADOW_CONFIG', 'True')

        super.applyDefault(project)

        project.plugins.apply('application')
        super.applyTestConfiguration(project)
        super.applyJarConfiguration(project)
        super.applyCommonDependencies(project)
        applyServiceDependencies(project)

        // exclude log4j globally from coming in to service shadow jar
        project.configurations {
            implementation.exclude group: 'org.slf4j', module: 'log4j-over-slf4j'
        }

        if (!project.hasProperty('mainClassName') || StringUtils.isBlank(project.properties.get('mainClassName'))) {
            throw new GradleException("Set 'mainClassName' property.")
        }
        project.application {
            mainClassName(project.properties.get('mainClassName'))
        }
        project.tasks.findByPath('build').finalizedBy 'incrementAndSaveVersionNumber'
        project.tasks.findByPath('distTar').setEnabled(false)
        project.tasks.findByPath('distZip').setEnabled(false)

        configureSwagger(project)

        def commonDep = PluginDependencies.getCommonDependencies()
        def otherDep = PluginDependencies.getServiceDependencies()
        for (Map.Entry<String, List<String>> entry : otherDep.entrySet()) {
            if (commonDep.containsKey(entry.key)) {
                commonDep.get(entry.key).addAll(entry.value)
            } else {
                commonDep.put(entry.key, entry.value)
            }
        }
        super.configurePluginHelperTask("lbcap-service-plugin", project, commonDep)
        super.configurePluginGitHookTask(project)
        project.tasks.getByName("build").dependsOn(project.tasks.getByName("installGitHooks"))
    }

    private void configureSwagger(Project project) {
        if (!project.hasProperty("DISABLE_SWAGGER_CONFIG")) {
            if (project.pluginManager.findPlugin('io.swagger.core.v3.swagger-gradle-plugin') == null) {
                throw new GradleException("Add plugin: id 'io.swagger.core.v3.swagger-gradle-plugin' version '2.1.9'")
            }

            if (!project.hasProperty('SWAGGER_RESOURCES_PACKAGE') || StringUtils.isBlank(project.properties.get('SWAGGER_RESOURCES_PACKAGE'))) {
                throw new GradleException("Set 'SWAGGER_RESOURCES_PACKAGE' property.")
            }
            project.tasks.getByName("resolve").configure {
                outputFileName = project.group + '.' + project.name + '-openapi-' + project.version
                outputFormat = 'YAML'
                prettyPrint = 'TRUE'
                classpath = project.sourceSets.main.runtimeClasspath
                resourcePackages = [project.properties.get('SWAGGER_RESOURCES_PACKAGE')]
                outputDir = project.file('../docs')
                modelConverterClasses = ['com.lbcap.common.openapi.LongToStringPropertyConverter']
            }
            project.tasks.getByName("resolve").doLast {
                ant.replace(file: '../docs/' + project.group + '.' + project.name + '-openapi-' + project.version + '.yaml', token: "@VERSION@", value: project.version, encoding: 'UTF-8')
                ant.delete(file: '../docs/index.html')
                ant.copy(file: '../docs/index.base.html', tofile: '../docs/index.html')
                ant.replace(file: '../docs/index.html', token: "@OPENAPI-FILE@", value: project.group + '.' + project.name + '-openapi-' + project.version + '.yaml', encoding: 'UTF-8')
            }
        } else {
            println "Gradle lbcap service plugin is not configuring swagger."
        }
    }

    private static void applyServiceDependencies(Project project) {
        PluginDependencies.getServiceDependencies().each { type, dep ->
            dep.each { dependency -> project.dependencies.add(type, dependency) }
        }
    }
}
