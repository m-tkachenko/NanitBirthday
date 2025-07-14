import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    // Module dependencies
    implementation(project(":core"))

    // Coroutines for async business logic
    implementation(libs.kotlinx.coroutines.core)

    // Date/time utilities
    implementation(libs.kotlinx.datetime)
}
