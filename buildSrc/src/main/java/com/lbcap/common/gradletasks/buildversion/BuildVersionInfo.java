package com.lbcap.common.gradletasks.buildversion;

import com.lbcap.common.gradletasks.FileUtil;
import com.lbcap.common.gradletasks.SerializationUtil;
import org.gradle.api.plugins.ExtraPropertiesExtension;

import java.io.IOException;

public class BuildVersionInfo {

    final static String VERSION_FILE = ".version/version.json";
    final static String VERSION_TEMPLATE_FILE = ".version/version.template.json";
    private long majorVersion;
    private long minorVersion;
    private long buildNumber;

    public BuildVersionInfo() {
    }

    public BuildVersionInfo(long majorVersion, long minorVersion, long buildNumber) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.buildNumber = buildNumber;
    }

    /**
     * @deprecated Use setBuildVersionFromFile instead.
     */
    @Deprecated
    public static void readNextVersionNumber(ExtraPropertiesExtension defaultExtraPropertiesExtension, String projectRootPath) throws IOException {
        defaultExtraPropertiesExtension.set(BaseVersioningTask.BUILD_VERSION_INFO_PROP, fromJson(projectRootPath));
    }

    public static void setBuildVersionFromFile(ExtraPropertiesExtension defaultExtraPropertiesExtension, String projectRootPath) throws IOException {
        defaultExtraPropertiesExtension.set(BaseVersioningTask.BUILD_VERSION_INFO_PROP, fromJson(projectRootPath));
    }

    public static void toJson(BuildVersionInfo info, String projectRootPath) throws IOException {
        String jsonTemplate = FileUtil.readFile(getVersionTemplateFilePath(projectRootPath));
        FileUtil.saveFile(String.format(jsonTemplate, info.majorVersion, info.minorVersion, info.buildNumber), getVersionFilePath(projectRootPath));
    }

    private static BuildVersionInfo fromJson(String projectRootPath) throws IOException {
        String json = FileUtil.readFile(getVersionFilePath(projectRootPath));
        BuildVersionInfo buildVersionInfo = SerializationUtil.instanceFromJson(BuildVersionInfo.class, json);
        System.out.println("Current build Version: " + buildVersionInfo.getVersionNumber());
        return buildVersionInfo;
    }

    private static String getVersionFilePath(String projectRootPath) {
        return projectRootPath + "/" + VERSION_FILE;
    }

    private static String getVersionTemplateFilePath(String projectRootPath) {
        return projectRootPath + "/" + VERSION_TEMPLATE_FILE;
    }

    public String getVersionNumber() {
        return String.format("%d.%d.%d", majorVersion, minorVersion, buildNumber);
    }

    public long getMajorVersion() {
        return this.majorVersion;
    }

    public void setMajorVersion(long majorVersion) {
        this.majorVersion = majorVersion;
    }

    public long getMinorVersion() {
        return this.minorVersion;
    }

    public void setMinorVersion(long minorVersion) {
        this.minorVersion = minorVersion;
    }

    public long getBuildNumber() {
        return this.buildNumber;
    }

    public void setBuildNumber(long buildNumber) {
        this.buildNumber = buildNumber;
    }


}
