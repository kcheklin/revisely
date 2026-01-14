plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myapp"
<<<<<<< HEAD
    compileSdk = 34
=======
    compileSdk {
        version = release(36)
    }
>>>>>>> bd114bac6a70ab1f02e6755026cdb2a87cfd4084

    defaultConfig {
        applicationId = "com.example.myapp"
        minSdk = 24
<<<<<<< HEAD
        targetSdk = 34
=======
        targetSdk = 36
>>>>>>> bd114bac6a70ab1f02e6755026cdb2a87cfd4084
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.cardview:cardview:1.0.0")
    // MPAndroidChart for pie chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
<<<<<<< HEAD
    
    // Retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
=======
>>>>>>> bd114bac6a70ab1f02e6755026cdb2a87cfd4084
}