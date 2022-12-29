import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("src/main/kotlin")
    }
}


group = "land.melon.lab"
version = "0.1"
val spigotAPIVersion = "1.18.2-R0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public")

    // mavenLocal() // This is needed for CraftBukkit and Spigot.
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.21")
    api("org.spigotmc:spigot-api:${spigotAPIVersion}")
    api("com.comphenix.protocol:ProtocolLib:4.8.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}



