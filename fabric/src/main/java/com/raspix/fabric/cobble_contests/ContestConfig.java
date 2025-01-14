package com.raspix.fabric.cobble_contests;

//import net.minecraftforge.common.ForgeConfigSpec;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.event.config.ModConfigEvent;
//import net.minecraftforge.registries.ForgeRegistries;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
//@EventBusSubscriber(modid = CobbleContestsForge.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ContestConfig {

    private static File configFile = new File("config/contest_config.json");
    public static boolean ballSwapperReusable = true;
    public static int LIKED_POFFIN_FRIEND_INCREMENT;
    public static int NEUTRAL_POFFIN_FRIEND_INCREMENT;
    public static int DISLIKED_POFFIN_FRIEND_DECREMENT;
    public static int FOUL_POFFIN_FRIEND_DECREMENT;

    public static void loadConfig() {
        try {
            if (!configFile.exists()) { //remove when changes are made
                createDefaultConfig();
            }
            JsonObject config = JsonParser.parseReader(new FileReader(configFile)).getAsJsonObject();
            ballSwapperReusable = config.get("ball_swapper_ball_reusable").getAsBoolean();
            LIKED_POFFIN_FRIEND_INCREMENT = config.get("liked_poffin_friend_increment").getAsInt();
            NEUTRAL_POFFIN_FRIEND_INCREMENT = config.get("neutral_poffin_friend_increment").getAsInt();
            DISLIKED_POFFIN_FRIEND_DECREMENT = config.get("disliked_poffin_friend_decrement").getAsInt();
            FOUL_POFFIN_FRIEND_DECREMENT = config.get("foul_poffin_friend_decrement").getAsInt();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createDefaultConfig() {
        JsonObject config = new JsonObject();
        config.addProperty("ball_swapper_ball_reusable", false);
        config.addProperty("liked_poffin_friend_increment", 5);
        config.addProperty("neutral_poffin_friend_increment", 1);
        config.addProperty("disliked_poffin_friend_decrement", 5);
        config.addProperty("foul_poffin_friend_decrement", 20);

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(config.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
