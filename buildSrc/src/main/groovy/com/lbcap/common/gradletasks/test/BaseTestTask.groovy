package com.lbcap.common.gradletasks.test

import org.gradle.api.tasks.testing.Test

class BaseTestTask extends Test{
    BaseTestTask(){
        // https://github.com/aws/aws-xray-sdk-java/blob/master/aws-xray-recorder-sdk-core/src/main/java/com/amazonaws/xray/strategy/ContextMissingStrategy.java
        setSystemProperties("com.amazonaws.xray.strategy.contextMissingStrategy":"IGNORE_ERROR")
        setGroup("verification")
        testLogging {
            events = ["passed", "skipped", "failed", "standardOut", "standardError"]
        }
        afterSuite { desc, result ->
            if (!desc.parent) { // will match the outermost suite
                def output = "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped)"
                def startItem = '|  ', endItem = '  |'
                def repeatLength = startItem.length() + output.length() + endItem.length()
                println('\n' + ('-' * repeatLength) + '\n' + startItem + output + endItem + '\n' + ('-' * repeatLength))
            }
        }
    }
}
