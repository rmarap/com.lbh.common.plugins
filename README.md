# lbcap-gradle-tasks
Custom gradle tasks.

## Contribution
- Declare all dependencies in PluginDependencies.groovy

## Usage
This repo creates 4 different plugins for lbcap gradle projects.

#### Add below entries in gradle.properties in root directory of project
```shell
GITHUB_PKGS_USERID=lbcap-z-bot
GITHUB_PKGS_ACCESS_TOKEN=9e3e89bff3a3cf05bc219c9fa56e1447b1611b32
GITHUB_GRADLE_TASKS_URL=https://maven.pkg.github.com/lbcap/lbcap-gradle-tasks
GRADLE_TASKS_LIBRARY=com.lbcap:lbcap-gradle-tasks:+

# Add below for projects containig SVC. e.g. user-profile-service
mainClassName='com.lbcap.<Package>.<SvcDriver class>'
SWAGGER_RESOURCES_PACKAGE='com.lbcap.<package>.resources'

# Add below for commons project and multi-module projects containing client and model. e.g. user-profile-svc, lbcap-commons
GITHUB_PKGS_URL=https://maven.pkg.github.com/lbcap/<Github repo name>
```

#### Use appropriate project name
For gradle project that contains /src in root directory, e.g. lbcap-commons,
add `rootProject.name = '<Project Name>'` in settings.gradle

For gradle project that contains multiple modules, and /src is inside module, e.g. lbcap-svc-v1,
add `rootProject.name = 'com.lbcap.<Project Name>'` in settings.gradle

```
Note: 'group' and 'name' properties in gradle project are used when publishing artifacts or to store JAR metadata.
When using multi-module project, `rootProject.name` is used as `group` and module name is used as `name`.
But in single-module project, `rootProject.name` is used as `name` and `group` remains empty.
```

#### Add gradle build dependency on `lbcap-gradle-tasks`
```groovy
buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            name = "GitHubPackages"
            url = uri(GITHUB_GRADLE_TASKS_URL)
            credentials {
                username = GITHUB_PKGS_USERID
                password = GITHUB_PKGS_ACCESS_TOKEN
            }

        }
    }
    dependencies {
        classpath GRADLE_TASKS_LIBRARY
    }
}
```

#### Apply plugins 
Apply one of the below plugins after `plugins` section
```groovy
buildscript {
//   ...
}
plugins {
//   ...
}
// For service projects e.g. user-profile-svc/svc
apply plugin: "com.lbcap.plugins.lbcap-service-plugin"
// For client projects e.g. user-profile-svc/client
apply plugin: "com.lbcap.plugins.lbcap-client-plugin"
// For model projects e.g. user-profile-svc/model
apply plugin: "com.lbcap.plugins.lbcap-model-plugin"
// For common projects e.g. lbcap-commons-data
apply plugin: "com.lbcap.plugins.lbcap-common-plugin"
```

## Tasks
Applying plugins automatically add tasks.
No manual configuration is required in `build.gradle` apart from applying the plugin.

### Default Tasks
These tasks are automatically added to build by all plugins.

#### incrementAndSaveVersionNumber
This task can be used to automatically increment build version on specific project build stages.
Plugins take care of invoking this task.

#### Verification Tasks
Four lbcap test tasks are available. `integrationTests` `regressionTests` `allTests` and `unitTests`.

`unitTests` are executed during `build` stage (`./gradlew clean build`). 
They can be manually invoked via `./gradlew unitTests`
Other tests can also be manually invoked in similar fashion.

`allTests` runs all tests (unit, integration and regression)

### Shadow JAR Task
This task is added by `lbcap-service-plugin` to create a FAT jar.

### Swagger Task
`resolve` task is added by `lbcap-service-plugin`.

Run `./gradlew resolve` to generate swagger docs.

### Publish Task
`publish` task is added by `lbcap-client-plugin`, `lbcap-model-plugin` and `lbcap-common-plugin`.
It is used to publish JAR to github repositories.
