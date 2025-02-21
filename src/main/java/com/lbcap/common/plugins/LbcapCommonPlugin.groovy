package com.lbcap.common.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin applied to lbcap commons gradle project which is packaged as java library and published to github.
 */
class LbcapCommonPlugin extends LbcapPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        super.applyDefault(project)
        super.configureLibraryProject(project)

        def commonDep = PluginDependencies.getCommonDependencies()
        super.configurePluginHelperTask("lbcap-common-plugin", project, commonDep)
        super.configurePluginGitHookTask(project)
        project.tasks.getByName("build").dependsOn(project.tasks.getByName("installGitHooks"))
    }
}
