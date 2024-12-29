package com.raspix.neoforge.cobble_contests.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class JsonLoadMoves extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static JsonLoadMoves instance = new JsonLoadMoves(GSON, "moves");

    public Map<String, ContestMoves.MoveData> moves;

    public JsonLoadMoves(Gson gson, String string) {
        super(gson, string);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager rM, ProfilerFiller pF) {
        map.forEach((rL, json) -> {


            JsonObject moveObject = json.getAsJsonObject();
            String moveName = moveObject.get("name").getAsString();
            String contestStat = moveObject.get("contestStat").getAsString();
            int appeal = moveObject.get("appeal").getAsInt();

            //moves.put(moveName, new ContestMoves.MoveData(contestStat, appeal));
            System.out.println("Adding moves"+ rL.getPath());
        });
    }
}
