package com.lbcap.common.plugins

import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class LbcapServicePluginTest extends Specification {
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

    def "service plugin throws exception when required plugin is not applied"() {
        when:
        project.plugins.apply("com.lbcap.plugins.lbcap-service-plugin")

        then:
        def e = thrown(PluginApplicationException)
        e.cause.message.contains("Add plugin: id 'com.github.johnrengelman.shadow' version '7.0.0'")
    }
}
