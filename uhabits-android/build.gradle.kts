import org.gradle.kotlin.dsl.invoke


/*
 * Copyright (C) 2016-2025 Álinson Santos Xavier <git@axavier.org>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

plugins {
    alias(libs.plugins.agp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint.plugin)
}

tasks.compileLint {
    dependsOn("updateTranslators")
}


/*
Added on top of kotlinOptions to work around this issue:
https://youtrack.jetbrains.com/issue/KTIJ-24311/task-current-target-is-17-and-kaptGenerateStubsProductionDebugKotlin-task-current-target-is-1.8-jvm-target-compatibility-should#focus=Comments-27-6798448.0-0
Updating gradle might fix this, so try again in the future to remove this and run:
./gradlew --rerun-tasks :uhabits-android:kaptGenerateStubsReleaseKotlin
If this doesn't produce any warning, try to remove it.
 */
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

        // ✅ важный раннер для инструментальных тестов
        testInstrumentationRunner = "com.kaspersky.kaspresso.runner.KaspressoRunner"
       // testInstrumentationRunnerArguments += mapOf(
          //  "listener" to "io.qameta.allure.kotlin.junit4.AllureJunit4"
      //  )//
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
        // JUnit 5 для тестов
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")

        // Appium Java Client (основная библиотека для Appium)
        testImplementation("io.appium:java-client:10.0.0")

        // Selenium (Appium Java Client основан на WebDriver)
        testImplementation("org.seleniumhq.selenium:selenium-java:4.35.0")

        // Ассерты Kotlin (удобные assertTrue, assertEquals и т.п.)
        androidTestImplementation(kotlin("test"))
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test:core:1.6.1")
        androidTestImplementation("androidx.test:rules:1.6.1")
        // Kaspresso + Allure: актуальные версии
        androidTestImplementation("com.kaspersky.android-components:kaspresso:1.6.0")
        androidTestImplementation("com.kaspersky.android-components:kaspresso-allure-support:1.6.0")
        androidTestImplementation("io.qameta.allure:allure-kotlin-android:2.4.0")
        androidTestImplementation("io.qameta.allure:allure-kotlin-junit4:2.4.0")


    }

