plugins {
    kotlin("jvm") version "2.0.20"
    id("application")
}

group = "io.rbs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass = "io.rbs.pixies.game.GameKt"
}

dependencies {
    implementation(project(":engine"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}