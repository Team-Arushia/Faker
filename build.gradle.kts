plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-rc2"
}

group = "io.github.bindglam"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    implementation("com.github.retrooper:packetevents-spigot:2.9.4")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveFileName = "Faker-$version.jar"

        relocate("com.github.retrooper.packetevents", "com.bindglam.faker.packetevents.api")
        relocate("io.github.retrooper.packetevents", "com.bindglam.faker.packetevents.impl")
    }
}