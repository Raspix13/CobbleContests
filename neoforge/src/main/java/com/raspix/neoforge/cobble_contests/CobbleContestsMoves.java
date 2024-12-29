package com.raspix.neoforge.cobble_contests;

import com.cobblemon.mod.common.api.data.DataRegistry;
import com.cobblemon.mod.common.api.data.JsonDataRegistry;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raspix.neoforge.cobble_contests.events.ContestMoves;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CobbleContestsMoves implements JsonDataRegistry<ContestMoves.MoveData> {

    public static CobbleContestsMoves INSTANCE = new CobbleContestsMoves();
    public Map<String, ContestMoves.MoveData> allMoves = new HashMap<>();

    @NotNull
    @Override
    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(CobbleContestsForge.MOD_ID, "contest_moves");
    }

    @NotNull
    @Override
    public SimpleObservable<? extends DataRegistry> getObservable() {
        return new SimpleObservable<CobbleContestsMoves>();
    }

    @NotNull
    @Override
    public PackType getType() {
        return PackType.SERVER_DATA;
    }

    public class Move {
        private String name;
        private String contestStat;
        private int appeal;

        // Default constructor (required by Gson)
        public Move() {
        }

        // Getters and setters for the properties
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContestStat() {
            return contestStat;
        }

        public void setContestStat(String contestStat) {
            this.contestStat = contestStat;
        }

        public int getAppeal() {
            return appeal;
        }

        public void setAppeal(int appeal) {
            this.appeal = appeal;
        }

        public String toString(){
            return "Name: " + name + ", Stat: " + getContestStat() + ", Appeal: " + appeal;
        }
    }

    private <T> T loadMechanic(ResourceManager manager, String name, Class<T> clazz) {
        try (var inputStream = manager.getResourceOrThrow(ResourceLocation.fromNamespaceAndPath(CobbleContestsForge.MOD_ID, "moves/" + name + ".json")).open()) {
            return getGson().fromJson(new InputStreamReader(inputStream), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T loadLine(String line, Class<T> clazz) {
        System.out.println(line);
        CobbleContestsForge.LOGGER.info(getGson().fromJson(line, clazz).toString());
        return getGson().fromJson(line, clazz);
        //return null;
    }


    @Override
    public void reload(@NotNull ResourceManager resourceManager) {
        this.allMoves.clear();
        resourceManager.listResourceStacks("moves", (path) -> path.getPath().endsWith(".json")).forEach((identifier, resource) -> {
                    try (InputStream stream = resource.get(0).open()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                        ResourceLocation resolvedIdentifier = ResourceLocation.fromNamespaceAndPath(identifier.getNamespace(), new File(identifier.getPath()).getName());
                        List<String> lines = reader.lines().toList();
                        for(int i = 1; i < lines.size()-2; i++){
                            if(!lines.get(i).equals("")) {
                                Move newMove = loadLine(lines.get(i).substring(0, lines.get(i).length() - 1).replaceAll("\\s", ""), Move.class);
                                allMoves.put(newMove.name, new ContestMoves.MoveData(newMove.name, newMove.contestStat, newMove.appeal));
                            }
                        }
                        Move lastMove = loadLine(lines.get(lines.size()-2).replaceAll("\\s", ""), Move.class);
                        allMoves.put(lastMove.name, new ContestMoves.MoveData(lastMove.name, lastMove.contestStat, lastMove.appeal));
                        //String js = reader.lines().collect(Collectors.joining("\n"));

                        //Map<String, String> mapie = getGson().fromJson(js, Move.class);

                        //allMoves.put("tackle1", new ContestMoves.MoveData("tackle1", "Cute", 1));

                        //CobbleContestsForge.LOGGER.info("RIGHT HERE THERE ARE {} THINGS READ XXXXXXXXXXXXXX", js);
                        //CobbleContestsForge.LOGGER.info("RIGHT HERE THERE ARE {} THINGS READ XXXXXXXXXXXXXX", mapie.size());
                        //bagItemsScripts.put(resolvedIdentifier.getPath(), js);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        //allMoves.put("tackle", new ContestMoves.MoveData("tackle", "Cute", 1));
        CobbleContestsForge.LOGGER.info("Loaded {} contest moves", this.allMoves.size());
        this.getObservable().emit();
    }

    @Override
    public void sync(@NotNull ServerPlayer serverPlayer) {

    }

    @NotNull
    @Override
    public Gson getGson() {
        return new Gson();

                /**new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();*/
    }

    @NotNull
    @Override
    public String getResourcePath() {
        return "moves";
    }

    @NotNull
    @Override
    public TypeToken getTypeToken() {
        return TypeToken.get(ContestMoves.MoveData.class);
    }


    @Override
    public void reload(@NotNull Map<ResourceLocation, ? extends ContestMoves.MoveData> data) {
        this.allMoves.clear();
        for (Map.Entry<ResourceLocation, ? extends ContestMoves.MoveData> entry : data.entrySet()) {
            ResourceLocation identifier = entry.getKey();
            ContestMoves.MoveData dat = entry.getValue();
            try {
                this.allMoves.put(identifier.getNamespace(), dat);
            } catch (Exception e) {
                CobbleContestsForge.LOGGER.error("Error loading the {} move", identifier, e);
            }
        }
        //CobbleContestsForge.LOGGER.info("MEOW RIGHT HERE or something");
        CobbleContestsForge.LOGGER.info("Loaded {} contest moves", this.allMoves.size());
        this.getObservable().emit();
    }

    /**@Override
    public void reload(@NotNull Map<ResourceLocation, ContestMoves.MoveData> data) {
        this.allMoves.clear();
        data.forEach { (identifier, berry) ->
            try {
                berry.identifier = identifier
                berry.validate()
                this.berries[identifier] = berry
            } catch (e: Exception) {
                Cobblemon.LOGGER.error("Skipped loading the {} berry", identifier, e)
            }
        }
        Cobblemon.LOGGER.info("Loaded {} berries", this.berries.size)
        this.observable.emit(this)
    }*/

}
