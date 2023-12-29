import java.io.FileInputStream
import java.util.Properties

buildscript {
    apply(from = "../dependency.gradle.kts")
}
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

val keystorePropertiesFile = rootProject.file("app/keystore/keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = versions.appId
    compileSdk = versions.compileSdk

    defaultConfig {
        applicationId = versions.appId
        minSdk = versions.minSdk
        targetSdk = versions.compileSdk
        versionCode = versions.versionCode
        versionName = versions.versionName

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
    implementation(libraries.coreKtx)
    implementation(libraries.appCompat)
    implementation(libraries.material)
    implementation(libraries.splashScreen)

    // View
    implementation(libraries.constraintLayout)
    implementation(libraries.fragmentKtx)
    implementation(libraries.navigationFragmentKtx)
    implementation(libraries.navigationUiKtx)
    implementation(libraries.browser)
    implementation(libraries.swipeRefresh)

    // Lifecycle Components
    implementation(libraries.lifecycleViewModel)
    implementation(libraries.lifecycleRuntime)

    // Dagger Hilt
    implementation(libraries.daggerHilt)
    kapt(libraries.daggerHiltCompiler)

    // Network
    implementation(libraries.retrofit)
    implementation(libraries.retrofitScalars)
    implementation(libraries.okhttpLogging)
    implementation(libraries.jsoup)
    implementation(libraries.glide)
    kapt(libraries.glideCompiler)

    //Test
    testImplementation(libraries.junit)
    androidTestImplementation(libraries.testJunit)
    androidTestImplementation(libraries.espresso)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}