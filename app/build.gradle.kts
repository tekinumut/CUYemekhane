import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.daggerHiltAndroid)
    alias(libs.plugins.navigationSafeArgs)
    kotlin("kapt")
    id("kotlin-parcelize")
}

val keystorePropertiesFile: File = rootProject.file("app/keystore/keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.tekinumut.cuyemekhane"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tekinumut.cuyemekhane"
        minSdk = 21
        targetSdk = 34
        versionCode = 110
        versionName = "1.10"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("config") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storePassword = keystoreProperties["storePassword"] as String
            storeFile = file("../app/keystore/CuYemekhane.jks")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            resValue("string", "app_name", "Cu-dev")
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = true
            resValue("string", "app_name", "CU Yemekhane")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("config")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // Core
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.splashscreen)

    // View
    implementation(libs.constraintlayout)
    implementation(libs.fragment.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.browser)
    implementation(libs.swiperefreshlayout)

    // Lifecycle Components
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    // Dagger Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.okhttp.logging)
    implementation(libs.jsoup)
    implementation(libs.glide)

    //Test
    testImplementation(libs.bundles.base.test)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}