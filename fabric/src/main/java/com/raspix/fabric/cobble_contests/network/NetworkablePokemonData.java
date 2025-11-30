package com.raspix.fabric.cobble_contests.network;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.client.render.models.blockbench.FloatingState;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Species;
import com.cobblemon.mod.common.pokemon.status.PersistentStatus;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public class NetworkablePokemonData {
    private UUID pokeUuid;
    private UUID playUuid;
    private String pokeName;
    private String playerName;
    private PokemonProperties properties;
    private Set<String> aspects;
    private PersistentStatus status;
    private FloatingState state;
    private int statusFlag1;
    private int statusFlag2;
    private int numHearts; // the number of solid hearts it should have
    private int numChangeHearts; // the number of hearts that should change
    private ItemStack heldItem;

    public NetworkablePokemonData(UUID pokeUuid, UUID playUuid, String pokeName, String playerName, int statusFlag1, int statusFlag2, int numHearts, int numChangeHearts, PokemonProperties properties, Set<String> aspects, ItemStack heldItem) {
        this.pokeUuid = pokeUuid;
        this.playUuid = playUuid;
        this.pokeName = pokeName;
        this.playerName = playerName;
        this.statusFlag1 = statusFlag1;
        this.statusFlag2 = statusFlag2;
        this.numHearts = numHearts;
        this.numChangeHearts = numChangeHearts;
        this.properties = properties;
        this.aspects = aspects;
        this.state = new FloatingState();
        this.state.setCurrentAspects(aspects);
        this.heldItem = heldItem;

    }

    public Species getSpecies() {
        return PokemonSpecies.INSTANCE.getByName(properties.getSpecies());
    }


    public ResourceLocation asIdentifierDefaultingNamespace(String namespace) {
        String id = namespace.toLowerCase();
        if (id.contains(":")) {
            return ResourceLocation.fromNamespaceAndPath(id.substring(0, id.indexOf(":")), id.substring(id.indexOf(":") + 1));
        } else {
            return ResourceLocation.fromNamespaceAndPath(namespace, id);
        }
    }

    public int getLevel() {
        return properties.getLevel() != null ? properties.getLevel() : 0;
    }

    public Gender getGender() {
        return properties.getGender() != null ? properties.getGender() : Gender.GENDERLESS;
    }

    public void updateAspects(Set<String> aspects) {
        this.aspects = aspects;
        state.setCurrentAspects(aspects);
    }

    public FloatingState getState(){
        return state;
    }

    public UUID getPokeUuid(){
        return pokeUuid;
    }

    public String getPokeName(){
        return pokeName;
    }

    public String getPlayerName(){
        return playerName;
    }

    public int getNumHearts(){
        return numHearts;
    }

    public int getNumChangeHearts(){
        return numChangeHearts;
    }

    public ItemStack getHeldItem(){
        return heldItem;
    }

    public Set<String> getAspects() {
        return aspects;
    }

    public NetworkablePokemonData(FriendlyByteBuf buf){
        this.pokeUuid = buf.readUUID();
        this.properties = PokemonProperties.Companion.parse(buf.readUtf(), ",");

        int size = buf.readVarInt(); // Read the size of the set
        Set<String> set = new HashSet<>();

        for (int i = 0; i < size; i++) {
            set.add(buf.readUtf()); // Read each string element and add it to the set
        }

        this.aspects = set;

        this.state = new FloatingState();
        this.state.setCurrentAspects(aspects);

        /**int mapSize = buf.readUnsignedByte(); // Read the size of the map
        Map<Stat, Integer> statChanges = new HashMap<>();

        for (int i = 0; i < mapSize; i++) {
            Stat stat = Cobblemon.INSTANCE.getStatProvider().decode(buf);
            int stages = buf.readByte(); // Assuming readSizedInt(IntSize.BYTE) reads a byte

            statChanges.put(stat, stages);
        }

        this.state = statChanges;*/

    }

    public void getAsBuf(FriendlyByteBuf buf){
        buf.writeUUID(pokeUuid);
        buf.writeUtf(properties.asString(","));

        buf.writeVarInt(aspects.size()); // Write the size of the set
        for (String element : aspects) {
            buf.writeUtf(element); // Write each string element to the buffer
        }
        /**buf.writeMap(IntSize.U_BYTE, statChanges, (stat, stages) -> {
            Cobblemon.INSTANCE.getStatProvider().encode(buf, stat);
            buf.writeByte(stages);
        });*/
        //readMapK(buf, IntSize.U_BYTE, statChanges);

        /**buf.writeByte(statChanges.size()); // Write the size of the map
        for (Map.Entry<Stat, Integer> entry : statChanges.entrySet()) {
            Stat stat = entry.getKey();
            int stages = entry.getValue();

            Cobblemon.INSTANCE.getStatProvider().encode(buf, stat);
            buf.writeByte(stages);
        }*/

    }

    public NetworkablePokemonData(CompoundTag tag){
        this.pokeUuid = tag.getUUID("id");
        this.playUuid = tag.getUUID("player_id");
        this.pokeName = tag.getString("poke_name");
        this.playerName = tag.getString("player_name");
        this.statusFlag1 = tag.getInt("status_flag_1");
        this.statusFlag2 = tag.getInt("status_flag_2");
        this.numHearts = tag.getInt("hearts");
        this.numChangeHearts = tag.getInt("hearts_change");
        String itemTag = tag.getString("held_item");


        String itemIdString = tag.getString(itemTag);
        String[] itemStringSplit = itemIdString.split(":");
        ResourceLocation itemId = new ResourceLocation(itemStringSplit[0], itemStringSplit[1]);

        // 2. Look up the Item in the global registry
        Item item = BuiltInRegistries.ITEM.get(itemId);
        this.heldItem = new ItemStack(item);

        //System.out.println("Has " + numHearts + " with a change of " + numChangeHearts);

        this.properties = PokemonProperties.Companion.parse(tag.getString("properties"), ",");

        int size = tag.getInt("aspect_size"); // Read the size of the set
        Set<String> set = new HashSet<>();

        for (int i = 0; i < size; i++) {
            set.add(tag.getString("aspect" + i)); // Read each string element and add it to the set
        }

        this.aspects = set;

        this.state = new FloatingState();
        this.state.setCurrentAspects(aspects);
    }

    public CompoundTag getAsTag(){

        CompoundTag tag = new CompoundTag();

        tag.putUUID("id", pokeUuid);
        tag.putUUID("player_id", playUuid);
        tag.putString("poke_name", pokeName);
        tag.putString("player_name", playerName);
        tag.putInt("status_flag_1", statusFlag1);
        tag.putInt("status_flag_2", statusFlag2);
        tag.putInt("hearts", numHearts);
        tag.putInt("hearts_change", numChangeHearts);



        tag.putString("properties", properties.asString(","));

        tag.putInt("aspect_size", aspects.size());

        Iterator<String> iterator = aspects.iterator();
        for (int i = 0; i < aspects.size(); i++) {
            tag.putString("aspect" + i, iterator.next()); // Write each string element to the buffer
        }

        tag.putString("held_item", BuiltInRegistries.ITEM.getKey(heldItem.getItem()).toString());

         return tag;

    }
}