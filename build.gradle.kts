import org.jetbrains.kotlin.utils.addToStdlib.safeAs

// Top-level build file where you can add configuration options common to all sub-projects/modules.


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.googleServices)
  //  alias(libs.plugins.hiltAndroidCompiler)
    id("com.google.dagger.hilt.android") version "2.48" apply false
    alias(libs.plugins.safeArgs)  apply false
    alias(libs.plugins.android.library) apply false
}