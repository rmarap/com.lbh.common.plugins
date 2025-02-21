package com.lbcap.plugins


import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

/**
 * A simple functional test for the 'com.lbcap.plugins.greeting' plugin.
 */
class LbcapPluginFunctionalTest extends Specification {

    def setup() {
        def projectDir = new File("build/functionalTest")
        def settingsFile = new File(projectDir, "settings.gradle")
        def buildFile = new File(projectDir, "build.gradle")
        def propertiesFile = new File(projectDir, "gradle.properties")

        if (settingsFile.exists()) {
            settingsFile.delete()
        }
        if (propertiesFile.exists()) {
            propertiesFile.delete()
        }
        if (buildFile.exists()) {
            buildFile.delete()
        }
        if (projectDir.exists()) {
            projectDir.delete()
        }


        def versionFolder = new File(projectDir, "/.version")
        if(!versionFolder.exists()) {
            versionFolder.mkdirs()
        }
        def versionFile = new File(projectDir, "/.version/version.json")
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

//    def "test lbcap service plugin"() {
//        given:
//        def projectDir = new File("build/functionalTest")
//        projectDir.mkdirs()
//        new File(projectDir, "settings.gradle") << ""
//        new File(projectDir, "gradle.properties") << """
//                SWAGGER_RESOURCES_PACKAGE="some-resources"
//                mainClassName="something"
//        """
//        new File(projectDir, "build.gradle") << """
//            plugins {
//                id 'com.github.johnrengelman.shadow' version '7.0.0'
//                id "io.swagger.core.v3.swagger-gradle-plugin" version "2.1.9"
//                id "com.lbcap.plugins.lbcap-service-plugin"
//            }
//        """
//
//        when:
//        def runner = GradleRunner.create()
//        runner.forwardOutput()
//        runner.withPluginClasspath()
//        runner.withProjectDir(projectDir)
//        runner.build()
//
//        then:
//        def exception = specificationContext.getThrownException();
//        if (exception != null) {
//            exception.printStackTrace();
//            System.out.println(exception.message)
//            exception.message
//        }
//        noExceptionThrown()
//    }
//
//    def "test required tasks are created in service plugin"() {
//        given:
//        def projectDir = new File("build/functionalTest")
//        projectDir.mkdirs()
//        new File(projectDir, "settings.gradle") << ""
//        new File(projectDir, "gradle.properties") << """
//                SWAGGER_RESOURCES_PACKAGE="some-resources"
//                mainClassName="something"
//        """
//        new File(projectDir, "build.gradle") << """
//            plugins {
//                id 'com.github.johnrengelman.shadow' version '7.0.0'
//                id "io.swagger.core.v3.swagger-gradle-plugin" version "2.1.9"
//                id "com.lbcap.plugins.lbcap-service-plugin"
//            }
//        """
//
//        when:
//        def runner = GradleRunner.create()
//        runner.forwardOutput()
//        runner.withPluginClasspath()
//        runner.withArguments("tasks")
//        runner.withProjectDir(projectDir)
//        def result = runner.build()
//
//        then:
//        result.output.contains('unitTests - Lbcap task that runs all unit tests.')
//        result.output.contains('regressionTests - Lbcap task that runs all regression tests.')
//        result.output.contains('integrationTests - Lbcap task that runs all integration tests.')
//        result.output.contains('allTests - Lbcap task that runs all lbcap annotated tests.')
//        result.output.contains('shadowJar - Create a combined JAR of project and runtime dependencies')
//        result.output.contains('incrementAndSaveVersionNumber')
//    }

    def "test required tasks are created in library plugin"() {
        given:
        def projectDir = new File("build/functionalTest")
        projectDir.mkdirs()
        new File(projectDir, "settings.gradle") << ""
        new File(projectDir, "gradle.properties") << """
        """
        new File(projectDir, "build.gradle") << """
            plugins {
                id 'com.github.johnrengelman.shadow' version '7.0.0'
                id "com.lbcap.plugins.lbcap-library-plugin"
            }
        """

        when:
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("tasks")
        runner.withProjectDir(projectDir)
        def result = runner.build()

        then:
        result.output.contains('unitTests - Lbcap task that runs all unit tests.')
        result.output.contains('regressionTests - Lbcap task that runs all regression tests.')
        result.output.contains('integrationTests - Lbcap task that runs all integration tests.')
        result.output.contains('allTests - Lbcap task that runs all lbcap annotated tests.')
        result.output.contains('shadowJar - Create a combined JAR of project and runtime dependencies')
        result.output.contains('incrementAndSaveVersionNumber')
    }
}
