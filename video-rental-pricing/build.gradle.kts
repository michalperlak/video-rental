plugins {
    kotlin("jvm")
}

val arrowVersion: String by project
val moneyApiVersion: String by project
val kotestVersion: String by project

dependencies {
    implementation(project(":video-rental-common"))

    implementation(kotlin("stdlib"))
    implementation("io.arrow-kt", "arrow-core", arrowVersion)
    implementation("org.javamoney", "moneta", moneyApiVersion)

    testImplementation("io.kotest", "kotest-runner-junit5-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-core-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-arrow-jvm", kotestVersion)
}