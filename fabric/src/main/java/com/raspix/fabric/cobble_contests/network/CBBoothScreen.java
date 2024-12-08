package com.raspix.fabric.cobble_contests.network;

import com.raspix.fabric.cobble_contests.menus.screens.PlayerContestInfoScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;


public class CBBoothScreen {


    public final UUID id;
    CompoundTag tag;

    public static void recieve(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        System.out.println("Sending Booth Pos");


    }

    public CBBoothScreen(UUID id, CompoundTag tag){
        this.id = id;
        this.tag = tag;
    }

    public CBBoothScreen(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readNbt());
    }

    public static FriendlyByteBuf encode(FriendlyByteBuf buf, UUID id, CompoundTag tag){
        buf.writeUUID(id);
        buf.writeNbt(tag);
        return  buf;
    }



    /**public static class Handler {
     public static void handle(CBWalletScreenParty packet, Supplier<NetworkEvent.Context> context) {
     NetworkEvent.Context ctx = context.get();
     ctx.enqueueWork(() -> {
     if(Minecraft.getInstance().screen instanceof PlayerContestInfoScreen screen){
     screen.setCVs(packet.tag);
     screen.setRibbons(packet.tag);
     }
     });
     ctx.setPacketHandled(true);
     }

     }*/


}
