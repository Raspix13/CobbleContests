package com.raspix.fabric.cobble_contests.items;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.item.PokeBallItem;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormParticlePacket;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import com.cobblemon.mod.common.pokeball.PokeBall;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.util.ContestManager;
import com.raspix.fabric.cobble_contests.util.data.ParticleEffectList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SealTester extends Item {

    private static int MAX_PARTICLE_INDEX = 6;
    private static List<ResourceLocation> particles = new ArrayList<>(){{
        add(ParticleEffectList.RAINBOW);
        add(ParticleEffectList.SNOW_SWIRL);
        add(ParticleEffectList.LOADING);
        add(ParticleEffectList.SHINY_RING);
        add(ParticleEffectList.IMPACT_DRAGON);
        add(ParticleEffectList.HEART_SMOKEBURST);
        add(ParticleEffectList.SNOW_SWIRL_TWO);
    }};

    public SealTester(Properties properties) {
        super(properties);
    }



    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if(!pLevel.isClientSide()){
            if(pPlayer.isShiftKeyDown()){
                increment(itemstack);
            }else{

                int effect;
                CustomData dat = itemstack.get(DataComponents.CUSTOM_DATA);
                if(dat == null || !dat.contains("effect")){
                    effect = 0;
                }else {
                    effect = dat.getUnsafe().getInt("effect");
                }

                Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty((ServerPlayer) pPlayer).get(0);
                PokemonEntity pokeEnt = poke.getEntity();
                if(pokeEnt != null){
                    ((ServerPlayer) pPlayer).displayClientMessage(Component.translatable("tooltip.cobble_contests.seal_tester.tooltip.effect_used", effect).withStyle(ChatFormatting.GRAY), true);
                    new SpawnSnowstormEntityParticlePacket(particles.get(effect), pokeEnt.getId(), Arrays.asList("middle"), null, null)
                            .sendToPlayersAround(pokeEnt.getX(), pokeEnt.getY(), pokeEnt.getZ(), 64.0, pokeEnt.level().dimension(), serverPlayer -> {
                                return false;
                            });

                    /**new SpawnSnowstormParticlePacket(particles.get(effect), pokeEnt.position())
                            .sendToPlayersAround(pokeEnt.getX(), pokeEnt.getY(), pokeEnt.getZ(), 64.0, pokeEnt.level().dimension(), serverPlayer -> {
                                return false;
                            });*/
                }
            }
        }else{

        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    public void increment(ItemStack thisWand){
        CustomData dat = thisWand.get(DataComponents.CUSTOM_DATA);
        int effect = 0;
        if(dat != null && dat.contains("effect")){
            effect = dat.copyTag().getInt("effect");
        }
        effect = (effect + 1) > MAX_PARTICLE_INDEX? 0 : effect + 1;
        CompoundTag wandTag = new CompoundTag();
        wandTag.putInt("effect", effect);// set new ball
        CustomData.set(DataComponents.CUSTOM_DATA, thisWand, wandTag);
    }


    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        CustomData dat = itemStack.get(DataComponents.CUSTOM_DATA);
        if(dat != null) {
            CompoundTag nbt = dat.copyTag();
            for (String tagInfo : nbt.getAllKeys()) {
                if (tagInfo.contains("effect")) {
                    int effect = nbt.getInt("effect");
                    try {
                        list.add(Component.translatable("tooltip.cobble_contests.seal_tester.tooltip.effect_type", effect).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
                    } catch (ClassCastException e) {
                    }
                }
            }
        }else {
            list.add(Component.translatable("tooltip.cobble_contests.seal_tester.tooltip.effect_type", "0").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY)); //Component.translatable("tooltip.cobble_contests.ball_swapper.tooltip.empty").getString()
        }

    }


}
