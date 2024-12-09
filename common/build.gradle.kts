plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")

    id("eclipse")
    id("idea")
    id("maven-publish")
}

architectury {
    common("fabric", "forge")
    platformSetupLoomIde()
}

base {
    archivesName = "cobble_contests"
}

dependencies {

    minecraft("com.mojang:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())
    modCompileOnly("com.cobblemon:mod:1.5.2+1.20.1") {
        isTransitive = false
    }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}