package com.lbcap.common.gradletasks.test

import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

class AllTests extends BaseTestTask {
    AllTests() {
        useJUnitPlatform()
        final JUnitPlatformOptions options = (JUnitPlatformOptions) this.getOptions()
        options.includeTags(TestCategories.UNIT_TESTS, TestCategories.INTEGRATION_TESTS, TestCategories.REGRESSION_TESTS, TestCategories.PRE_DEPLOY_INTEGRATION_TESTS, TestCategories.POST_DEPLOY_INTEGRATION_TESTS)
        setDescription("Lbcap task that runs all lbcap annotated tests.")
    }
}
