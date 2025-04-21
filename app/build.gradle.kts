plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.php.ebloodconnect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.php.ebloodconnect"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        resValue("string", "mapbox_access_token", "sk.eyJ1IjoicHJlZXRhbTI5IiwiYSI6ImNtOXA5cGVrMzAzOGwycXNmMGE4NmxpM3kifQ.RtksIHjzun_JhIpCHeq3ig")

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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")

    implementation("com.mapbox.maps:android:11.11.0")
    implementation("com.mapbox.geojson:geojson:2.0.0")
    implementation("com.mapbox.navigation:core:3.8.4")
    implementation("com.mapbox.navigation:ui-components:3.8.4")
    implementation("com.mapbox.search:mapbox-search-android-ui:2.12.0-beta.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}