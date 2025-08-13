package com.raspix.fabric.cobble_contests.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raspix.fabric.cobble_contests.util.Contest;
import com.raspix.fabric.cobble_contests.util.data.ContestType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContestMoves {

    public static ContestMoves instance = new ContestMoves();

    public Map<String, MoveDataOld> contestMoves;

    public MoveData defaultMoveData = new MoveData("default", ContestType.None, "q");

    public Map<String, MoveData> allMoves = new HashMap<>() {{

        // region Beauty
        put("acidspray", new MoveData("acidspray", ContestType.Beauty,  "badly_startle_dup_type"));
        put("aquaring", new MoveData("aquaring", ContestType.Beauty,  "pump_up_self"));
        put("aquatail", new MoveData("aquatail", ContestType.Beauty,  "quite_appealing"));
        put("aromaticmist", new MoveData("aromaticmist", ContestType.Beauty,  "pump_up_self"));
        put("aurasphere", new MoveData("aurasphere", ContestType.Beauty,  "first_boost"));
        put("aurorabeam", new MoveData("aurorabeam", ContestType.Beauty,  "badly_startle_prev"));
        put("autotomize", new MoveData("autotomize", ContestType.Beauty,  "pump_up_self"));
        put("avalanche", new MoveData("avalanche", ContestType.Beauty,  "copy_prev"));
        put("blastburn", new MoveData("blastburn", ContestType.Beauty,  "startle_all"));
        put("blizzard", new MoveData("blizzard", ContestType.Beauty,  "badly_startle_prev"));
        put("blueflare", new MoveData("blueflare", ContestType.Beauty,  "reusable_appeal"));
        put("boltstrike", new MoveData("boltstrike", ContestType.Beauty,  "reusable_appeal"));
        put("bubblebeam", new MoveData("bubblebeam", ContestType.Beauty,  "badly_startle_prev"));
        put("bugbuzz", new MoveData("bugbuzz", ContestType.Beauty,  "badly_startle_prev"));
        put("chargebeam", new MoveData("chargebeam", ContestType.Beauty,  "pumped_boost"));
        put("clearsmog", new MoveData("clearsmog", ContestType.Beauty,  "first_boost"));
        put("conversion", new MoveData("conversion", ContestType.Beauty,  "dup_type_boost"));
        //put("Conversion 2", new MoveData("Conversion 2", ContestType.Beauty,  "dup_type_boost"));
        put("cosmicpower", new MoveData("cosmicpower", ContestType.Beauty,  "pump_up_self"));
        put("cottonspore", new MoveData("cottonspore", ContestType.Beauty,  "startle_high_expectations"));
        put("dazzlinggleam", new MoveData("dazzlinggleam", ContestType.Beauty,  "quite_appealing"));
        put("diamondstorm", new MoveData("diamondstorm", ContestType.Beauty,  "last_excitement"));
        put("discharge", new MoveData("discharge", ContestType.Beauty,  "badly_startle_prev"));
        put("dive", new MoveData("dive", ContestType.Beauty,  "startle_block"));
        put("doomdesire", new MoveData("doomdesire", ContestType.Beauty,  "last_excitement"));
        put("dracometeor", new MoveData("dracometeor", ContestType.Beauty,  "appeal_risk_startle"));
        put("dragonascent", new MoveData("dragonascent", ContestType.Beauty,  "appeal_risk_startle"));
        put("dragonpulse", new MoveData("dragonpulse", ContestType.Beauty,  "quite_appealing"));
        put("earthpower", new MoveData("earthpower", ContestType.Beauty,  "quite_appealing"));
        put("echoedvoice", new MoveData("echoedvoice", ContestType.Beauty,  "reusable_appeal"));
        put("electroweb", new MoveData("electroweb", ContestType.Beauty,  "startle_high_expectations"));
        put("energyball", new MoveData("energyball", ContestType.Beauty,  "quite_appealing"));
        put("eruption", new MoveData("eruption", ContestType.Beauty,  "appeal_risk_startle"));
        put("explosion", new MoveData("explosion", ContestType.Beauty,  "appeal_lose_moves"));
        put("fairywind", new MoveData("fairywind", ContestType.Beauty,  "quite_appealing"));
        put("featherdance", new MoveData("featherdance", ContestType.Beauty,  "last_boost"));
        put("fierydance", new MoveData("fierydance", ContestType.Beauty,  "pumped_boost"));
        put("fireblast", new MoveData("fireblast", ContestType.Beauty,  "excitement_boost"));
        put("firepledge", new MoveData("firepledge", ContestType.Beauty,  "any_excite"));
        put("firespin", new MoveData("firespin", ContestType.Beauty,  "block_excite"));
        put("flameburst", new MoveData("flameburst", ContestType.Beauty,  "discourage_audience"));
        put("flamewheel", new MoveData("flamewheel", ContestType.Beauty,  "copy_prev"));
        put("flamethrower", new MoveData("flamethrower", ContestType.Beauty,  "quite_appealing"));
        put("flash", new MoveData("flash", ContestType.Beauty,  "discourage_audience"));
        put("flashcannon", new MoveData("flashcannon", ContestType.Beauty,  "quite_appealing"));
        put("flowershield", new MoveData("flowershield", ContestType.Beauty,  "turn_startle_block"));
        put("freezeshock", new MoveData("freezeshock", ContestType.Beauty,  "copy_prev"));
        put("freezedry", new MoveData("freezedry", ContestType.Beauty,  "reusable_appeal"));
        put("frostbreath", new MoveData("frostbreath", ContestType.Beauty,  "expectant_audience"));
        put("fusionflare", new MoveData("fusionflare", ContestType.Beauty,  "late_boost"));
        put("geomancy", new MoveData("geomancy", ContestType.Beauty,  "pump_up_self"));
        put("glaciate", new MoveData("glaciate", ContestType.Beauty,  "block_excite"));
        put("grasspledge", new MoveData("grasspledge", ContestType.Beauty,  "reusable_appeal"));
        put("grassyterrain", new MoveData("grassyterrain", ContestType.Beauty,  "first_excitement"));
        put("growth", new MoveData("growth", ContestType.Beauty,  "pump_up_self"));
        put("hail", new MoveData("hail", ContestType.Beauty,  "startle_appeals"));
        put("haze", new MoveData("haze", ContestType.Beauty,  "discourage_audience"));
        put("healbell", new MoveData("healbell", ContestType.Beauty,  "turn_startle_block"));
        put("healpulse", new MoveData("healpulse", ContestType.Beauty,  "any_excite"));
        put("healingwish", new MoveData("healingwish", ContestType.Beauty,  "appeal_lose_moves"));
        put("heatwave", new MoveData("heatwave", ContestType.Beauty,  "startle_priors"));
        put("hydrocannon", new MoveData("hydrocannon", ContestType.Beauty,  "startle_all"));
        put("hydropump", new MoveData("hydropump", ContestType.Beauty,  "excitement_boost"));
        put("iceball", new MoveData("iceball", ContestType.Beauty,  "block_excite"));
        put("icebeam", new MoveData("icebeam", ContestType.Beauty,  "badly_startle_prev"));
        put("iceburn", new MoveData("iceburn", ContestType.Beauty,  "copy_prev"));
        put("icepunch", new MoveData("icepunch", ContestType.Beauty,  "quite_appealing"));
        put("iceshard", new MoveData("iceshard", ContestType.Beauty,  "move_earlier"));
        put("iciclecrash", new MoveData("iciclecrash", ContestType.Beauty,  "last_excitement"));
        put("iciclespear", new MoveData("iciclespear", ContestType.Beauty,  "placement_boost"));
        put("icywind", new MoveData("icywind", ContestType.Beauty,  "expectant_audience"));
        put("inferno", new MoveData("inferno", ContestType.Beauty,  "badly_startle_prev"));
        put("iondeluge", new MoveData("iondeluge", ContestType.Beauty,  "block_excite"));
        put("judgment", new MoveData("judgment", ContestType.Beauty,  "reusable_appeal"));
        put("landswrath", new MoveData("landswrath", ContestType.Beauty,  "startle_priors"));
        put("leafstorm", new MoveData("leafstorm", ContestType.Beauty,  "appeal_risk_startle"));
        put("lightscreen", new MoveData("lightscreen", ContestType.Beauty,  "startle_block"));
        put("lovelykiss", new MoveData("lovelykiss", ContestType.Beauty,  "expectant_audience"));
        put("lunardance", new MoveData("lunardance", ContestType.Beauty,  "appeal_lose_moves"));
        put("magiccoat", new MoveData("magiccoat", ContestType.Beauty,  "last_boost"));
        put("magicalleaf", new MoveData("magicalleaf", ContestType.Beauty,  "first_boost"));
        put("meanlook", new MoveData("meanlook", ContestType.Beauty,  "intimidate_posts"));
        put("meditate", new MoveData("meditate", ContestType.Beauty,  "pump_up_self"));
        put("mirrorcoat", new MoveData("mirrorcoat", ContestType.Beauty,  "last_boost"));
        put("mirrorshot", new MoveData("mirrorshot", ContestType.Beauty,  "startle_prev"));
        put("mist", new MoveData("mist", ContestType.Beauty,  "turn_startle_block"));
        put("mistyterrain", new MoveData("mistyterrain", ContestType.Beauty,  "first_excitement"));
        put("moonblast", new MoveData("moonblast", ContestType.Beauty,  "badly_startle_prev"));
        put("moonlight", new MoveData("moonlight", ContestType.Beauty,  "placement_boost"));
        put("morningsun", new MoveData("morningsun", ContestType.Beauty,  "placement_boost"));
        put("mysticalfire", new MoveData("mysticalfire", ContestType.Beauty,  "reusable_appeal"));
        put("naturepower", new MoveData("naturepower", ContestType.Beauty,  "excitement_boost"));
        put("ominouswind", new MoveData("ominouswind", ContestType.Beauty,  "pump_up_self"));
        put("originpulse", new MoveData("originpulse", ContestType.Beauty,  "first_excitement"));
        put("overheat", new MoveData("overheat", ContestType.Beauty,  "appeal_risk_startle"));
        put("perishsong", new MoveData("perishsong", ContestType.Beauty,  "expectant_audience"));
        put("petalblizzard", new MoveData("petalblizzard", ContestType.Beauty,  "startle_priors"));
        put("petaldance", new MoveData("petaldance", ContestType.Beauty,  "appeal_risk_startle"));
        put("powdersnow", new MoveData("powdersnow", ContestType.Beauty,  "quite_appealing"));
        put("powergem", new MoveData("powergem", ContestType.Beauty,  "quite_appealing"));
        put("psybeam", new MoveData("psybeam", ContestType.Beauty,  "discourage_audience"));
        put("psyshock", new MoveData("psyshock", ContestType.Beauty,  "badly_startle_prev"));
        put("quiverdance", new MoveData("quiverdance", ContestType.Beauty,  "pump_up_self"));
        put("raindance", new MoveData("raindance", ContestType.Beauty,  "excitement_boost"));
        put("relicsong", new MoveData("relicsong", ContestType.Beauty,  "intimidate_posts"));
        put("roaroftime", new MoveData("roaroftime", ContestType.Beauty,  "startle_all"));
        put("round", new MoveData("round", ContestType.Beauty,  "dup_type_boost"));
        put("sacredfire", new MoveData("sacredfire", ContestType.Beauty,  "last_excitement"));
        put("safeguard", new MoveData("safeguard", ContestType.Beauty,  "startle_block"));
        put("secretsword", new MoveData("secretsword", ContestType.Beauty,  "reusable_appeal"));
        put("seedflare", new MoveData("seedflare", ContestType.Beauty,  "appeal_risk_startle"));
        put("selfdestruct", new MoveData("selfdestruct", ContestType.Beauty,  "appeal_lose_moves"));
        put("sheercold", new MoveData("sheercold", ContestType.Beauty,  "startle_appeals"));
        put("signalbeam", new MoveData("signalbeam", ContestType.Beauty,  "discourage_audience"));
        put("silverwind", new MoveData("silverwind", ContestType.Beauty,  "pump_up_self"));
        put("spacialrend", new MoveData("spacialrend", ContestType.Beauty,  "appeal_risk_startle"));
        put("spore", new MoveData("spore", ContestType.Beauty,  "startle_priors"));
        put("sunnyday", new MoveData("sunnyday", ContestType.Beauty,  "excitement_boost"));
        put("surf", new MoveData("surf", ContestType.Beauty,  "startle_priors"));
        put("swordsdance", new MoveData("swordsdance", ContestType.Beauty,  "pump_up_self"));
        put("tailglow", new MoveData("tailglow", ContestType.Beauty,  "pump_up_self"));
        put("triattack", new MoveData("triattack", ContestType.Beauty,  "placement_boost"));
        put("venoshock", new MoveData("venoshock", ContestType.Beauty,  "dup_type_boost"));
        put("waterpledge", new MoveData("waterpledge", ContestType.Beauty,  "any_excite"));
        put("waterpulse", new MoveData("waterpulse", ContestType.Beauty,  "discourage_audience"));
        put("waterspout", new MoveData("waterspout", ContestType.Beauty,  "appeal_risk_startle"));
        put("weatherball", new MoveData("weatherball", ContestType.Beauty,  "reusable_appeal"));
        put("whirlpool", new MoveData("whirlpool", ContestType.Beauty,  "block_excite"));
        put("willowisp", new MoveData("willowisp", ContestType.Beauty,  "discourage_audience"));

        // endregion

        // region Smart/Clever

        put("absorb", new MoveData("absorb", ContestType.Smart,  "quite_appealing"));
        put("acid", new MoveData("acid", ContestType.Smart,  "startle_high_expectations"));
        put("allyswitch", new MoveData("allyswitch", ContestType.Smart,  "scramble"));
        put("aromatherapy", new MoveData("aromatherapy", ContestType.Smart,  "turn_startle_block"));
        put("assurance", new MoveData("assurance", ContestType.Smart,  "late_boost"));
        put("attackorder", new MoveData("attackorder", ContestType.Smart,  "reusable_appeal"));
        put("beatup", new MoveData("beatup", ContestType.Smart,  "pumped_boost"));
        put("calmmind", new MoveData("calmmind", ContestType.Smart,  "pump_up_self"));
        put("camouflage", new MoveData("camouflage", ContestType.Smart,  "average_priors"));
        put("charge", new MoveData("charge", ContestType.Smart,  "pump_up_self"));
        put("confuseray", new MoveData("confuseray", ContestType.Smart,  "startle_high_expectations"));
        put("confusion", new MoveData("confusion", ContestType.Smart,  "quite_appealing"));
        put("craftyshield", new MoveData("craftyshield", ContestType.Smart,  "first_excitement"));
        put("darkvoid", new MoveData("darkvoid", ContestType.Smart,  "intimidate_posts"));
        put("defendorder", new MoveData("defendorder", ContestType.Smart,  "startle_block"));
        put("destinybond", new MoveData("destinybond", ContestType.Smart,  "appeal_lose_moves"));
        put("disable", new MoveData("disable", ContestType.Smart,  "intimidate_posts"));
        put("dreameater", new MoveData("dreameater", ContestType.Smart,  "dup_type_boost"));
        put("eerieimpulse", new MoveData("eerieimpulse", ContestType.Smart,  "badly_startle_prev"));
        put("electricterrain", new MoveData("electricterrain", ContestType.Smart,  "first_excitement"));
        put("electrify", new MoveData("electrify", ContestType.Smart,  "badly_startle_dup_type"));
        put("embargo", new MoveData("embargo", ContestType.Smart,  "drain_priors"));
        put("fairylock", new MoveData("fairylock", ContestType.Smart,  "block_excite"));
        put("feint", new MoveData("feint", ContestType.Smart,  "move_earlier"));
        put("feintattack", new MoveData("feintattack", ContestType.Smart,  "first_boost"));
        put("flatter", new MoveData("flatter", ContestType.Smart,  "intimidate_posts"));
        put("fly", new MoveData("fly", ContestType.Smart,  "startle_block"));
        put("foresight", new MoveData("foresight", ContestType.Smart,  "badly_startle_dup_type"));
        put("forestscurse", new MoveData("forestscurse", ContestType.Smart,  "excitement_boost"));
        put("foulplay", new MoveData("foulplay", ContestType.Smart,  "appeal_prev"));
        put("futuresight", new MoveData("futuresight", ContestType.Smart,  "dup_type_boost"));
        put("geargrind", new MoveData("geargrind", ContestType.Smart,  "quite_appealing"));
        put("gigadrain", new MoveData("gigadrain", ContestType.Smart,  "badly_startle_prev"));
        put("grasswhistle", new MoveData("grasswhistle", ContestType.Smart,  "startle_block"));
        put("gravity", new MoveData("gravity", ContestType.Smart,  "intimidate_posts"));
        put("guardsplit", new MoveData("guardsplit", ContestType.Smart,  "average_priors"));
        put("guardswap", new MoveData("guardswap", ContestType.Smart,  "average_priors"));
        put("gust", new MoveData("gust", ContestType.Smart,  "badly_startle_prev"));
        put("healblock", new MoveData("healblock", ContestType.Smart,  "block_excite"));
        put("healorder", new MoveData("healorder", ContestType.Smart,  "badly_startle_dup_type"));
        put("heartswap", new MoveData("heartswap", ContestType.Smart,  "average_priors"));
        put("helpinghand", new MoveData("helpinghand", ContestType.Smart,  "quite_appealing"));
        put("hex", new MoveData("hex", ContestType.Smart,  "badly_startle_dup_type"));
        put("hiddenpower", new MoveData("hiddenpower", ContestType.Smart,  "reusable_appeal"));
        put("hyperspacehole", new MoveData("hyperspacehole", ContestType.Smart,  "first_excitement"));
        put("hypnosis", new MoveData("hypnosis", ContestType.Smart,  "startle_priors"));
        put("imprison", new MoveData("imprison", ContestType.Smart,  "block_excite"));
        put("ingrain", new MoveData("ingrain", ContestType.Smart,  "pump_up_self"));
        put("kinesis", new MoveData("kinesis", ContestType.Smart,  "reusable_appeal"));
        put("knockoff", new MoveData("knockoff", ContestType.Smart,  "badly_startle_priors"));
        put("leechlife", new MoveData("leechlife", ContestType.Smart,  "appeal_prev"));
        put("leechseed", new MoveData("leechseed", ContestType.Smart,  "pump_up_self"));
        put("lockon", new MoveData("lockon", ContestType.Smart,  "move_earlier"));
        put("lowsweep", new MoveData("lowsweep", ContestType.Smart,  "turn_startle_block"));
        put("lusterpurge", new MoveData("lusterpurge", ContestType.Smart,  "badly_startle_dup_type"));
        put("magicroom", new MoveData("magicroom", ContestType.Smart,  "block_excite"));
        put("magnetrise", new MoveData("magnetrise", ContestType.Smart,  "intimidate_posts"));
        put("magneticflux", new MoveData("magneticflux", ContestType.Smart,  "pump_up_self"));
        put("mefirst", new MoveData("mefirst", ContestType.Smart,  "move_earlier"));
        put("megadrain", new MoveData("megadrain", ContestType.Smart,  "startle_prev"));
        put("metalsound", new MoveData("metalsound", ContestType.Smart,  "startle_priors"));
        put("mindreader", new MoveData("mindreader", ContestType.Smart,  "move_earlier"));
        put("miracleeye", new MoveData("miracleeye", ContestType.Smart,  "first_boost"));
        put("mirrormove", new MoveData("mirrormove", ContestType.Smart,  "appeal_prev"));
        put("mistball", new MoveData("mistball", ContestType.Smart,  "startle_appeals"));
        put("nastyplot", new MoveData("nastyplot", ContestType.Smart,  "pump_up_self"));
        put("naturalgift", new MoveData("naturalgift", ContestType.Smart,  "excitement_boost"));
        put("needlearm", new MoveData("needlearm", ContestType.Smart,  "quite_appealing"));
        put("nightshade", new MoveData("nightshade", ContestType.Smart,  "reusable_appeal"));
        put("nightmare", new MoveData("nightmare", ContestType.Smart,  "startle_priors"));
        put("odorsleuth", new MoveData("odorsleuth", ContestType.Smart,  "startle_block"));
        put("painsplit", new MoveData("painsplit", ContestType.Smart,  "average_priors"));
        put("paraboliccharge", new MoveData("paraboliccharge", ContestType.Smart,  "average_priors"));
        put("payday", new MoveData("payday", ContestType.Smart,  "any_excite"));
        put("poisonfang", new MoveData("poisonfang", ContestType.Smart,  "quite_appealing"));
        put("poisongas", new MoveData("poisongas", ContestType.Smart,  "discourage_audience"));
        put("poisonpowder", new MoveData("poisonpowder", ContestType.Smart,  "drain_priors"));
        put("poisonsting", new MoveData("poisonsting", ContestType.Smart,  "startle_prev"));
        put("poisontail", new MoveData("poisontail", ContestType.Smart,  "drain_priors"));
        put("powder", new MoveData("powder", ContestType.Smart,  "expectant_audience"));
        put("powersplit", new MoveData("powersplit", ContestType.Smart,  "average_priors"));
        put("powerswap", new MoveData("powerswap", ContestType.Smart,  "average_priors"));
        put("powertrick", new MoveData("powertrick", ContestType.Smart,  "dup_type_boost"));
        put("psychup", new MoveData("psychup", ContestType.Smart,  "dup_type_boost"));
        put("psychic", new MoveData("psychic", ContestType.Smart,  "quite_appealing"));
        put("psychoboost", new MoveData("psychoboost", ContestType.Smart,  "appeal_risk_startle"));
        put("psychoshift", new MoveData("psychoshift", ContestType.Smart,  "last_boost"));
        put("psywave", new MoveData("psywave", ContestType.Smart,  "placement_boost"));
        put("pursuit", new MoveData("pursuit", ContestType.Smart,  "badly_startle_dup_type"));
        put("quash", new MoveData("quash", ContestType.Smart,  "move_earlier"));
        put("ragepowder", new MoveData("ragepowder", ContestType.Smart,  "block_excite"));
        put("recover", new MoveData("recover", ContestType.Smart,  "dup_type_boost"));
        put("recycle", new MoveData("recycle", ContestType.Smart,  "appeal_prev"));
        put("reflect", new MoveData("reflect", ContestType.Smart,  "startle_block"));
        put("reflecttype", new MoveData("reflecttype", ContestType.Smart,  "dup_type_boost"));
        put("rocktomb", new MoveData("rocktomb", ContestType.Smart,  "discourage_audience"));
        put("roost", new MoveData("roost", ContestType.Smart,  "expectant_audience"));
        put("sandtomb", new MoveData("sandtomb", ContestType.Smart,  "block_excite"));
        put("screech", new MoveData("screech", ContestType.Smart,  "discourage_audience"));
        put("secretpower", new MoveData("secretpower", ContestType.Smart,  "pumped_boost"));
        put("shadowball", new MoveData("shadowball", ContestType.Smart,  "quite_appealing"));
        put("shadowpunch", new MoveData("shadowpunch", ContestType.Smart,  "first_boost"));
        put("shadowsneak", new MoveData("shadowsneak", ContestType.Smart,  "move_earlier"));
        put("shiftgear", new MoveData("shiftgear", ContestType.Smart,  "pump_up_self"));
        put("sketch", new MoveData("sketch", ContestType.Smart,  "appeal_prev"));
        put("skillswap", new MoveData("skillswap", ContestType.Smart,  "appeal_prev"));
        put("sleeppowder", new MoveData("sleeppowder", ContestType.Smart,  "startle_priors"));
        put("smokescreen", new MoveData("smokescreen", ContestType.Smart,  "startle_block"));
        put("snatch", new MoveData("snatch", ContestType.Smart,  "appeal_prev"));
        put("spiderweb", new MoveData("spiderweb", ContestType.Smart,  "intimidate_posts"));
        put("spikes", new MoveData("spikes", ContestType.Smart,  "intimidate_posts"));
        put("storedpower", new MoveData("storedpower", ContestType.Smart,  "pumped_boost"));
        put("stringshot", new MoveData("stringshot", ContestType.Smart,  "startle_prev"));
        put("stunspore", new MoveData("stunspore", ContestType.Smart,  "startle_appeals"));
        put("suckerpunch", new MoveData("suckerpunch", ContestType.Smart,  "first_excitement"));
        put("supersonic", new MoveData("supersonic", ContestType.Smart,  "discourage_audience"));
        put("switcheroo", new MoveData("switcheroo", ContestType.Smart,  "badly_startle_dup_type"));
        put("synchronoise", new MoveData("synchronoise", ContestType.Smart,  "dup_type_boost"));
        put("synthesis", new MoveData("synthesis", ContestType.Smart,  "placement_boost"));
        put("taunt", new MoveData("taunt", ContestType.Smart,  "startle_high_expectations"));
        put("telekinesis", new MoveData("telekinesis", ContestType.Smart,  "intimidate_posts"));
        put("topsyturvy", new MoveData("topsyturvy", ContestType.Smart,  "scramble"));
        put("toxic", new MoveData("toxic", ContestType.Smart,  "drain_priors"));
        put("toxicspikes", new MoveData("toxicspikes", ContestType.Smart,  "intimidate_posts"));
        put("transform", new MoveData("transform", ContestType.Smart,  "reusable_appeal"));
        put("trick", new MoveData("trick", ContestType.Smart,  "badly_startle_dup_type"));
        put("trickroom", new MoveData("trickroom", ContestType.Smart,  "scramble"));
        put("venomdrench", new MoveData("venomdrench", ContestType.Smart,  "drain_priors"));
        put("whirlwind", new MoveData("whirlwind", ContestType.Smart,  "move_later"));
        put("wonderroom", new MoveData("wonderroom", ContestType.Smart,  "scramble"));
        put("worryseed", new MoveData("worryseed", ContestType.Smart,  "intimidate_posts"));
        put("zenheadbutt", new MoveData("zenheadbutt", ContestType.Smart,  "quite_appealing"));


        // endregion

        // region Cool

        put("acrobatics", new MoveData("acrobatics", ContestType.Cool,  "pumped_boost"));
        put("aerialace", new MoveData("aerialace", ContestType.Cool,  "first_boost"));
        put("aeroblast", new MoveData("aeroblast", ContestType.Cool,  "last_excitement"));
        put("agility", new MoveData("agility", ContestType.Cool,  "move_earlier"));
        put("aircutter", new MoveData("aircutter", ContestType.Cool,  "quite_appealing"));
        put("airslash", new MoveData("airslash", ContestType.Cool,  "badly_startle_prev"));
        put("aquajet", new MoveData("aquajet", ContestType.Cool,  "move_earlier"));
        put("barrier", new MoveData("barrier", ContestType.Cool,  "turn_startle_block"));
        put("blazekick", new MoveData("blazekick", ContestType.Cool,  "reusable_appeal"));
        put("bravebird", new MoveData("bravebird", ContestType.Cool,  "appeal_risk_startle"));
        put("brickbreak", new MoveData("brickbreak", ContestType.Cool,  "quite_appealing"));
        put("bulkup", new MoveData("bulkup", ContestType.Cool,  "pump_up_self"));
        put("bulletseed", new MoveData("bulletseed", ContestType.Cool,  "placement_boost"));
        put("circlethrow", new MoveData("circlethrow", ContestType.Cool,  "move_later"));
        put("crosschop", new MoveData("crosschop", ContestType.Cool,  "copy_prev"));
        put("crosspoison", new MoveData("crosspoison", ContestType.Cool,  "badly_startle_dup_type"));
        put("crushclaw", new MoveData("crushclaw", ContestType.Cool,  "badly_startle_prev"));
        put("cut", new MoveData("cut", ContestType.Cool,  "quite_appealing"));
        put("darkpulse", new MoveData("darkpulse", ContestType.Cool,  "quite_appealing"));
        put("defog", new MoveData("defog", ContestType.Cool,  "first_boost"));
        put("detect", new MoveData("detect", ContestType.Cool,  "turn_startle_block"));
        put("doublehit", new MoveData("doublehit", ContestType.Cool,  "badly_startle_dup_type"));
        put("doublekick", new MoveData("doublekick", ContestType.Cool,  "badly_startle_dup_type"));
        put("doubleteam", new MoveData("doubleteam", ContestType.Cool,  "pump_up_self"));
        put("dragonbreath", new MoveData("dragonbreath", ContestType.Cool,  "badly_startle_prev"));
        put("dragonclaw", new MoveData("dragonclaw", ContestType.Cool,  "quite_appealing"));
        put("dragondance", new MoveData("dragondance", ContestType.Cool,  "pump_up_self"));
        put("dragonrage", new MoveData("dragonrage", ContestType.Cool,  "reusable_appeal"));
        put("drillpeck", new MoveData("drillpeck", ContestType.Cool,  "appeal_prev"));
        put("dynamicpunch", new MoveData("dynamicpunch", ContestType.Cool,  "startle_high_expectations"));
        put("electroball", new MoveData("electroball", ContestType.Cool,  "first_excitement"));
        put("extrasensory", new MoveData("extrasensory", ContestType.Cool,  "badly_startle_dup_type"));
        put("extremespeed", new MoveData("extremespeed", ContestType.Cool,  "move_earlier"));
        put("falseswipe", new MoveData("falseswipe", ContestType.Cool,  "expectant_audience"));
        put("fellstinger", new MoveData("fellstinger", ContestType.Cool,  "excitement_boost"));
        put("firefang", new MoveData("firefang", ContestType.Cool,  "quite_appealing"));
        put("flamecharge", new MoveData("flamecharge", ContestType.Cool,  "pumped_boost"));
        put("flareblitz", new MoveData("flareblitz", ContestType.Cool,  "appeal_risk_startle"));
        put("focusblast", new MoveData("focusblast", ContestType.Cool,  "quite_appealing"));
        put("focusenergy", new MoveData("focusenergy", ContestType.Cool,  "pump_up_self"));
        put("forcepalm", new MoveData("forcepalm", ContestType.Cool,  "quite_appealing"));
        put("frenzyplant", new MoveData("frenzyplant", ContestType.Cool,  "startle_all"));
        put("furyattack", new MoveData("furyattack", ContestType.Cool,  "placement_boost"));
        put("furycutter", new MoveData("furycutter", ContestType.Cool,  "reusable_appeal"));
        put("fusionbolt", new MoveData("fusionbolt", ContestType.Cool,  "late_boost"));
        put("guillotine", new MoveData("guillotine", ContestType.Cool,  "startle_appeals"));
        put("gyroball", new MoveData("gyroball", ContestType.Cool,  "late_boost"));
        put("highjumpkick", new MoveData("highjumpkick", ContestType.Cool,  "appeal_risk_startle"));
        put("holdback", new MoveData("holdback", ContestType.Cool,  "expectant_audience"));
        put("hornattack", new MoveData("hornattack", ContestType.Cool,  "quite_appealing"));
        put("horndrill", new MoveData("horndrill", ContestType.Cool,  "startle_appeals"));
        put("howl", new MoveData("howl", ContestType.Cool,  "last_boost"));
        put("hyperbeam", new MoveData("hyperbeam", ContestType.Cool,  "startle_all"));
        put("hyperfang", new MoveData("hyperfang", ContestType.Cool,  "reusable_appeal"));
        put("hypervoice", new MoveData("hypervoice", ContestType.Cool,  "startle_priors"));
        put("icefang", new MoveData("icefang", ContestType.Cool,  "quite_appealing"));
        put("irontail", new MoveData("irontail", ContestType.Cool,  "quite_appealing"));
        put("jumpkick", new MoveData("jumpkick", ContestType.Cool,  "appeal_risk_startle"));
        put("kingsshield", new MoveData("kingsshield", ContestType.Cool,  "turn_startle_block"));
        put("leafblade", new MoveData("leafblade", ContestType.Cool,  "reusable_appeal"));
        put("leaftornado", new MoveData("leaftornado", ContestType.Cool,  "reusable_appeal"));
        put("leer", new MoveData("leer", ContestType.Cool,  "startle_high_expectations"));
        put("machpunch", new MoveData("machpunch", ContestType.Cool,  "move_earlier"));
        put("magnetbomb", new MoveData("magnetbomb", ContestType.Cool,  "first_boost"));
        put("matblock", new MoveData("matblock", ContestType.Cool,  "startle_priors"));
        put("megakick", new MoveData("megakick", ContestType.Cool,  "excitement_boost"));
        put("megahorn", new MoveData("megahorn", ContestType.Cool,  "reusable_appeal"));
        put("metalburst", new MoveData("metalburst", ContestType.Cool,  "last_boost"));
        put("metalclaw", new MoveData("metalclaw", ContestType.Cool,  "quite_appealing"));
        put("meteormash", new MoveData("meteormash", ContestType.Cool,  "quite_appealing"));
        put("nightdaze", new MoveData("nightdaze", ContestType.Cool,  "reusable_appeal"));
        put("nightslash", new MoveData("nightslash", ContestType.Cool,  "copy_prev"));
        put("oblivionwing", new MoveData("oblivionwing", ContestType.Cool,  "average_priors"));
        put("outrage", new MoveData("outrage", ContestType.Cool,  "appeal_risk_startle"));
        put("partingshot", new MoveData("partingshot", ContestType.Cool,  "last_excitement"));
        put("peck", new MoveData("peck", ContestType.Cool,  "quite_appealing"));
        put("phantomforce", new MoveData("phantomforce", ContestType.Cool,  "turn_startle_block"));
        put("pinmissile", new MoveData("pinmissile", ContestType.Cool,  "placement_boost"));
        put("precipiceblades", new MoveData("precipiceblades", ContestType.Cool,  "last_excitement"));
        put("psychocut", new MoveData("psychocut", ContestType.Cool,  "quite_appealing"));
        put("psystrike", new MoveData("psystrike", ContestType.Cool,  "reusable_appeal"));
        put("punishment", new MoveData("punishment", ContestType.Cool,  "startle_high_expectations"));
        put("quickattack", new MoveData("quickattack", ContestType.Cool,  "move_earlier"));
        put("quickguard", new MoveData("quickguard", ContestType.Cool,  "first_boost"));
        put("rapidspin", new MoveData("rapidspin", ContestType.Cool,  "excitement_boost"));
        put("razorleaf", new MoveData("razorleaf", ContestType.Cool,  "quite_appealing"));
        put("razorshell", new MoveData("razorshell", ContestType.Cool,  "reusable_appeal"));
        put("razorwind", new MoveData("razorwind", ContestType.Cool,  "copy_prev"));
        put("retaliate", new MoveData("retaliate", ContestType.Cool,  "copy_prev"));
        put("reversal", new MoveData("reversal", ContestType.Cool,  "late_boost"));
        put("roar", new MoveData("roar", ContestType.Cool,  "move_later"));
        put("rollingkick", new MoveData("rollingkick", ContestType.Cool,  "quite_appealing"));
        put("sacredsword", new MoveData("sacredsword", ContestType.Cool,  "any_excite"));
        put("searingshot", new MoveData("searingshot", ContestType.Cool,  "reusable_appeal"));
        put("shadowclaw", new MoveData("shadowclaw", ContestType.Cool,  "quite_appealing"));
        put("shadowforce", new MoveData("shadowforce", ContestType.Cool,  "turn_startle_block"));
        put("shockwave", new MoveData("shockwave", ContestType.Cool,  "first_boost"));
        put("skyattack", new MoveData("skyattack", ContestType.Cool,  "copy_prev"));
        put("skyuppercut", new MoveData("skyuppercut", ContestType.Cool,  "badly_startle_dup_type"));
        put("slash", new MoveData("slash", ContestType.Cool,  "quite_appealing"));
        put("solarbeam", new MoveData("solarbeam", ContestType.Cool,  "copy_prev"));
        put("sonicboom", new MoveData("sonicboom", ContestType.Cool,  "reusable_appeal"));
        put("spark", new MoveData("spark", ContestType.Cool,  "quite_appealing"));
        put("spikecannon", new MoveData("spikecannon", ContestType.Cool,  "placement_boost"));
        put("stealthrock", new MoveData("stealthrock", ContestType.Cool,  "intimidate_posts"));
        put("steelwing", new MoveData("steelwing", ContestType.Cool,  "copy_prev"));
        put("stormthrow", new MoveData("stormthrow", ContestType.Cool,  "any_excite"));
        put("submission", new MoveData("submission", ContestType.Cool,  "appeal_risk_startle"));
        put("swift", new MoveData("swift", ContestType.Cool,  "first_boost"));
        put("tailwind", new MoveData("tailwind", ContestType.Cool,  "move_earlier"));
        put("technoblast", new MoveData("technoblast", ContestType.Cool,  "pumped_boost"));
        put("teleport", new MoveData("teleport", ContestType.Cool,  "turn_startle_block"));
        put("thunder", new MoveData("thunder", ContestType.Cool,  "excitement_boost"));
        put("thunderfang", new MoveData("thunderfang", ContestType.Cool,  "quite_appealing"));
        put("thunderpunch", new MoveData("thunderpunch", ContestType.Cool,  "quite_appealing"));
        put("thundershock", new MoveData("thundershock", ContestType.Cool,  "quite_appealing"));
        put("thunderwave", new MoveData("thunderwave", ContestType.Cool,  "startle_priors"));
        put("thunderbolt", new MoveData("thunderbolt", ContestType.Cool,  "quite_appealing"));
        put("triplekick", new MoveData("triplekick", ContestType.Cool,  "reusable_appeal"));
        put("trumpcard", new MoveData("trumpcard", ContestType.Cool,  "late_boost"));
        put("twineedle", new MoveData("twineedle", ContestType.Cool,  "badly_startle_dup_type"));
        put("twister", new MoveData("twister", ContestType.Cool,  "quite_appealing"));
        put("vcreate", new MoveData("vcreate", ContestType.Cool,  "appeal_risk_startle"));
        put("vacuumwave", new MoveData("vacuumwave", ContestType.Cool,  "move_earlier"));
        put("vinewhip", new MoveData("vinewhip", ContestType.Cool,  "quite_appealing"));
        put("vitalthrow", new MoveData("vitalthrow", ContestType.Cool,  "last_boost"));
        put("voltswitch", new MoveData("voltswitch", ContestType.Cool,  "expectant_audience"));
        put("voltackle", new MoveData("volttackle", ContestType.Cool,  "appeal_risk_startle"));
        put("watershuriken", new MoveData("watershuriken", ContestType.Cool,  "move_earlier"));
        put("wingattack", new MoveData("wingattack", ContestType.Cool,  "quite_appealing"));
        put("xscissor", new MoveData("xscissor", ContestType.Cool,  "badly_startle_dup_type"));
        put("zapcannon", new MoveData("zapcannon", ContestType.Cool,  "startle_appeals"));


        // endregion

        // region Cute

        put("afteryou", new MoveData("afteryou", ContestType.Cute,  "move_later"));
        put("amnesia", new MoveData("amnesia", ContestType.Cute,  "turn_startle_block"));
        put("assist", new MoveData("assist", ContestType.Cute,  "placement_boost"));
        put("astonish", new MoveData("astonish", ContestType.Cute,  "badly_startle_prev"));
        put("attract", new MoveData("attract", ContestType.Cute,  "intimidate_posts"));
        put("babydolleyes", new MoveData("babydolleyes", ContestType.Cute,  "move_earlier"));
        put("barrage", new MoveData("barrage", ContestType.Cute,  "placement_boost"));
        put("batonpass", new MoveData("batonpass", ContestType.Cute,  "pumped_boost"));
        put("bellydrum", new MoveData("bellydrum", ContestType.Cute,  "appeal_risk_startle"));
        put("bestow", new MoveData("bestow", ContestType.Cute,  "excitement_boost"));
        put("block", new MoveData("block", ContestType.Cute,  "intimidate_posts"));
        put("bounce", new MoveData("bounce", ContestType.Cute,  "turn_startle_block"));
        put("bubble", new MoveData("bubble", ContestType.Cute,  "quite_appealing"));
        put("bugbite", new MoveData("bugbite", ContestType.Cute,  "discourage_audience"));
        put("captivate", new MoveData("captivate", ContestType.Cute,  "expectant_audience"));
        put("celebrate", new MoveData("celebrate", ContestType.Cute,  "any_excite"));
        put("charm", new MoveData("charm", ContestType.Cute,  "startle_high_expectations"));
        put("chatter", new MoveData("chatter", ContestType.Cute,  "discourage_audience"));
        put("confide", new MoveData("confide", ContestType.Cute,  "drain_priors"));
        put("copycat", new MoveData("copycat", ContestType.Cute,  "copy_prev"));
        put("cottonguard", new MoveData("cottonguard", ContestType.Cute,  "turn_startle_block"));
        put("covet", new MoveData("covet", ContestType.Cute,  "copy_prev"));
        put("defensecurl", new MoveData("defensecurl", ContestType.Cute,  "startle_block"));
        put("disarmingvoice", new MoveData("disarmingvoice", ContestType.Cute,  "first_boost"));
        put("dizzypunch", new MoveData("dizzypunch", ContestType.Cute,  "startle_high_expectations"));
        put("doubleslap", new MoveData("doubleslap", ContestType.Cute,  "placement_boost"));
        put("drainingkiss", new MoveData("drainingkiss", ContestType.Cute,  "average_priors"));
        put("eggbomb", new MoveData("eggbomb", ContestType.Cute,  "quite_appealing"));
        put("ember", new MoveData("ember", ContestType.Cute,  "quite_appealing"));
        put("encore", new MoveData("encore", ContestType.Cute,  "intimidate_posts"));
        put("entrainment", new MoveData("entrainment", ContestType.Cute,  "badly_startle_dup_type"));
        put("facade", new MoveData("facade", ContestType.Cute,  "last_boost"));
        put("fakeout", new MoveData("fakeout", ContestType.Cute,  "badly_startle_prev"));
        put("fake Tears", new MoveData("fake Tears", ContestType.Cute,  "expectant_audience"));
        put("flail", new MoveData("flail", ContestType.Cute,  "late_boost"));
        put("fling", new MoveData("fling", ContestType.Cute,  "drain_priors"));
        put("followme", new MoveData("followme", ContestType.Cute,  "block_excite"));
        put("frustration", new MoveData("frustration", ContestType.Cute,  "badly_startle_prev"));
        put("grassknot", new MoveData("grassknot", ContestType.Cute,  "late_boost"));
        put("growl", new MoveData("growl", ContestType.Cute,  "last_boost"));
        put("happyhour", new MoveData("happyhour", ContestType.Cute,  "any_excite"));
        put("heartstamp", new MoveData("heartstamp", ContestType.Cute,  "reusable_appeal"));
        put("holdhands", new MoveData("holdhands", ContestType.Cute,  "turn_startle_block"));
        put("honeclaws", new MoveData("honeclaws", ContestType.Cute,  "pump_up_self"));
        put("infestation", new MoveData("infestation", ContestType.Cute,  "block_excite"));
        put("lastresort", new MoveData("lastresort", ContestType.Cute,  "pumped_boost"));
        put("lick", new MoveData("lick", ContestType.Cute,  "startle_prev"));
        put("luckychant", new MoveData("luckychant", ContestType.Cute,  "pump_up_self"));
        put("metronome", new MoveData("metronome", ContestType.Cute,  "placement_boost"));
        put("milkdrink", new MoveData("milkdrink", ContestType.Cute,  "first_boost"));
        put("mimic", new MoveData("mimic", ContestType.Cute,  "appeal_prev"));
        put("minimize", new MoveData("minimize", ContestType.Cute,  "turn_startle_block"));
        put("mudbomb", new MoveData("mudbomb", ContestType.Cute,  "startle_prev"));
        put("mudsport", new MoveData("mudsport", ContestType.Cute,  "any_excite"));
        put("mudslap", new MoveData("mudslap", ContestType.Cute,  "discourage_audience"));
        put("nuzzle", new MoveData("nuzzle", ContestType.Cute,  "badly_startle_dup_type"));
        put("playnice", new MoveData("playnice", ContestType.Cute,  "excitement_boost"));
        put("playrough", new MoveData("playrough", ContestType.Cute,  "drain_priors"));
        put("pluck", new MoveData("pluck", ContestType.Cute,  "drain_priors"));
        put("present", new MoveData("present", ContestType.Cute,  "reusable_appeal"));
        put("protect", new MoveData("protect", ContestType.Cute,  "startle_block"));
        put("refresh", new MoveData("refresh", ContestType.Cute,  "startle_block"));
        put("rest", new MoveData("rest", ContestType.Cute,  "turn_startle_block"));
        put("return", new MoveData("return", ContestType.Cute,  "quite_appealing"));
        put("roleplay", new MoveData("roleplay", ContestType.Cute,  "appeal_prev"));
        put("rollout", new MoveData("rollout", ContestType.Cute,  "reusable_appeal"));
        put("sandattack", new MoveData("sandattack", ContestType.Cute,  "discourage_audience"));
        put("sharpen", new MoveData("sharpen", ContestType.Cute,  "pump_up_self"));
        put("simplebeam", new MoveData("simplebeam", ContestType.Cute,  "drain_priors"));
        put("sing", new MoveData("sing", ContestType.Cute,  "intimidate_posts"));
        put("slackoff", new MoveData("slackoff", ContestType.Cute,  "expectant_audience"));
        put("sleeptalk", new MoveData("sleeptalk", ContestType.Cute,  "placement_boost"));
        put("snore", new MoveData("snore", ContestType.Cute,  "expectant_audience"));
        put("soak", new MoveData("soak", ContestType.Cute,  "startle_high_expectations"));
        put("softboiled", new MoveData("softboiled", ContestType.Cute,  "first_boost"));
        put("splash", new MoveData("splash", ContestType.Cute,  "expectant_audience"));
        put("strugglebug", new MoveData("strugglebug", ContestType.Cute,  "last_boost"));
        put("substitute", new MoveData("substitute", ContestType.Cute,  "startle_block"));
        put("swagger", new MoveData("swagger", ContestType.Cute,  "drain_priors"));
        put("sweetkiss", new MoveData("sweetkiss", ContestType.Cute,  "intimidate_posts"));
        put("sweetscent", new MoveData("sweetscent", ContestType.Cute,  "startle_block"));
        put("tailslap", new MoveData("tailslap", ContestType.Cute,  "placement_boost"));
        put("tailwhip", new MoveData("tailwhip", ContestType.Cute,  "last_boost"));
        put("teeterdance", new MoveData("teeterdance", ContestType.Cute,  "startle_all"));
        put("tickle", new MoveData("tickle", ContestType.Cute,  "drain_priors"));
        put("trickortreat", new MoveData("trickortreat", ContestType.Cute,  "badly_startle_dup_type"));
        put("uturn", new MoveData("uturn", ContestType.Cute,  "expectant_audience"));
        put("uproar", new MoveData("uproar", ContestType.Cute,  "startle_high_expectations"));
        put("watergun", new MoveData("watergun", ContestType.Cute,  "quite_appealing"));
        put("watersport", new MoveData("watersport", ContestType.Cute,  "any_excite"));
        put("wish", new MoveData("wish", ContestType.Cute,  "last_excitement"));
        put("withdraw", new MoveData("withdraw", ContestType.Cute,  "startle_block"));
        put("yawn", new MoveData("yawn", ContestType.Cute,  "intimidate_posts"));


        // endregion

        // region Tough

        put("acidarmor", new MoveData("acidarmor", ContestType.Tough,  "turn_startle_block"));
        put("acupressure", new MoveData("acupressure", ContestType.Tough,  "placement_boost"));
        put("ancientpower", new MoveData("ancientpower", ContestType.Tough,  "pump_up_self"));
        put("armthrust", new MoveData("armthrust", ContestType.Tough,  "placement_boost"));
        put("belch", new MoveData("belch", ContestType.Tough,  "pumped_boost"));
        put("bide", new MoveData("bide", ContestType.Tough,  "move_later"));
        put("bind", new MoveData("bind", ContestType.Tough,  "block_excite"));
        put("bite", new MoveData("bite", ContestType.Tough,  "badly_startle_prev"));
        put("bodyslam", new MoveData("bodyslam", ContestType.Tough,  "badly_startle_prev"));
        put("boneclub", new MoveData("boneclub", ContestType.Tough,  "reusable_appeal"));
        put("bonerush", new MoveData("bonerush", ContestType.Tough,  "placement_boost"));
        put("bonemerang", new MoveData("bonemerang", ContestType.Tough,  "badly_startle_dup_type"));
        put("boomburst", new MoveData("boomburst", ContestType.Tough,  "startle_all"));
        put("brine", new MoveData("brine", ContestType.Tough,  "copy_prev"));
        put("bulldoze", new MoveData("bulldoze", ContestType.Tough,  "badly_startle_prev"));
        put("bulletpunch", new MoveData("bulletpunch", ContestType.Tough,  "move_earlier"));
        put("chipaway", new MoveData("chipaway", ContestType.Tough,  "any_excite"));
        put("clamp", new MoveData("clamp", ContestType.Tough,  "block_excite"));
        put("closecombat", new MoveData("closecombat", ContestType.Tough,  "appeal_risk_startle"));
        put("coil", new MoveData("coil", ContestType.Tough,  "pump_up_self"));
        put("cometpunch", new MoveData("cometpunch", ContestType.Tough,  "placement_boost"));
        put("constrict", new MoveData("constrict", ContestType.Tough,  "discourage_audience"));
        put("counter", new MoveData("counter", ContestType.Tough,  "last_boost"));
        put("crabhammer", new MoveData("crabhammer", ContestType.Tough,  "reusable_appeal"));
        put("crunch", new MoveData("crunch", ContestType.Tough,  "badly_startle_prev"));
        put("crushgrip", new MoveData("crushgrip", ContestType.Tough,  "reusable_appeal"));
        put("curse", new MoveData("curse", ContestType.Tough,  "move_later"));
        put("dig", new MoveData("dig", ContestType.Tough,  "startle_block"));
        put("doubleedge", new MoveData("doubleedge", ContestType.Tough,  "appeal_risk_startle"));
        put("dragonrush", new MoveData("dragonrush", ContestType.Tough,  "copy_prev"));
        put("dragontail", new MoveData("dragontail", ContestType.Tough,  "move_later"));
        put("drainpunch", new MoveData("drainpunch", ContestType.Tough,  "appeal_prev"));
        put("drillrun", new MoveData("drillrun", ContestType.Tough,  "appeal_prev"));
        put("dualchop", new MoveData("dualchop", ContestType.Tough,  "badly_startle_dup_type"));
        put("earthquake", new MoveData("earthquake", ContestType.Tough,  "startle_appeals"));
        put("endeavor", new MoveData("endeavor", ContestType.Tough,  "last_boost"));
        put("endure", new MoveData("endure", ContestType.Tough,  "move_later"));
        put("finalgambit", new MoveData("finalgambit", ContestType.Tough,  "appeal_lose_moves"));
        put("firepunch", new MoveData("firepunch", ContestType.Tough,  "quite_appealing"));
        put("fissure", new MoveData("fissure", ContestType.Tough,  "startle_appeals"));
        put("flyingpress", new MoveData("flyingpress", ContestType.Tough,  "any_excite"));
        put("focuspunch", new MoveData("focuspunch", ContestType.Tough,  "last_boost"));
        put("furyswipes", new MoveData("furyswipes", ContestType.Tough,  "placement_boost"));
        put("gastroacid", new MoveData("gastroacid", ContestType.Tough,  "drain_priors"));
        put("gigaimpact", new MoveData("gigaimpact", ContestType.Tough,  "startle_all"));
        put("glare", new MoveData("glare", ContestType.Tough,  "badly_startle_prev"));
        put("grudge", new MoveData("grudge", ContestType.Tough,  "startle_all"));
        put("gunkshot", new MoveData("gunkshot", ContestType.Tough,  "startle_appeals"));
        put("hammerarm", new MoveData("hammerarm", ContestType.Tough,  "appeal_risk_startle"));
        put("harden", new MoveData("harden", ContestType.Tough,  "startle_block"));
        put("headcharge", new MoveData("headcharge", ContestType.Tough,  "appeal_risk_startle"));
        put("headsmash", new MoveData("headsmash", ContestType.Tough,  "appeal_risk_startle"));
        put("headbutt", new MoveData("headbutt", ContestType.Tough,  "quite_appealing"));
        put("heatcrash", new MoveData("heatcrash", ContestType.Tough,  "last_boost"));
        put("heavyslam", new MoveData("heavyslam", ContestType.Tough,  "last_boost"));
        put("hornleech", new MoveData("hornleech", ContestType.Tough,  "appeal_prev"));
        put("hurricane", new MoveData("hurricane", ContestType.Tough,  "startle_appeals"));
        put("hyperspacefury", new MoveData("hyperspacefury", ContestType.Tough,  "first_excitement"));
        put("incinerate", new MoveData("incinerate", ContestType.Tough,  "discourage_audience"));
        put("irondefense", new MoveData("irondefense", ContestType.Tough,  "turn_startle_block"));
        put("ironhead", new MoveData("ironhead", ContestType.Tough,  "quite_appealing"));
        put("karatechop", new MoveData("karatechop", ContestType.Tough,  "quite_appealing"));
        put("lavaplume", new MoveData("lavaplume", ContestType.Tough,  "startle_priors"));
        put("lowkick", new MoveData("lowkick", ContestType.Tough,  "late_boost"));
        put("magmastorm", new MoveData("magmastorm", ContestType.Tough,  "block_excite"));
        put("magnitude", new MoveData("magnitude", ContestType.Tough,  "excitement_boost"));
        put("megapunch", new MoveData("megapunch", ContestType.Tough,  "reusable_appeal"));
        put("memento", new MoveData("memento", ContestType.Tough,  "appeal_lose_moves"));
        put("mudshot", new MoveData("mudshot", ContestType.Tough,  "quite_appealing"));
        put("muddywater", new MoveData("muddywater", ContestType.Tough,  "startle_priors"));
        put("nobleroar", new MoveData("nobleroar", ContestType.Tough,  "first_boost"));
        put("octazooka", new MoveData("octazooka", ContestType.Tough,  "reusable_appeal"));
        put("payback", new MoveData("payback", ContestType.Tough,  "last_boost"));
        put("poisonjab", new MoveData("poisonjab", ContestType.Tough,  "quite_appealing"));
        put("pound", new MoveData("pound", ContestType.Tough,  "quite_appealing"));
        put("powerwhip", new MoveData("powerwhip", ContestType.Tough,  "excitement_boost"));
        put("poweruppunch", new MoveData("poweruppunch", ContestType.Tough,  "pumped_boost"));
        put("rage", new MoveData("rage", ContestType.Tough,  "startle_priors"));
        put("revenge", new MoveData("revenge", ContestType.Tough,  "last_boost"));
        put("rockblast", new MoveData("rockblast", ContestType.Tough,  "placement_boost"));
        put("rockclimb", new MoveData("rockclimb", ContestType.Tough,  "discourage_audience"));
        put("rockpolish", new MoveData("rockpolish", ContestType.Tough,  "move_earlier"));
        put("rockslide", new MoveData("rockslide", ContestType.Tough,  "startle_priors"));
        put("rocksmash", new MoveData("rocksmash", ContestType.Tough,  "quite_appealing"));
        put("rockthrow", new MoveData("rockthrow", ContestType.Tough,  "quite_appealing"));
        put("rockwrecker", new MoveData("rockwrecker", ContestType.Tough,  "startle_all"));
        put("rototiller", new MoveData("rototiller", ContestType.Tough,  "pump_up_self"));
        put("sandstorm", new MoveData("sandstorm", ContestType.Tough,  "startle_appeals"));
        put("scald", new MoveData("scald", ContestType.Tough,  "intimidate_posts"));
        put("scaryface", new MoveData("scaryface", ContestType.Tough,  "drain_priors"));
        put("scratch", new MoveData("scratch", ContestType.Tough,  "quite_appealing"));
        put("seedbomb", new MoveData("seedbomb", ContestType.Tough,  "quite_appealing"));
        put("seismictoss", new MoveData("seismictoss", ContestType.Tough,  "reusable_appeal"));
        put("shellsmash", new MoveData("shellsmash", ContestType.Tough,  "last_excitement"));
        put("skullbash", new MoveData("skullbash", ContestType.Tough,  "copy_prev"));
        put("skydrop", new MoveData("skydrop", ContestType.Tough,  "block_excite"));
        put("slam", new MoveData("slam", ContestType.Tough,  "quite_appealing"));
        put("sludge", new MoveData("sludge", ContestType.Tough,  "startle_prev"));
        put("sludgebomb", new MoveData("sludgebomb", ContestType.Tough,  "startle_high_expectations"));
        put("sludgewave", new MoveData("sludgewave", ContestType.Tough,  "drain_priors"));
        put("smackdown", new MoveData("smackdown", ContestType.Tough,  "startle_high_expectations"));
        put("smellingsalts", new MoveData("smellingsalts", ContestType.Tough,  "copy_prev"));
        put("smog", new MoveData("smog", ContestType.Tough,  "quite_appealing"));
        put("snarl", new MoveData("snarl", ContestType.Tough,  "expectant_audience"));
        put("spikyshield", new MoveData("spikyshield", ContestType.Tough,  "turn_startle_block"));
        put("spitup", new MoveData("spitup", ContestType.Tough,  "pumped_boost"));
        put("spite", new MoveData("spite", ContestType.Tough,  "startle_appeals"));
        put("steamroller", new MoveData("steamroller", ContestType.Tough,  "reusable_appeal"));
        put("stickyweb", new MoveData("stickyweb", ContestType.Tough,  "startle_high_expectations"));
        put("stockpile", new MoveData("stockpile", ContestType.Tough,  "pump_up_self"));
        put("stomp", new MoveData("stomp", ContestType.Tough,  "quite_appealing"));
        put("stoneedge", new MoveData("stoneedge", ContestType.Tough,  "copy_prev"));
        put("strength", new MoveData("strength", ContestType.Tough,  "quite_appealing"));
        put("superfang", new MoveData("superfang", ContestType.Tough,  "startle_appeals"));
        put("superpower", new MoveData("superpower", ContestType.Tough,  "appeal_risk_startle"));
        put("swallow", new MoveData("swallow", ContestType.Tough,  "startle_block"));
        put("tackle", new MoveData("tackle", ContestType.Tough,  "quite_appealing"));
        put("takedown", new MoveData("takedown", ContestType.Tough,  "appeal_risk_startle"));
        put("thief", new MoveData("thief", ContestType.Tough,  "appeal_prev"));
        put("thrash", new MoveData("thrash", ContestType.Tough,  "appeal_risk_startle"));
        put("torment", new MoveData("torment", ContestType.Tough,  "intimidate_posts"));
        put("vicegrip", new MoveData("vicegrip", ContestType.Tough,  "quite_appealing"));
        put("wakeupslap", new MoveData("wakeupslap", ContestType.Tough,  "copy_prev"));
        put("waterfall", new MoveData("waterfall", ContestType.Tough,  "quite_appealing"));
        put("wideguard", new MoveData("wideguard", ContestType.Tough,  "turn_startle_block"));
        put("wildcharge", new MoveData("wildcharge", ContestType.Tough,  "appeal_risk_startle"));
        put("woodhammer", new MoveData("woodhammer", ContestType.Tough,  "appeal_risk_startle"));
        put("workup", new MoveData("workup", ContestType.Tough,  "first_excitement"));
        put("wrap", new MoveData("wrap", ContestType.Tough,  "block_excite"));
        put("wringout", new MoveData("wringout", ContestType.Tough,  "badly_startle_dup_type"));


        // endregion


    }};

    private final Map<String, FunctionData> ALL_FUNCTION_DATA = new HashMap<>() {{
        put("default", new FunctionData("default", 2, 0,"This move has not been added.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("quite_appealing", new FunctionData("quite_appealing", 4, 0,"Quite an appealing move.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("reusable_appeal", new FunctionData("reusable_appeal", 3, 0, "An appealing move that can be used repeatedly without boring the audience.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("first_boost", new FunctionData("first_boost", 2, 0, "Works great if the user goes first this turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("late_boost", new FunctionData("late_boost", 1, 0, "Works better the later it is used in a turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("last_boost", new FunctionData("last_boost", 2, 0, "Works great if the user goes last this turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("placement_boost", new FunctionData("placement_boost", 1, 0, "Effectiveness varies depending on when it is used.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("pumped_boost", new FunctionData("pumped_boost", 1, 0, "Works well if the user is pumped up.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("dup_type_boost", new FunctionData("dup_type_boost", 2, 0, "Works well if it is the same type as the move used by the last Pokémon.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("excitement_boost", new FunctionData("excitement_boost", 1, 0, "Works better the more the crowd is excited.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("any_excite", new FunctionData("any_excite", 2, 0, "Excites the audience in any kind of contest.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("block_excite", new FunctionData("block_excite", 3, 0, "Temporarily stops the crowd from growing excited.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("first_excitement", new FunctionData("first_excitement", 3, 0, "Excites the audience a lot if used first.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("last_excitement", new FunctionData("last_excitement", 3, 0, "Excites the audience a lot if used last.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("startle_appeals", new FunctionData("startle_appeals", 2, 1, "Badly startles all Pokémon that successfully showed their appeal.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("startle_prev", new FunctionData("startle_prev", 2, 3, "Startles the last Pokémon to act before the user.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("badly_startle_prev", new FunctionData("badly_startle_prev", 2, 2, "Badly startles the last Pokémon to act before the user.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); })); // TODO figure out the values of this one
        put("startle_priors_1", new FunctionData("startle_priors_1", 1, 3, "Startles all of the Pokémon to act before the user.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); })); // TODO figure out the values of this one
        put("startle_priors_2", new FunctionData("startle_priors_2", 2, 2, "Startles all of the Pokémon to act before the user.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); })); // TODO figure out the values of this one
        put("startle_priors_3", new FunctionData("startle_priors_3", 2, 3, "Startles all of the Pokémon to act before the user.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); })); // TODO figure out the values of this one
        put("badly_startle_priors", new FunctionData("badly_startle_priors", 2, 3, "Badly startles all of the Pokémon to act before the user.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("badly_startle_dup_type", new FunctionData("badly_startle_dup_type", 2, 1, "Badly startles Pokémon that used a move of the same type.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("startle_all", new FunctionData("startle_all", 4, 4, "Startles all other Pokémon. User cannot act in the next turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("startle_high_expectations", new FunctionData("startle_high_expectations", 4, 0, "Badly startles Pokémon that the audience has high expectations of.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); })); // TODO figure out the values of this one
        put("startle_block", new FunctionData("startle_block", 2, 0, "Prevents the user from being startled one time this turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("turn_startle_block", new FunctionData("turn_startle_block", 4, 0, "Prevents the user from being startled until the turn ends.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); })); // TODO figure out the values of this one
        put("pump_up_self", new FunctionData("pump_up_self", 1, 0, "Gets the Pokémon pumped up. Helps prevent nervousness, too.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("appeal_risk_startle", new FunctionData("appeal_risk_startle", 6, 0, "A very appealing move, but after using this move, the user is more easily startled.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("appeal_lose_moves", new FunctionData("appeal_lose_moves", 7, 0, "A move of huge appeal, but using it prevents the user from taking further contest moves.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("discourage_audience", new FunctionData("discourage_audience", 3, 0, "Makes audience expect little of other contestants.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("expectant_audience", new FunctionData("expectant_audience", 4, 0, "Makes the audience quickly grow bored when an appeal move has little effect.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("drain_priors", new FunctionData("drain_priors", 3, 0, "Brings down the energy of any Pokémon that have already used a move this turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("intimidate_posts", new FunctionData("intimidate_posts", 2, 0, "Makes the remaining Pokémon nervous.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("copy_prev", new FunctionData("copy_prev", 3, 0, "Affected by how well the previous Pokémon's move went.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("average_priors", new FunctionData("average_priors", 1, 0, "Shows off the Pokémon's appeal about as well as all the moves before it this turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("appeal_prev", new FunctionData("appeal_prev", 1, 0, "Shows off the Pokémon's appeal about as well as the move used just before it.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("move_earlier", new FunctionData("move_earlier", 3, 0, "Causes the user to move earlier on the next turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("scramble", new FunctionData("scramble", 3, 0, "Scrambles the order in which Pokémon will move on the next turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));
        put("move_later", new FunctionData("move_later", 3, 0, "Causes the user to move later on the next turn.", (data, server, contest, contestant) -> { NothingExtra(server, contest, contestant); }));

    }};


    public FunctionData getFunctionDataFromName(String name){
        FunctionData data;
        if(ALL_FUNCTION_DATA.containsKey(name)){
            data = ALL_FUNCTION_DATA.get(name);
        }else {
            data = ALL_FUNCTION_DATA.get("default");
        }
        return data;
    }

    public ContestMoves() {
        //loadFromJson();
    }

    public static class MoveData {
        String name;
        ContestType type;
        String functionType;

        public MoveData(String name, ContestType type, String functionType){
            this.name = name;
            this.type = type;
            this.functionType = functionType;
        }

        public String getName(){
            return name;
        }

        public ContestType getType(){
            return type;
        }

        public String getFunctionType(){
            return functionType;
        }


    }

    public static class FunctionData{
        String name;
        int appeal;
        int jam;
        String description; // This is just here for me, actual descriptions should come from lang file
        protected final OnUse onUse;


        public FunctionData(String name, int appeal, int jam, String description, OnUse onUse){
            this.name = name;
            this.appeal = appeal;
            this.jam = jam;
            this.description = description;
            this.onUse = onUse;

        }


        public String getName(){
            return name;
        }

        public int getAppeal(){
            return appeal;
        }

        public int getJam(){
            return jam;
        }

        public String getDescription(){
            return description;
        }

        public void onUse(MinecraftServer server, Contest contest, Contest.Contestant contestant) {
            this.onUse.onUse(this, server, contest, contestant);
        }

    }


    public void loadFromJson() {
        new GsonBuilder();
        Gson gson = new GsonBuilder().create();
        //MoveData
        String jsonString = "";


        //this.config = CobblemonConfig.GSON.fromJson(fileReader, CobblemonConfig::class.java)


        //ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        //try{
            /**String CONFIG_PATH = "config/$MODID/main.json";
            ResourceLocation jsonLocation = new ResourceLocation(CobbleContestsForge.MOD_ID, "contest_moves.json");
            String str = "main/resources/assets/contest_moves.json";//jsonLocation.getPath();
            System.out.println(str);
            File configFile = new File(jsonLocation.getPath());
            System.out.println(configFile.getAbsolutePath());
            FileReader fileReader = new FileReader(configFile);*/

            //Optional<Resource> resource = resourceManager.getResource(jsonLocation);
            //InputStream inputStream = (InputStream) resource.stream();
            // Read the input stream into a string
            //jsonString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            //fileReader.close();
        //} catch (IOException e) {
            //e.printStackTrace();
        //}
        /**try {
            contestMoves = new HashMap<>();
            ResourceLocation jsonLocation = new ResourceLocation(CobbleContestsForge.MOD_ID, "contest_moves.json");
            InputStream inputStream = Minecraft.getInstance().getResourceManager().getResource(jsonLocation).get().open();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(new InputStreamReader(inputStream)).getAsJsonObject();
            JsonArray movesArray = jsonObject.getAsJsonArray("moves");
            for (int i = 0; i < movesArray.size(); i++) {
                JsonObject moveObject = movesArray.get(i).getAsJsonObject();
                String moveName = moveObject.get("name").getAsString();
                String contestStat = moveObject.get("contestStat").getAsString();
                int appeal = moveObject.get("appeal").getAsInt();
                //System.out.println("Move:" + moveName);
                contestMoves.put(moveName, new MoveData(contestStat, appeal));
            }
        }catch (IOException e){
            System.out.println("failed");
            e.printStackTrace();
        }*/
    }

    public static class MoveDataOld {
        String name;
        String type;
        int appeal;

        public MoveDataOld(String name, String type, int appeal){
            this.name = name;
            this.type = type;
            this.appeal = appeal;
        }

        public String getType(){
            return type;
        }

        public int getAppeal(){
            return appeal;
        }

        public String getName(){
            return name;
        }

    }


    public MoveData getMoveData(String moveName){
        MoveData data = allMoves.get(moveName);
        if(data == null){
            data = defaultMoveData;
        }
        return data;
    }


    //@Environment(EnvType.SERVER)
    public interface OnUse {
        void onUse(FunctionData functionData, MinecraftServer server, Contest contest, Contest.Contestant contestant);
    }

    private static final HashMap<ContestType, List<ContestType>> oppositeType = new HashMap<>(){{
        put(ContestType.Beauty, Arrays.asList(ContestType.Tough, ContestType.Smart));
        put(ContestType.Cool, Arrays.asList(ContestType.Cute, ContestType.Smart));
        put(ContestType.Smart, Arrays.asList(ContestType.Beauty, ContestType.Cool));
        put(ContestType.Cute, Arrays.asList(ContestType.Cool, ContestType.Tough));
        put(ContestType.Tough, Arrays.asList(ContestType.Cute, ContestType.Beauty));
        put(ContestType.None, Arrays.asList(ContestType.None, ContestType.None));
    }};


    public void NothingExtra(MinecraftServer server, Contest contest, Contest.Contestant contestant){
        String move = contestant.getCurrentMove();
        String lastMove = contestant.getLastMove();

        ContestMoves.MoveData moveData = ContestMoves.instance.getMoveData(move);
        ContestMoves.FunctionData functionData = getFunctionDataFromName(moveData.getFunctionType());

        ContestType type = contest.getContestType();

        ContestType moveType = moveData.getType();

        int typeMod = 0;
        if(type.equals(moveType)){
            typeMod = 1;
        }else if(moveType.equals(oppositeType.get(type).getFirst()) || moveType.equals(oppositeType.get(type).get(1))){
            typeMod = -1;
        }

        //System.out.println("This contest type is " + type.name() + " with opposites of " + oppositeType.get(type).getFirst().name() + " and " + oppositeType.get(type).get(1).name() + " and the move type was " + moveType.name());
        int repMove = 0;
        if(move.equals(lastMove)){
            repMove = -1;
        }


        contestant.setTurnHearts(functionData.getAppeal());

        if(repMove == -1){
            contest.scheduleAction(() -> {
                contestant.addTurnHearts(-1);
                contest.addContestantMessage(server, ChatFormatting.RED, "cobble_contests.contest_showcase.reused_move");
                contest.sendEveryoneContestants(server);
                }, 10);
        }
        
        if(typeMod == -1){
            contest.scheduleAction(() -> {
                contestant.addTurnHearts(-1);
                contest.DecreaseApplause(server, contestant);
                contest.addContestantMessage(server, ChatFormatting.RED, "cobble_contests.contest_showcase.bad_appeal_type");
                contest.sendEveryoneContestants(server);
                }, 15);
        }else if(typeMod == 1){
            contest.scheduleAction(() -> {
                contestant.addTurnHearts(1);
                contest.IncreaseApplause(server, contestant);
                contest.addContestantMessage(server, ChatFormatting.AQUA, "cobble_contests.contest_showcase.good_appeal_type");
                contest.sendEveryoneContestants(server);
                }, 15);
        }

    }

    public void MoveLater(){

    }

    public void PreventStartle() {

    }

    public void AppealingMove() {

    }

    public void ExpectLittle() {

    }

    public void GrowBored() {

    }

    public void ExciteAudience() {

    }

    public void StartleHighExpectations() {

    }

    public void MoveEarlier() {

    }

    public void EffectivenessVaries() {

    }

    public void WorkWell() {

    }

    public void PumpedUp() {

    }

    public void BetterWithExcitement() {

    }

    public void MakeNervous() {

    }

    public void EnergyDown() {

    }

    public void AffectedByPreviousMove() {

    }

    public void LastTurn() {

    }

    public void FirstTurn() {

    }

    public void BetterLater() {

    }

    public void StopExcitement() {

    }

    public void VariesEffectiveness() {

    }

    public void BadlyStartles() {

    }

    public void VeryAppealing() {

    }

    public void ShowAppeal() {

    }

    public void RepeatAppeal() {

    }

    public void StartleSameType() {

    }

    public void StartleLastAct() {

    }

    public void StartleAll() {

    }

    public void ExciteAudienceALot() {

    }

}
