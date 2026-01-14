pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven-other.tuya.com/repository/maven-commercial-releases/") }
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Syllent Connect"
include(":app")
