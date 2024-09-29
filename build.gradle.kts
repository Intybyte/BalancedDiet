plugins {
    kotlin("jvm") version "2.1.0-Beta1"
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "me.vaan"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
    maven { url = uri("https://repo.aikar.co/content/groups/aikar/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.20")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.0.20")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    dependencies {
        include(dependency("co.aikar:acf-paper:0.5.1-SNAPSHOT"))
    }

    relocate("co.aikar.commands", "me.vaan.acf")
    relocate("co.aikar.locales", "me.vaan.locales")
}
