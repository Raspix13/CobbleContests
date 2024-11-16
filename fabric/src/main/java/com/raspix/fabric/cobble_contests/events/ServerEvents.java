package com.raspix.fabric.cobble_contests.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;

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
