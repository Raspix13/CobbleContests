package com.raspix.forge.cobble_contests.network;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.forge.cobble_contests.menus.screens.PlayerContestInfoScreen;
import com.raspix.forge.cobble_contests.pokemon.CVs;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class SBInfoScreenParty {

    public final UUID id;

    public SBInfoScreenParty(UUID id){
        this.id = id;
    }

    public SBInfoScreenParty(FriendlyByteBuf buf){
        this(buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeUUID(this.id);
    }

    public static SBInfoScreenParty decode(FriendlyByteBuf buf) {
        return new SBInfoScreenParty(buf.readUUID());
    }


    public static class Handler {
        public static boolean handle(SBInfoScreenParty packet, Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            context.get().enqueueWork(() -> {
                Player player = ctx.getSender();// context.get().getSender();
                PlayerPartyStore pps = null;
                CompoundTag tag = new CompoundTag();
                try {
                    pps = Cobblemon.INSTANCE.getStorage().getParty(packet.id);
                    List<Pokemon> poke = pps.toGappyList();
                    for(int i = 0; i < 6; i++){
                        if(poke.size() > i && poke.get(i) != null){
                            tag.put("poke" +i, poke.get(i).getPersistentData().getCompound("CVs"));
                        }else{
                            tag.put("poke" +i, new CVs().saveToNBT());
                        }

                    }
                    if (player != null && player instanceof ServerPlayer serverPlayer) {
                        PacketHandler.sendToClient(new CBInfoScreenParty(packet.id, tag), () -> serverPlayer);

                    }

                } catch (NoPokemonStoreException e) {
                    throw new RuntimeException(e);
                }catch (NullPointerException e){

                }

            });
            return true;
        }

    }
}
