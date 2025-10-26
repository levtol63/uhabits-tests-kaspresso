import org.gradle.kotlin.dsl.invoke


plugins {
    alias(libs.plugins.agp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint.plugin)
}

tasks.compileLint {
    dependsOn("updateTranslators")
}
kotlin {
    jvmToolchain(17)
}

android {
    namespace = "org.isoron.uhabits"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.isoron.uhabits"
        minSdk = 28
        targetSdk = 36
        versionCode = 20301
        versionName = "2.3.1"

        testInstrumentationRunner = "com.kaspersky.kaspresso.runner.KaspressoRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.txt"
            )
            signingConfigs.findByName("release")?.let { signingConfig = it }
        }
        debug {
            enableUnitTestCoverage = true
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility(JavaVersion.VERSION_17)
        targetCompatibility(JavaVersion.VERSION_17)
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
    }

    lint {
        abortOnError = false
    }
}

    dependencies {
        compileOnly(libs.jsr250.api)
        coreLibraryDesugaring(libs.desugar.jdk.libs)
        implementation(libs.appIntro)
        implementation(libs.jsr305)
        implementation(libs.dagger)
        implementation(libs.guava)
        implementation(libs.ktor.client.android)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.jackson)
        implementation(libs.ktor.client.json)
        implementation(libs.kotlin.stdlib.jdk8)
        implementation(libs.kotlinx.coroutines.android)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.appcompat)
        implementation(libs.legacy.preference.v14)
        implementation(libs.legacy.support.v4)
        implementation(libs.material)
        implementation(libs.opencsv)
        implementation(libs.konfetti.xml)
        implementation(project(":uhabits-core"))
        ksp(libs.dagger.compiler)


        androidTestImplementation(libs.bundles.androidTest)
        testImplementation(libs.bundles.test)

        testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")

        testImplementation("io.appium:java-client:10.0.0")

        testImplementation("org.seleniumhq.selenium:selenium-java:4.35.0")

        androidTestImplementation(kotlin("test"))
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test:core:1.6.1")
        androidTestImplementation("androidx.test:rules:1.6.1")

        androidTestImplementation("com.kaspersky.android-components:kaspresso:1.6.0")
        androidTestImplementation("com.kaspersky.android-components:kaspresso-allure-support:1.6.0")
        androidTestImplementation("io.qameta.allure:allure-kotlin-android:2.4.0")
        androidTestImplementation("io.qameta.allure:allure-kotlin-junit4:2.4.0")
        androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.2.0")


    }

