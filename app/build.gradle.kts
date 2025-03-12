import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.timife.cowryconverter"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.timife.cowryconverter"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties().apply {
            load(FileInputStream(rootProject.file("gradle.properties")))
        }

        buildConfigField("String", "API_KEY", "\"${properties.getProperty("api-key")}\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.room)
    implementation(libs.roomKtx)
    implementation(libs.hilt)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.retrofit)
    implementation(libs.coroutinesAdapter)
    implementation(libs.coroutineCore)
    implementation(libs.coroutineAndroid)
    implementation(libs.viewModelCompose)
    implementation(libs.moshi)
    implementation(libs.paging)
    implementation(libs.paging.compose)
    implementation(libs.roomPaging)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.roomCompiler)
    implementation(libs.timber)
    implementation(libs.coroutineCore)
    implementation(libs.coroutineAndroid)
    implementation(libs.coroutinesAdapter)
    implementation(libs.hiltCompose)
    implementation(libs.materialIcons)
    implementation(libs.coil)
    implementation(libs.okHttp)
    implementation(libs.interceptor)
    implementation(libs.moshi.converter)
    implementation(libs.moshi.kotlin)
    implementation(libs.json.serializer)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.interceptor)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.chuckerLib)



    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.core.test)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.runner)

    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.core.test)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}