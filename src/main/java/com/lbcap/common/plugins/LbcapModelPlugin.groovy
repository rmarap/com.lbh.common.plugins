package com.lbcap.common.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin applied to Model sub-project that contains data model classes.
 */
class LbcapModelPlugin extends LbcapPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        super.applyDefault(project)
        super.configureLibraryProject(project)
        applyModelDependencies(project)

        def commonDep = PluginDependencies.getCommonDependencies()
        def otherDep = PluginDependencies.getModelDependencies()
        for (Map.Entry<String, List<String>> entry : otherDep.entrySet()) {
            if (commonDep.containsKey(entry.key)) {
                commonDep.get(entry.key).addAll(entry.value)
            } else {
                commonDep.put(entry.key, entry.value)
            }
        }
        super.configurePluginHelperTask("lbcap-model-plugin", project, commonDep)
    }

    private static void applyModelDependencies(Project project) {
        PluginDependencies.getModelDependencies().each { type, dep ->
            dep.each { dependency -> project.dependencies.add(type, dependency) }
        }
    }
}
