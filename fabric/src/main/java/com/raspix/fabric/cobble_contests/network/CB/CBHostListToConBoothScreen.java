package com.raspix.fabric.cobble_contests.network.CB;

import com.raspix.fabric.cobble_contests.menus.screens.ContestBoothScreen;
import com.raspix.fabric.cobble_contests.menus.screens.ContestScreen;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.util.Contest;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CBHostListToConBoothScreen implements CustomPacketPayload {

    public final UUID id;
    CompoundTag tag;

    public static final CustomPacketPayload.Type<CBHostListToConBoothScreen> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.HOSTLIST_TO_CONBOOTHSCREEN);
    public static final StreamCodec<FriendlyByteBuf, CBHostListToConBoothScreen> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBHostListToConBoothScreen>() {
        @Override
        public @NotNull CBHostListToConBoothScreen decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new CBHostListToConBoothScreen(FriendlyByteBuf.readUUID(buf), FriendlyByteBuf.readNbt(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBHostListToConBoothScreen contestInfo) {
            FriendlyByteBuf.writeUUID(buf, contestInfo.getId());
            FriendlyByteBuf.writeNbt(buf, contestInfo.getTag());
        }
    };

    public CBHostListToConBoothScreen(UUID id, CompoundTag buf) {
        this.id = id;
        this.tag = buf;
    }

    public UUID getId(){
        return id;
    }

    public CompoundTag getTag(){
        return tag;
    }

    public void recieve(Minecraft minecraft){
        System.out.println("Receiving HostListToConBoothScreen");
        if(Minecraft.getInstance().screen instanceof ContestBoothScreen screen){
            screen.redoPlayerPanes(tag);
            //CompoundTag tag = buf.readNbt();
            //screen.setUpdatedInfo(tag.getUUID("index"), Contest.ContestPhase.fromTag(tag, "phase"), tag.getInt("seconds"));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
