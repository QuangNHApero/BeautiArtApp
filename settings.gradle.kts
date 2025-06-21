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
        maven {
            credentials {
                username = "software-inhouse"
                password = "apero@123"
            }
            url = uri("https://artifactory.apero.vn/artifactory/gradle-release/")
        }
    }
}

rootProject.name = "Art Beautify"
include(":app")
include(":aperoaiservice")
include(":beautisdk")
include(":common")
