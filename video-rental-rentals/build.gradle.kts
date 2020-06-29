plugins {
    kotlin("jvm")
}

val arrowVersion: String by project
val exposedVersion: String by project
val slf4jVersion: String by project
val logbackVersion: String by project
val h2Version: String by project
val flywayVersion: String by project
val kotestVersion: String by project
val mockkVersion: String by project

dependencies {
    implementation(project(":video-rental-pricing"))
    implementation(project(":video-rental-inventory"))

    implementation(kotlin("stdlib"))
    implementation("io.arrow-kt", "arrow-core", arrowVersion)
    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-java-time", exposedVersion)
    implementation("org.slf4j", "slf4j-api", slf4jVersion)
    implementation("ch.qos.logback", "logback-classic", logbackVersion)

    testImplementation("io.kotest", "kotest-runner-junit5-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-core-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-arrow-jvm", kotestVersion)
    testImplementation("io.mockk", "mockk", mockkVersion)
    testImplementation("com.h2database", "h2", h2Version)
    testImplementation("org.flywaydb", "flyway-core", flywayVersion)
}