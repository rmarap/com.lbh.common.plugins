package com.lbcap.common.plugins

/**
 * One place to declare all dependencies and related versions.
 */
class PluginDependencies {
    public static final String DEP_PRISIDIO_COMMON = 'com.lbcap:lbcap-common:+'
    public static final String DEP_PRISIDIO_COMMON_AUTH = 'com.lbcap:lbcap-common-auth:+'
    public static final String DEP_PRISIDIO_COMMON_DATA = 'com.lbcap:lbcap-common-data:+'
    public static final String DEP_PRISIDIO_COMMON_ACTIVITY = 'com.lbcap:lbcap-common-activity:+'
    public static final String DEP_PRISIDIO_COMMON_CONSUME = 'com.lbcap:lbcap-common-consume:+'
    public static final String DEP_PRISIDIO_COMMON_DROPWIZARD = 'com.lbcap:lbcap-common-dropwizard:+'

    //Importing TestCategories to project
    public static final String DEP_PRISIDIO_GRADLE_TASKS = 'com.lbcap:lbcap-gradle-tasks:+'

    public static final String DEP_COMMONS_LANG3 = 'org.apache.commons:commons-lang3:3.12.0'
    public static final String DEP_DROPWIZARD_AUTH = 'io.dropwizard:dropwizard-auth:2.0.25'
    public static final String DEP_DROPWIZARD_CLIENT = 'io.dropwizard:dropwizard-client:2.0.25'
    public static final String DEP_SWAGGER_CORE = 'io.swagger.core.v3:swagger-core:2.1.10'
    public static final String DEP_SWAGGER_JAXRS = 'io.swagger.core.v3:swagger-jaxrs2:2.1.10'
    public static final String DEP_SWAGGER_INTEGRATION = 'io.swagger.core.v3:swagger-integration:2.1.10'
    public static final String DEP_JUNIT_JUPITER_API = 'org.junit.jupiter:junit-jupiter-api:5.8.0'
    public static final String DEP_JUNIT_JUPITER_ENGINE = 'org.junit.jupiter:junit-jupiter-engine:5.8.0'
    public static final String DEP_MOCKITO_CORE = 'org.mockito:mockito-core:3.12.4'
    public static final String DEP_MOCKITO_JUNIT_JUPITER = 'org.mockito:mockito-junit-jupiter:3.12.4'
    public static final String DEP_JOSE4J = 'org.bitbucket.b_c:jose4j:0.7.9'
    public static final String DEP_JUNIT_PIONEER = "org.junit-pioneer:junit-pioneer:1.4.2"

    // Needed to run in EKS to get AWS credentials from WebIdentityToken
    public static final String DEP_AWS_SDK_STS = "software.amazon.awssdk:sts:2.16.24"
    public static final String DEP_AWS_JAVA_SDK_STS = "com.amazonaws:aws-java-sdk-sts:1.12.198"

    // Dependencies applied to all plugins
    static Map<String, List<String>> getCommonDependencies() {
        return [
                implementation    : [DEP_COMMONS_LANG3],
                testImplementation: [DEP_PRISIDIO_GRADLE_TASKS,
                                     DEP_JUNIT_JUPITER_API,
                                     DEP_MOCKITO_CORE,
                                     DEP_MOCKITO_JUNIT_JUPITER,
                                     DEP_JUNIT_PIONEER],
                testRuntimeOnly   : [DEP_JUNIT_JUPITER_ENGINE]] as Map<String, List<String>>
    }

    static Map<String, List<String>> getClientDependencies() {
        return [
                api           : [DEP_PRISIDIO_COMMON_CONSUME],
                implementation: [DEP_PRISIDIO_COMMON,
                                 DEP_JOSE4J]] as Map<String, List<String>>
    }

    static Map<String, List<String>> getModelDependencies() {
        return [implementation: [DEP_PRISIDIO_COMMON]] as Map<String, List<String>>
    }

    static Map<String, List<String>> getServiceDependencies() {
        return [
                implementation: [DEP_PRISIDIO_COMMON,
                                 DEP_PRISIDIO_COMMON_AUTH,
                                 DEP_PRISIDIO_COMMON_DATA,
                                 DEP_PRISIDIO_COMMON_ACTIVITY,
                                 DEP_PRISIDIO_COMMON_DROPWIZARD,
                                 DEP_DROPWIZARD_AUTH,
                                 DEP_DROPWIZARD_CLIENT,
                                 DEP_SWAGGER_CORE,
                                 DEP_SWAGGER_JAXRS,
                                 DEP_SWAGGER_INTEGRATION,
                                 DEP_JOSE4J,
                                 DEP_AWS_SDK_STS,
                                 DEP_AWS_JAVA_SDK_STS]] as Map<String, List<String>>
    }
}
