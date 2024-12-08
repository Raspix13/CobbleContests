package com.raspix.fabric.cobble_contests.network;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.pokemon.CVs;
import com.raspix.fabric.cobble_contests.pokemon.Ribbons;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.UUID;

public class SBBoothScreen {

    public static void recieve(MinecraftServer server, Player player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender sender) {
        System.out.println("Recieving Booth");
        PlayerPartyStore pps = null;
        CompoundTag tag = new CompoundTag();
        try {
            UUID id = buf.readUUID();
            pps = Cobblemon.INSTANCE.getStorage().getParty(id);
            List<Pokemon> poke = pps.toGappyList();
            for(int i = 0; i < 6; i++){
                if(poke.size() > i && poke.get(i) != null){
                    CompoundTag pers = poke.get(i).getPersistentData();
                    tag.put("poke" +i, pers.getCompound("CVs"));
                    tag.put("poke" +i + "ribbons", pers.getCompound("Ribbons"));
                }else{
                    tag.put("poke" +i, new CVs().saveToNBT());
                    tag.put("poke" +i + "ribbons", new Ribbons().saveToNBT());
                }

            }
            if (player != null && player instanceof ServerPlayer serverPlayer) {
                FriendlyByteBuf bufi = new FriendlyByteBuf(Unpooled.buffer());
                //bufi.writeUUID(id);
                bufi.writeNbt(tag);
                ServerPlayNetworking.send(serverPlayer, MessagesInit.BOOTH_ID_1, bufi);//.sendToClient(new CBWalletScreenParty(id, tag), () -> serverPlayer);
            }

        } catch (NoPokemonStoreException e) {
            throw new RuntimeException(e);
        }catch (NullPointerException e){

        }
    }


    public final UUID id;

    public SBBoothScreen(UUID id){
        this.id = id;
    }

    public SBBoothScreen(FriendlyByteBuf buf){
        this(buf.readUUID());
    }

    public static SBWalletScreenParty read(FriendlyByteBuf buf) {
        return new SBWalletScreenParty(buf.readUUID());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.id);
    }

    public void handle(MinecraftServer server, Player player, ServerGamePacketListenerImpl handler, PacketSender sender) {

    }

}
