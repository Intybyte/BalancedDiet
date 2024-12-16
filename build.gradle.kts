import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.*

plugins {
    kotlin("jvm") version "2.1.0-Beta1"
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "me.vaan"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
    maven { url = uri("https://repo.aikar.co/content/groups/aikar/") }
    maven("https://repo.xenondevs.xyz/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    library(kotlin("stdlib"))
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.0.20")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("xyz.xenondevs.invui:invui-core:1.43")
    implementation("xyz.xenondevs.invui:inventory-access:1.43")
    implementation("xyz.xenondevs.invui:inventory-access-r19:1.43")
    implementation("xyz.xenondevs.invui:inventory-access-r20:1.43")
    implementation("xyz.xenondevs.invui:inventory-access-r21:1.43")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.43")
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

bukkit {
    name = "BalancedDiet"
    main = "me.vaan.balanceddiet.BalancedDiet"
    apiVersion = "1.20"
    version = project.version.toString()
    author = "Vaan1310"
    permissions {
        register("balanceddiet.command.*") {
            children = listOf(
                "balanceddiet.command.profile",
                "balanceddiet.command.effects",
                "balanceddiet.command.save",
                "balanceddiet.command.profile.other",
                "balanceddiet.command.clear"
            )
            default = OP
        }

        register("balanceddiet.command.profile") {
            default = TRUE
        }

        register("balanceddiet.command.effects") {
            default = TRUE
        }

        register("balanceddiet.command.save") {
            description = "Saves to the database async"
            default = FALSE
        }

        register("balanceddiet.command.profile.other") {
            description = "Gives info about another player diet"
            default = FALSE
        }

        register("balanceddiet.command.clear") {
            description = "Clears your or another player diet"
            default = FALSE
        }
    }
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

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

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

tasks.shadowJar {
    minimize {
        exclude(dependency("co.aikar:acf-paper:0.5.1-SNAPSHOT"))
        exclude(dependency("xyz.xenondevs.invui:invui-core:1.43"))
        exclude(dependency("xyz.xenondevs.invui:invui-kotlin:1.43"))
        exclude(dependency("xyz.xenondevs.invui:inventory-access:1.43"))
        exclude(dependency("xyz.xenondevs.invui:inventory-access-r19:1.43"))
        exclude(dependency("xyz.xenondevs.invui:inventory-access-r20:1.43"))
        exclude(dependency("xyz.xenondevs.invui:inventory-access-r21:1.43"))
    }

    dependencies {
        include(dependency("co.aikar:acf-paper:0.5.1-SNAPSHOT"))
        include(dependency("xyz.xenondevs.invui:invui-core:1.43"))
        include(dependency("xyz.xenondevs.invui:invui-kotlin:1.43"))
        include(dependency("xyz.xenondevs.invui:inventory-access:1.43"))
        include(dependency("xyz.xenondevs.invui:inventory-access-r19:1.43"))
        include(dependency("xyz.xenondevs.invui:inventory-access-r20:1.43"))
        include(dependency("xyz.xenondevs.invui:inventory-access-r21:1.43"))
    }

    relocate("xyz.xenondevs.inventoryaccess", "me.vaan.balanceddiet.deps.inventoryaccess")
    relocate("xyz.xenondevs.invui", "me.vaan.balanceddiet.deps.invui")
    relocate("co.aikar.commands", "me.vaan.balanceddiet.deps.acf")
    relocate("co.aikar.locales", "me.vaan.balanceddiet.deps.locales")
}
