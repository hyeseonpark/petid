// Top-level build file where you can add configuration options common to all sub-projects/modules.
// FCM
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath(libs.hilt.android.gradle.plugin)
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false

    // ksp
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false

    // Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false

    // Navigation Safe Args
    id("androidx.navigation.safeargs.kotlin") version "2.8.3" apply false
}

/*
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
*/
