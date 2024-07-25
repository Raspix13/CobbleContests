rootProject.name = "Multi-Platform"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        gradlePluginPortal()
        gradlePluginPortal()
    }
}

listOf(
    "common",
    "forge",
    "fabric"
).forEach { include(it)}
