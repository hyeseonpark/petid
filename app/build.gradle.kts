import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")

    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.android.petid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.android.petid"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_LOGIN_CLIENT_ID", "\"${localProperties["GOOGLE_LOGIN_CLIENT_ID"]}\"")
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${localProperties["KAKAO_NATIVE_APP_KEY"]}\"")
        buildConfigField("String", "NAVER_CLIENT_ID", "\"${localProperties["NAVER_CLIENT_ID"]}\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"${localProperties["NAVER_CLIENT_SECRET"]}\"")
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
    dataBinding {
        enable = true
    }
    packaging {
        resources {
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

configurations.configureEach {
    exclude(group = "com.google.protobuf", module = "protobuf-java")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.camera.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation("androidx.preference:preference-ktx:1.2.1")

    // splashscreen api
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")

    // kakao Login
    implementation(libs.v2.all)
    implementation(libs.v2.user)

    // naver
    implementation(libs.oauth.jdk8)

    // google one-tap login
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Google Play services
    implementation("com.google.gms:google-services:4.4.2")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Firebase dependencies without version
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging-ktx") // FCM-push

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Logger
    implementation(libs.logger)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    // okHttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // GSON
    implementation("com.google.code.gson:gson:2.11.0")

    // Glide
    implementation(libs.glide)

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.fragment:fragment-ktx:1.8.2")

    // 시간
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.0")

    // Jetpack Compose Integration
    implementation(libs.androidx.navigation.compose)

    // Views/Fragments Integration
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Feature module support for Fragments
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

    // Easy flip view
    implementation(libs.easyflipview)

    // mediapipe
    implementation("com.google.mediapipe:tasks-vision:latest.release")


    // AWS
    implementation("com.amazonaws:aws-android-sdk-mobile-client:2.13.5")
    implementation("com.amazonaws:aws-android-sdk-cognito:2.13.5")
    implementation("com.amazonaws:aws-android-sdk-s3:2.13.5")
}