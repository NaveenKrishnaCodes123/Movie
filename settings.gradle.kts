pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Movieapp"
include(":app")
include(":core")
include(":data")
include(":domain")

//project(":app").projectDir = File("app")
//project(":core").projectDir = File("core")
//project(":data").projectDir = File("data")
//project(":domain").projectDir = File("domain")
