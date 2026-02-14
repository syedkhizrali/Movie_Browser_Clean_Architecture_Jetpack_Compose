

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)


}

android {
    namespace = "com.moviebrowser"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.moviebrowser"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Expose environment variables to BuildConfig.
        // Prevents hardcoding API base URLs and tokens in source code.
        buildConfigField(
            "String",
            "BASE_URL",
            "\"${project.property("BASE_URL")}\""
        )

        buildConfigField(
            "String",
            "IMAGE_URL",
            "\"${project.property("IMAGE_URL")}\""
        )

        buildConfigField(
            "String",
            "TMDB_TOKEN",
            "\"${project.property("TMDB_TOKEN")}\""
        )
    }



    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true

        // Enable BuildConfig generation
        // Required to access BASE_URL, IMAGE_URL, TMDB_TOKEN
        android.buildFeatures.buildConfig = true
    }
}

dependencies {

    // -------- CORE AND LIFECYCLE --------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // -------- COMPOSE UI --------
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // -------- NETWORKING --------
    implementation(libs.retrofit.core)                 // API calls
    implementation(libs.kotlinx.serialization.converter) // Retrofit converter
    implementation(libs.serialization.json)            // JSON serialization
    implementation(libs.okhttp)                        // HTTP client
    implementation(libs.okhttp.interceptor)            // Logging / custom interceptors

    // -------- LOCAL DATABASE (ROOM) --------
    implementation(libs.room.ktx)                      // Kotlin extensions
    implementation(libs.room.runtime)                  // Core Room
    implementation(libs.room.paging)                   // Paging + Room integration

    // -------- PAGING --------
    implementation(libs.paging3)                       // Paging 3 library

    // -------- VIEWMODEL --------
    implementation(libs.viewmodel.compose)             // ViewModel integration with Compose

    // -------- IMAGE LOADING --------
    implementation(libs.coil)                          // Async image loading

    // -------- LOGGING --------
    implementation(libs.timber)                        // Logging utility

    // -------- DEPENDENCY INJECTION --------
    implementation(libs.hilt.android)                  // Hilt core
    implementation(libs.hilt.navigation.compose)       // Hilt + Compose integration

    // -------- JSON PARSING (MOSHI) --------
    implementation(libs.moshi.core)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.converter)

    // -------- KSP (CODE GENERATION) --------
    ksp(libs.room.compiler)            // Generates Room database code
    ksp(libs.moshi.codegen)            // Generates Moshi adapters
    ksp(libs.hilt.android.compiler)    // Generates Hilt components
    ksp(libs.kotlin.metadata.jvm)

    // -------- TESTING --------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug tools
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
