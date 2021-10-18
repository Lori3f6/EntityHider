import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("src/main/kotlin")
    }
}


group = "land.melon.lab"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public")

    // mavenLocal() // This is needed for CraftBukkit and Spigot.
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    api("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    api("com.comphenix.protocol:ProtocolLib:4.7.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<ShadowJar>{
    archiveFileName.set("$name-${archiveVersion.get()}-shaded.jar")
    minimize()

    dependencies {
        exclude(dependency("org.spigotmc:spigot-api:1.16-R0.1-SNAPSHOT") )
        exclude(dependency("com.comphenix.protocol:ProtocolLib:4.7.0"))
    }
}



