plugins { id("org.jetbrains.kotlin.jvm") }
repositories { mavenCentral() }

dependencies {
    implementation(platform("org.seleniumhq.selenium:selenium-bom:4.23.0"))
    implementation("org.seleniumhq.selenium:selenium-api")
    implementation("org.seleniumhq.selenium:selenium-remote-driver")
    implementation("org.seleniumhq.selenium:selenium-support")

    implementation("io.appium:java-client:10.0.0")

    // JUnit — обычно это testImplementation (если тесты в src/test)
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testImplementation(kotlin("test"))
}

kotlin { jvmToolchain(21) }
tasks.test { useJUnitPlatform() }