plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.syllent.connectdev"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.syllent.connectdev"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        multiDexEnabled = true

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }

        buildConfigField("String", "TUYA_APP_KEY", "\"puctaw54epemuetxm4j5\"")
        buildConfigField("String", "TUYA_APP_SECRET", "\"ghfuvprrq5f39rwwcr4jhetgqgsxuusg\"")

        manifestPlaceholders["TUYA_SMART_APPKEY"] = "puctaw54epemuetxm4j5"
        manifestPlaceholders["TUYA_SMART_SECRET"] = "ghfuvprrq5f39rwwcr4jhetgqgsxuusg"
        manifestPlaceholders["PACKAGE_NAME"] = "com.syllent.connectdev"
        manifestPlaceholders["APP_SCHEME_NAME"] = "syllentconnect"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    packaging {
        resources {
            pickFirsts += listOf(
                "lib/*/liblog.so",
                "lib/*/libc++_shared.so",
                "lib/*/libyuv.so",
                "lib/*/libopenh264.so",
                "lib/*/libv8wrapper.so",
                "lib/*/libv8android.so"
            )
            excludes += listOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module"
            )
        }
    }

    lint {
        abortOnError = false
    }
}

dependencies {
    // Tuya SmartLife SDK BOM
    implementation(platform("com.thingclips.smart:thingsmart-BizBundlesBom:6.11.1"))

    // Tuya SDK Core
    implementation("com.thingclips.smart:thingsmart:6.11.1")

    // Tuya BizBundle - Device Activator (Pairing)
    implementation("com.thingclips.smart:thingsmart-bizbundle-device_activator")

    // Tuya BizBundle - QR Code Scanner
    implementation("com.thingclips.smart:thingsmart-bizbundle-qrcode_mlkit")

    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // Apache Ant (required by Tuya SDK)
    implementation("org.apache.ant:ant:1.10.5")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
