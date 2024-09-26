plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.zzangse.attendance_check"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zzangse.attendance_check"
        minSdk = 24
        targetSdk = 34
        versionCode = 8
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
    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.volley)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.kakao.sdk:v2-user:2.20.1" )// 카카오 로그인 API 모듈// 카카오 로그인 API 모듈
    implementation("com.google.android.gms:play-services-ads:22.6.0")
}