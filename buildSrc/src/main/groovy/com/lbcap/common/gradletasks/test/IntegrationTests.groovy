package com.lbcap.common.gradletasks.test

import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

class IntegrationTests extends BaseTestTask {
    IntegrationTests() {
        useJUnitPlatform()
        final JUnitPlatformOptions options = (JUnitPlatformOptions) this.getOptions()
        options.includeTags(TestCategories.INTEGRATION_TESTS, TestCategories.PRE_DEPLOY_INTEGRATION_TESTS, TestCategories.POST_DEPLOY_INTEGRATION_TESTS)
        setDescription("Lbcap task that runs all integration tests.")
    }
}
