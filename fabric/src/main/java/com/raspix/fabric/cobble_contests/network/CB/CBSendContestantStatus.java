package com.raspix.fabric.cobble_contests.network.CB;

import com.raspix.fabric.cobble_contests.menus.screens.ContestBoothScreen;
import com.raspix.fabric.cobble_contests.menus.screens.ContestScreen;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class CBSendContestantStatus implements CustomPacketPayload {

    public final UUID id;
    CompoundTag tag;
    int applause;
    //public int round;
    //public boolean shouldPickMoves;

    public static final CustomPacketPayload.Type<CBSendContestantStatus> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.SEND_CONTESTANTS);
    public static final StreamCodec<FriendlyByteBuf, CBSendContestantStatus> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBSendContestantStatus>() {
        @Override
        public @NotNull CBSendContestantStatus decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new CBSendContestantStatus(FriendlyByteBuf.readUUID(buf), FriendlyByteBuf.readNbt(buf), buf.readInt());
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBSendContestantStatus contestInfo) {
            FriendlyByteBuf.writeUUID(buf, contestInfo.getId());
            FriendlyByteBuf.writeNbt(buf, contestInfo.getTag());
            buf.writeInt(contestInfo.getApplause());
        }
    };

    public CBSendContestantStatus(UUID id, CompoundTag buf, int numApplause) {
        this.id = id;
        this.tag = buf;
        this.applause = numApplause;
    }

    public UUID getId(){
        return id;
    }

    public CompoundTag getTag(){
        return tag;
    }

    public int getApplause(){
        return applause;
    }

    public void recieve(Minecraft minecraft){
        System.out.println("Recieving CBSendContestants");
        if(Minecraft.getInstance().screen instanceof ContestScreen screen){
            screen.setShowdownContestantsData(tag);
            screen.setNumApplause(applause);
        }else if (Minecraft.getInstance().screen instanceof ContestBoothScreen bScreen){
            bScreen.updateLobbyContestants(tag);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}