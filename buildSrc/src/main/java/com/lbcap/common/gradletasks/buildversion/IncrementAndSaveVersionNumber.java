package com.lbcap.common.gradletasks.buildversion;

import com.google.common.base.Preconditions;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.util.Objects;

public class IncrementAndSaveVersionNumber extends BaseVersioningTask {

    @TaskAction
    public void incrementAndSaveVersionNumber() throws IOException {

        final Object versionInfoObj = getProject().getExtensions().getExtraProperties().get(BUILD_VERSION_INFO_PROP);
        Preconditions.checkState(versionInfoObj instanceof BuildVersionInfo);

        BuildVersionInfo buildVersionInfo = (BuildVersionInfo) versionInfoObj;

        long newBuildNumber = buildVersionInfo.getBuildNumber() + 1;
        buildVersionInfo.setBuildNumber(newBuildNumber);

        BuildVersionInfo.toJson(buildVersionInfo, getProject().getRootDir().getAbsolutePath());

        long expectedBuildNumber = ((BuildVersionInfo) Objects.requireNonNull(getProject().getExtensions().getExtraProperties().get(BUILD_VERSION_INFO_PROP))).getBuildNumber();
        Preconditions.checkState(expectedBuildNumber == newBuildNumber);
    }

}
