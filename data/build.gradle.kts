import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    // id("org.jetbrains.kotlin.plugin.serialization") version "1.5.0"
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.petid.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "AWS_ACCESS_KEY", "\"${localProperties["AWS_ACCESS_KEY"]}\"")
        buildConfigField("String", "AWS_SECRET_KEY", "\"${localProperties["AWS_SECRET_KEY"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"http://yourpet-id.com:8080/\"")
        }
        debug {
            buildConfigField("String", "BASE_URL", "\"http://yourpet-id.com:8080/\"")
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.tools.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":domain")) // Domain Layer에 대한 의존성

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Logger
    implementation("com.orhanobut:logger:2.2.0")

    // AWS
    implementation("com.amazonaws:aws-android-sdk-mobile-client:2.13.5")
    implementation("com.amazonaws:aws-android-sdk-cognito:2.13.5")
    implementation("com.amazonaws:aws-android-sdk-s3:2.13.5")

    // mediapipe
    implementation(libs.tasks.vision)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}