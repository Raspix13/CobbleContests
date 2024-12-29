package com.raspix.neoforge.cobble_contests.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.raspix.neoforge.cobble_contests.CobbleContestsForge;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = CobbleContestsForge.MOD_ID)
public class ServerEvents {


    /**@SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        try {
            if (event.getEntity() != null && event.getEntity() instanceof PokemonEntity){
                PokemonEntity poke = (PokemonEntity) event.getEntity();

                //poke..addEntityProperty(null, 1);
                //poke.getPokemon().getEntity().getPokemon()
                //EntityDataAccessor
            }
        }catch (Exception e){

        }
    }*/

    /**@SubscribeEvent(priority = EventPriority.HIGH)
    public void PokemonEntitySaveEvent(PokemonEntity pokemonEntity, CompoundTag nbt){
        System.out.println("saved");
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void PokemonEntityLoadEvent(PokemonEntity pokemonEntity, CompoundTag nbt) {
        System.out.println("loaded");
    }*/
}
