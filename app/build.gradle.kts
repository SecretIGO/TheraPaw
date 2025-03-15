plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.experiments.therapaw"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.experiments.therapaw"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            res {
                srcDirs(
                    "src\\main\\res",
                    "src\\main\\res\\layouts\\auth\\signin",
                    "src\\main\\res\\layouts\\auth\\signup",
                    "src\\main\\res\\layouts\\main\\main",
                    "src\\main\\res\\layouts\\profile\\pet",
                    "src\\main\\res\\layouts\\profile\\user",
                    "src\\main\\res\\layouts\\splash\\splash"
                )
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation (libs.glide)
    implementation(libs.osmdroid.android)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    ksp(libs.androidx.room.compiler)
    ksp(libs.compiler)

    implementation(libs.mpandroidchart)
}