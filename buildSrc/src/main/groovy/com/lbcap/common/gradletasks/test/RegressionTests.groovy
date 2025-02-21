package com.lbcap.common.gradletasks.test

import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

class RegressionTests extends BaseTestTask {
    RegressionTests() {
        useJUnitPlatform()
        final JUnitPlatformOptions options = (JUnitPlatformOptions) this.getOptions()
        options.includeTags(TestCategories.REGRESSION_TESTS)
        setDescription("Lbcap task that runs all regression tests.")
    }
}
