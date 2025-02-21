package com.lbcap.common.gradletasks.test

import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

class PostDeployIntegrationTests extends BaseTestTask {
    PostDeployIntegrationTests() {
        useJUnitPlatform()
        final JUnitPlatformOptions options = (JUnitPlatformOptions) this.getOptions()
        options.includeTags(TestCategories.POST_DEPLOY_INTEGRATION_TESTS)
        setDescription("Lbcap task that runs integration tests tagged with TestCategories.POST_DEPLOY_INTEGRATION_TESTS.")
    }
}
