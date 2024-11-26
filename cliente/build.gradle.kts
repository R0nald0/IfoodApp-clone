plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.hiltAndroidCompiler)
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id ("kotlin-parcelize")
  //  id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.ifood_clone2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ifood_clone2"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    //https://github.com/bumptech/glide
    implementation (libs.glide)

    // https://github.com/denzcoskun/ImageSlideshow
    implementation (libs.imageslideshow)

    // Views/Fragments integration
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // ViewModel
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    // LiveData
    implementation (libs.androidx.lifecycle.livedata.ktx)
    // Lifecycles only (without ViewModel or LiveData)
    implementation (libs.androidx.lifecycle.runtime.ktx )

    //Fragment-KTX
    implementation (libs.androidx.fragment.ktx)

    //SPLASH SCREEN
    implementation(libs.androidx.core.splashscreen)

    // Import the Firebase BoM
    implementation(libs.firebase.bom)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.firestore.ktx)

    implementation(project(":domain"))
    //hilt
    implementation(libs.hilt.android)
    implementation(project(":data"))
    implementation(project(":core"))
    kapt(libs.hilt.android.compiler)

    // lib de permisssoes android

    implementation (libs.permissionx)

    //mascaras
    implementation (libs.maskededittext)
    //EASY Validation
    implementation (libs.easyvalidation.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
kapt {
    correctErrorTypes = true
}