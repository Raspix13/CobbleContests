plugins {
    id("java")
    id("java-library")
    kotlin("jvm") version("1.7.10")

    id("dev.architectury.loom") version("1.2-SNAPSHOT") apply false
    id("architectury-plugin") version("3.4-SNAPSHOT") apply false
}

group = "com.raspix.cobble_contests"
version = "1.0.1-SNAPSHOT"

base {
    archivesName = "cobble_contests"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven(url = "https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        maven("https://maven.impactdev.net/repository/development/")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}

