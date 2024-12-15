package com.raspix.forge.cobble_contests.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.raspix.forge.cobble_contests.CobbleContestsForge;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CobbleContestsForge.MOD_ID)
public class ServerEvents {


    @SubscribeEvent(priority = EventPriority.HIGH)
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
    }

    /**@SubscribeEvent(priority = EventPriority.HIGH)
    public void PokemonEntitySaveEvent(PokemonEntity pokemonEntity, CompoundTag nbt){
        System.out.println("saved");
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void PokemonEntityLoadEvent(PokemonEntity pokemonEntity, CompoundTag nbt) {
        System.out.println("loaded");
    }*/
}
