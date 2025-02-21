package com.lbcap.common.gradletasks.test

import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

class PreDeployIntegrationTests extends BaseTestTask {
    PreDeployIntegrationTests() {
        useJUnitPlatform()
        final JUnitPlatformOptions options = (JUnitPlatformOptions) this.getOptions()
        options.includeTags(TestCategories.PRE_DEPLOY_INTEGRATION_TESTS)
        setDescription("Lbcap task that runs integration tests tagged with TestCategories.PRE_DEPLOY_INTEGRATION_TESTS.")
    }
}
