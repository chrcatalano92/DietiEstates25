plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.sonarqube)
}

android {
    namespace = "com.demo.dietiestates25"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.demo.dietiestates25"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "app_name", "Dieti Estates25")
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    lint {
        abortOnError = true
        warningsAsErrors = true
    }
}

dependencies {
    // Implementation Dependencies

    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.auth)
    implementation(libs.google.playservices.auth)

    implementation(libs.auth0.jwt)
    implementation(libs.androidx.security.crypto)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.preference)
    implementation(libs.activity)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.okhttp3)
    implementation(libs.logging.interceptor)
    implementation(libs.glide)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Annotation Processors
    annotationProcessor(libs.compiler)

    // coreLibraryDesugaring
    coreLibraryDesugaring(libs.desugaring)

    // Test Dependencies
    testImplementation(libs.junit)

    // Android Test Dependencies
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


}