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
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.com.google.gms.google.services)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.com.google.firebase.crashlytics)
    alias(libs.plugins.androidx.navigation.safeargs.kotlin)
}

android {
    namespace = "com.petid.petid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.petid.petid"
        minSdk = 24
        targetSdk = 35
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_LOGIN_CLIENT_ID", "\"${localProperties["GOOGLE_LOGIN_CLIENT_ID"]}\"")
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${localProperties["KAKAO_NATIVE_APP_KEY"]}\"")
        buildConfigField("String", "NAVER_CLIENT_ID", "\"${localProperties["NAVER_CLIENT_ID"]}\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"${localProperties["NAVER_CLIENT_SECRET"]}\"")

        // manifestPlaceholders에 값을 전달
        manifestPlaceholders["kakao_native_app_key"] = localProperties["KAKAO_NATIVE_APP_KEY"] as String
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
        viewBinding = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/gradle/incremental.annotation.processors"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }

    flavorDimensions += listOf("version")
    productFlavors {
        create("develop") {
            dimension = "version"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "펫아이디 Dev")
            buildConfigField("boolean", "IS_DEVELOP", "true")
        }
        create("operation") {
            dimension = "version"
            buildConfigField("boolean", "IS_DEVELOP", "false")
        }
    }
    firebaseCrashlytics {
        mappingFileUploadEnabled = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation("io.mockk:mockk:1.13.14")
    androidTestImplementation("io.mockk:mockk-android:1.13.14")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.corbind)
    implementation(libs.corbind.material)

    implementation(libs.threetenabp)

    // material calendarView
    implementation(libs.material.calendarview)

    // Easy flip view
    implementation(libs.easyflipview)

    // kakao Login
    implementation(libs.v2.user)

    // naver
    implementation(libs.oauth.jdk8)

    // google one-tap login
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Google Play services
    implementation("com.google.gms:google-services:4.4.2")
    implementation(libs.play.services.location) // 위치
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx) // 비정상 종료 추적
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.messaging.ktx) // FCM-push

    // Logger
    implementation(libs.logger)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.ksp)

    // multidex
    implementation(libs.androidx.multidex)

    // Jetpack Compose Integration
    implementation(libs.androidx.navigation.compose)

    // Views/Fragments Integration
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Feature module support for Fragments
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

    // mediapipe
    implementation(libs.tasks.vision)
}