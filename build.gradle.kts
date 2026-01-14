// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("kotlin_version", "1.9.22")
        set("biz_bom_version", "6.11.1")
        set("sdk_version", "6.11.1")
    }

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven-other.tuya.com/repository/maven-commercial-releases/") }
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kotlin_version"]}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven-other.tuya.com/repository/maven-commercial-releases/") }
        maven { url = uri("https://jitpack.io") }
    }

    configurations.all {
        resolutionStrategy {
            force("com.squareup.okhttp3:okhttp:4.9.1")
            force("com.squareup.okio:okio:2.10.0")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
