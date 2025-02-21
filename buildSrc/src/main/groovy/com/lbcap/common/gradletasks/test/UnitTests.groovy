package com.lbcap.common.gradletasks.test

import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

class UnitTests extends BaseTestTask {
    UnitTests() {
        useJUnitPlatform()
        final JUnitPlatformOptions options = (JUnitPlatformOptions) this.getOptions()
        options.includeTags(TestCategories.UNIT_TESTS)
        setDescription("Lbcap task that runs all unit tests.")
    }

}
