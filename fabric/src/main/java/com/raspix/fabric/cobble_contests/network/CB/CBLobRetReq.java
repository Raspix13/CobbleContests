package com.raspix.fabric.cobble_contests.network.CB;

import com.raspix.fabric.cobble_contests.menus.screens.ContestBoothScreen;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CBLobRetReq implements CustomPacketPayload {

    public final UUID id;

    public static final CustomPacketPayload.Type<CBLobRetReq> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.LOB_RET_REQ);
    public static final StreamCodec<FriendlyByteBuf, CBLobRetReq> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBLobRetReq>() {
        @Override
        public @NotNull CBLobRetReq decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new CBLobRetReq(FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBLobRetReq contestInfo) {
            FriendlyByteBuf.writeUUID(buf, contestInfo.getId());
        }
    };

    public CBLobRetReq(UUID id) {
        this.id = id;
    }

    public UUID getId(){
        return id;
    }


    public void recieve(Minecraft minecraft){
        System.out.println("Receiving LobRetReq");
        if(Minecraft.getInstance().screen instanceof ContestBoothScreen screen){
            //screen.redoPlayerPanes(tag);
            screen.setPageToLobby();
            //CompoundTag tag = buf.readNbt();
            //screen.setUpdatedInfo(tag.getUUID("index"), Contest.ContestPhase.fromTag(tag, "phase"), tag.getInt("seconds"));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
