import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    //id("com.github.gradle-lean") version "0.1.2"
}

group = "land.melon.lab"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    /*
     As Spigot-API depends on the Bungeecord ChatComponent-API,
    we need to add the Sonatype OSS repository, as Gradle,
    in comparison to maven, doesn't want to understand the ~/.m2
    directory unless added using mavenLocal(). Maven usually just gets
    it from there, as most people have run the BuildTools at least once.
    This is therefore not needed if you're using the full Spigot/CraftBukkit,
    or if you're using the Bukkit API.
    */
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    // mavenLocal() // This is needed for CraftBukkit and Spigot.
    maven("https://repo.dmulloy2.net/repository/public")
}

dependencies {
    // Pick only one of these and read the comment in the repositories block.
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    api("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo.
    runtimeOnly("com.google.code.gson:gson:2.8.8")
    runtimeOnly("com.comphenix.protocol:ProtocolLib:4.7.0")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<ShadowJar>{
    archiveFileName.set("$name-${archiveVersion.get()}-shaded.jar")

}



