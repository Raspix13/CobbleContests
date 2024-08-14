plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id ("maven-publish")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    enableTransitiveAccessWideners.set(true)
    silentMojangMappingsLicense()

    mixin {
        defaultRefmapName.set("mixins.${project.name}.refmap.json")
    }
}

fabricApi {

}

dependencies {
    minecraft("net.minecraft:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.15.10")

    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:0.89.3+1.20.1")
    modImplementation(fabricApi.module("fabric-command-api-v2", "0.89.3+1.20.1"))
    implementation(project(":common", configuration = "namedElements"))
    "developmentFabric"(project(":common", configuration = "namedElements"))

    modImplementation("com.cobblemon:fabric:1.5.2+1.20.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}