import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    val keyPropertiesFile = rootProject.file("key.properties")
    val keyProperties = Properties()
    keyProperties.load(FileInputStream(keyPropertiesFile))

    namespace = "com.ramdani.danamon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ramdani.danamon"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "baseUrl", keyProperties.getProperty("baseUrl"))
            buildConfigField("String", "logoUrl", keyProperties.getProperty("logoUrl"))
        }
        release {
            buildConfigField("String", "baseUrl", keyProperties.getProperty("baseUrl"))
            buildConfigField("String", "logoUrl", keyProperties.getProperty("logoUrl"))
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    /** Network */
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    /** RxLibrary */
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.10")
    implementation("com.jakewharton.rxbinding3:rxbinding:3.1.0")

    /** Koin */
    implementation("io.insert-koin:koin-androidx-scope:2.2.3")
    implementation("io.insert-koin:koin-androidx-viewmodel:2.2.3")
    implementation("io.insert-koin:koin-androidx-fragment:2.2.3")

    /** Glide */
    implementation("com.github.bumptech.glide:glide:4.16.0")


}