import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

val arrowVersion: String by project
val reactorNettyVersion: String by project
val reactorKotlinVersion: String by project
val kotestVersion: String by project
val restAssuredVersion: String by project

dependencies {
    implementation(project(":video-rental-inventory"))

    implementation(kotlin("stdlib"))
    implementation("io.arrow-kt", "arrow-core", arrowVersion)
    implementation("io.projectreactor.netty", "reactor-netty", reactorNettyVersion)
    implementation("io.projectreactor.kotlin", "reactor-kotlin-extensions", reactorKotlinVersion)

    testImplementation("io.kotest", "kotest-runner-junit5-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-core-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-arrow-jvm", kotestVersion)
    testImplementation("io.rest-assured", "kotlin-extensions", restAssuredVersion)
}

val mainClass = "pl.michalperlak.videorental.VideoRentalApplicationKt"
application {
    mainClassName = mainClass
}

tasks.withType<ShadowJar>() {
    manifest {
        attributes["Main-Class"] = mainClass
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
