plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20"
    id("androidx.room") version "2.8.0" apply false
    alias(libs.plugins.ksp) apply false
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
}
