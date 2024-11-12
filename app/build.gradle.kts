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
    }
    dataBinding {
        isEnabled = true
    }
    packagingOptions {
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
    implementation("com.airbnb.android:lottie:6.4.1")

    // kakao Login
    implementation("com.kakao.sdk:v2-all:2.20.0")
    implementation("com.kakao.sdk:v2-user:2.20.0")

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
    implementation("com.orhanobut:logger:2.2.0")

    // GSON
    implementation("com.google.code.gson:gson:2.11.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    ksp("com.google.dagger:hilt-android-compiler:2.49")

    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    // okHttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")

    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.fragment:fragment-ktx:1.8.2")

    // naver
    implementation("com.navercorp.nid:oauth-jdk8:5.10.0")

    implementation("com.jakewharton.threetenabp:threetenabp:1.2.0")

    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    val navVersion = "2.8.0"

    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // Views/Fragments Integration
    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")

    // Feature module support for Fragments
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    // Easy flip view
    implementation("com.wajahatkarim:EasyFlipView:3.0.3")

    // mediapipe
    implementation("com.google.mediapipe:tasks-vision:latest.release")
}

ksp {
    arg("dagger.fastInit", "ENABLED")
}
