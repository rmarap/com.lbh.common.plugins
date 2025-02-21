package com.lbcap.common.plugins

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class LbcapClientPluginTest extends Specification {
    def project = ProjectBuilder.builder().build()

    def setup() {
        project.extensions.extraProperties.set("DISABLE_SHADOW_CONFIG", "true")
        def versionFolder = new File(project.rootDir.absolutePath, "/.version")
        if (!versionFolder.exists()) {
            versionFolder.mkdirs()
        }
        def versionFile = new File(project.rootDir.absolutePath, "/.version/version.json")
        if (!versionFile.exists()) {
            versionFile << """
                {
                        "majorVersion": 0,
                        "minorVersion": 0,
                        "buildNumber": 1
                }
            """
        }
    }

    def "service plugin registers test tasks"() {
        when:
        project.plugins.apply("com.lbcap.plugins.lbcap-library-plugin")

        then:
        project.tasks.findByName("incrementAndSaveVersionNumber") != null
        project.tasks.findByName("integrationTests") != null
        project.tasks.findByName("regressionTests") != null
        project.tasks.findByName("unitTests") != null
        project.tasks.findByName("allTests") != null
    }

    def "plugin registers java-library and publish plugin"() {
        when:
        project.plugins.apply("com.lbcap.plugins.lbcap-library-plugin")

        then:
        project.plugins.findPlugin("java-library") != null
        project.plugins.findPlugin("maven-publish") != null
    }

    def "plugin sets default project group"() {
        when:
        project.plugins.apply("com.lbcap.plugins.lbcap-library-plugin")

        then:
        project.group == "com.lbcap"
        project.tasks.findByName("jar").manifest.attributes.get("Implementation-Title") == "com.lbcap.test"
    }

    def "plugin sets default project version"() {
        when:
        project.plugins.apply("com.lbcap.plugins.lbcap-library-plugin")

        then:
        project.version != null
        project.version != ""
        project.version != "unspecified"
        project.tasks.findByName("jar").manifest.attributes.get("Implementation-Version") != ""
        project.tasks.findByName("jar").manifest.attributes.get("Implementation-Version") != "unspecified"
    }

    def "plugin registers jar task with default attributes"() {
        when:
        project.plugins.apply("com.lbcap.plugins.lbcap-library-plugin")

        then:
        project.tasks.findByName("jar") != null
        project.tasks.findByName("jar").manifest.attributes.get("Manifest-Version") == "1.0"
        project.tasks.findByName("jar").manifest.attributes.get("Implementation-Version") != "unspecified"
        project.tasks.findByName("jar").manifest.attributes.get("Implementation-Title") == "com.lbcap.test"
        project.tasks.findByName("jar").manifest.attributes.get("Implementation-Vendor") == "Lbcap, Inc."
    }

    def "user can override group and version of project"() {
        when:
        project.version = "1234"
        project.group = "yeahoo"
        project.plugins.apply("com.lbcap.plugins.lbcap-library-plugin")

        then:
        project.version == "1234"
        project.group == "yeahoo"
        project.tasks.findByName("jar").manifest.attributes.get("Implementation-Title") == "yeahoo.test"
        project.tasks.findByName("jar").manifest.attributes.get("Implementation-Version") == "1234"
    }

    def "default repositories are added"() {
        when:
        project.plugins.apply("com.lbcap.plugins.lbcap-library-plugin")

        then:
        project.repositories.findByName("MavenRepo") != null
        project.repositories.findByName("lbcap-common") != null
    }

    def "configured repositories are added"() {
        when:
        project.extensions.extraProperties["REPOS"] = " mavenCentral , lbcap-common,lbcap-common-auth ,  lbcap-common-data "
        project.plugins.apply("com.lbcap.plugins.lbcap-library-plugin")

        then:
        project.repositories.findByName("MavenRepo") != null
        project.repositories.findByName("lbcap-common") != null
//        project.repositories.findByName("lbcap-common-auth") != null
//        project.repositories.findByName("lbcap-common-data") != null
//        project.repositories.findByName("lbcap-common-dropwizard") == null
//        project.repositories.findByName("lbcap-common-consume") == null
//        project.repositories.findByName("lbcap-gradle-tasks") == null
//        project.repositories.findByName("lbcap-common-activity") == null
    }
}
