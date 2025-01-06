// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.google.services)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false

    // ksp
    alias(libs.plugins.com.google.devtools.ksp) apply false

    // Google services Gradle plugin
    alias(libs.plugins.com.google.gms.google.services) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    alias(libs.plugins.com.google.firebase.crashlytics) apply false

    // Navigation Safe Args
    alias(libs.plugins.androidx.navigation.safeargs.kotlin) apply false
}

/*
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
*/
