package com.lbcap.common.gradletasks.buildversion;

import org.gradle.api.DefaultTask;

public class BaseVersioningTask extends DefaultTask {
    static final String BUILD_VERSION_INFO_PROP = "buildVersionInfo";

    public BaseVersioningTask() {
        setGroup("publishing");
    }
}
