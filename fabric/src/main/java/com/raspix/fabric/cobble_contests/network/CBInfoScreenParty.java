package com.raspix.fabric.cobble_contests.network;

import com.raspix.forge.cobble_contests.menus.screens.PlayerContestInfoScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CBInfoScreenParty {


    public final UUID id;
    CompoundTag tag;

    public CBInfoScreenParty(UUID id, CompoundTag tag){
        this.id = id;
        this.tag = tag;
    }

    public CBInfoScreenParty(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeUUID(this.id);
        buf.writeNbt(this.tag);
    }


    public static class Handler {
        public static void handle(CBInfoScreenParty packet, Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            ctx.enqueueWork(() -> {
                if(Minecraft.getInstance().screen instanceof PlayerContestInfoScreen screen){
                    screen.setCVs(packet.tag);
                    screen.setRibbons(packet.tag);
                }
            });
            ctx.setPacketHandled(true);
        }

    }


}
