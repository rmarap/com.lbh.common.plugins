package com.lbcap.common.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin applied to Client sub-project that contains http client library for specific service.
 */
class LbcapClientPlugin extends LbcapPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        super.applyDefault(project)
        super.configureLibraryProject(project)
        applyLibraryDependencies(project)

        def commonDeps = PluginDependencies.getCommonDependencies()
        def otherDeps = PluginDependencies.getClientDependencies()
        for (Map.Entry<String, List<String>> entry : otherDeps.entrySet()) {
            if (commonDeps.containsKey(entry.key)) {
                commonDeps.get(entry.key).addAll(entry.value)
            } else {
                commonDeps.put(entry.key, entry.value)
            }
        }
        super.configurePluginHelperTask("lbcap-client-plugin", project, commonDeps)
    }

    private static void applyLibraryDependencies(Project project) {
        PluginDependencies.getClientDependencies().each { type, dep ->
            dep.each { dependency -> project.dependencies.add(type, dependency) }
        }
    }
}
