val versions = mapOf(
        // config
        "appId" to "com.tekinumut.cuyemekhane",
        "minSdk" to 21,
        "compileSdk" to 33,
        "versionCode" to 110,
        "versionName" to "1.10",

        // Core
        "coreKtx" to "1.10.1",
        "appCompat" to "1.6.1",
        "material" to "1.9.0",
        "splashScreen" to "1.0.1",

        // View
        "constraintLayout" to "2.1.4",
        "fragmentKtx" to "1.6.0",
        "navigationKtx" to "2.6.0",
        "browser" to "1.5.0",
        "swipeRefresh" to "1.1.0",

        // Dagger Hilt
        "daggerHilt" to "2.47",

        // Lifecycle Components
        "lifecycle" to "2.6.1",

        // Network
        "retrofit" to "2.9.0",
        "okhttpLogging" to "4.10.0",
        "jsoup" to "1.16.1",
        "glide" to "4.15.1",

        // Test
        "junit" to "4.13.2",
        "testJunit" to "1.1.5",
        "espresso" to "3.5.1"
)

val libraries = mapOf(
        // Core
        "coreKtx" to "androidx.core:core-ktx:${versions["coreKtx"]}",
        "appCompat" to "androidx.appcompat:appcompat:${versions["appCompat"]}",
        "material" to "com.google.android.material:material:${versions["material"]}",
        "splashScreen" to "androidx.core:core-splashscreen:${versions["splashScreen"]}",

        // View
        "constraintLayout" to "androidx.constraintlayout:constraintlayout:${versions["constraintLayout"]}",
        "fragmentKtx" to "androidx.fragment:fragment-ktx:${versions["fragmentKtx"]}",
        "navigationFragmentKtx" to "androidx.navigation:navigation-fragment-ktx:${versions["navigationKtx"]}",
        "navigationUiKtx" to "androidx.navigation:navigation-ui-ktx:${versions["navigationKtx"]}",
        "browser" to "androidx.browser:browser:${versions["browser"]}",
        "swipeRefresh" to "androidx.swiperefreshlayout:swiperefreshlayout:${versions["swipeRefresh"]}",

        // Lifecycle Components
        "lifecycleViewModel" to "androidx.lifecycle:lifecycle-viewmodel-ktx:${versions["lifecycle"]}",
        "lifecycleRuntime" to "androidx.lifecycle:lifecycle-runtime-ktx:${versions["lifecycle"]}",

        // Dagger Hilt
        "daggerHilt" to "com.google.dagger:hilt-android:${versions["daggerHilt"]}",
        "daggerHiltCompiler" to "com.google.dagger:hilt-compiler:${versions["daggerHilt"]}",

        // Network
        "retrofit" to "com.squareup.retrofit2:retrofit:${versions["retrofit"]}",
        "retrofitScalars" to "com.squareup.retrofit2:converter-scalars:${versions["retrofit"]}",
        "okhttpLogging" to "com.squareup.okhttp3:logging-interceptor:${versions["okhttpLogging"]}",
        "jsoup" to "org.jsoup:jsoup:${versions["jsoup"]}",
        "glide" to "com.github.bumptech.glide:glide:${versions["glide"]}",
        "glideCompiler" to "com.github.bumptech.glide:compiler:${versions["glide"]}",

        // Test
        "junit" to "junit:junit:${versions["junit"]}",
        "testJunit" to "androidx.test.ext:junit:${versions["testJunit"]}",
        "espresso" to "androidx.test.espresso:espresso-core:${versions["espresso"]}"
)