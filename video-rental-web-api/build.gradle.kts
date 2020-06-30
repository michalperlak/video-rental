plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm")
    kotlin("plugin.spring")
}

val arrowVersion: String by project
val h2Version: String by project
val kotestVersion: String by project
val restAssuredVersion: String by project

dependencies {
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")

    implementation(project(":video-rental-common"))
    implementation(project(":video-rental-pricing"))
    implementation(project(":video-rental-inventory"))
    implementation(project(":video-rental-rentals"))

    implementation(kotlin("stdlib"))
    implementation("io.arrow-kt", "arrow-core", arrowVersion)
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-jdbc")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("com.h2database", "h2", h2Version)
    implementation("com.zaxxer", "HikariCP")
    implementation("org.flywaydb", "flyway-core")

    testImplementation("io.kotest", "kotest-runner-junit5-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-core-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-arrow-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-extensions-spring", kotestVersion)
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("io.rest-assured", "rest-assured", restAssuredVersion)
    testImplementation("io.rest-assured", "rest-assured-common", restAssuredVersion)
    testImplementation("io.rest-assured", "json-path", restAssuredVersion)
    testImplementation("io.rest-assured", "xml-path", restAssuredVersion)
    testImplementation("io.rest-assured", "kotlin-extensions", restAssuredVersion)
}
