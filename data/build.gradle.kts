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
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.com.google.dagger.hilt.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.petid.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

configurations.configureEach {
    exclude(group = "com.google.protobuf", module = "protobuf-java")
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.tools.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.firebase.crashlytics.ktx) // 비정상 종료 추적
    implementation(libs.firebase.analytics.ktx)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Logger
    implementation(libs.logger)

    // AWS
    implementation(libs.amazonaws.aws.android.sdk.mobile.client)
    implementation(libs.amazonaws.aws.android.sdk.cognito)
    implementation(libs.amazonaws.aws.android.sdk.s3)

    // mediapipe
    implementation(libs.tasks.vision)

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}