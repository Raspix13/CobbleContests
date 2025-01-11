package com.raspix.fabric.cobble_contests.network;

import com.raspix.fabric.cobble_contests.menus.screens.ContestScreen;
import com.raspix.fabric.cobble_contests.menus.screens.PlayerConditionCardScreen;
import com.raspix.fabric.cobble_contests.util.Contest;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CBUpdateContestInfo implements CustomPacketPayload{

    public final UUID id;
    CompoundTag tag;

    public static final CustomPacketPayload.Type<CBUpdateContestInfo> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.CONTEST_UPDATE_2);
    public static final StreamCodec<FriendlyByteBuf, CBUpdateContestInfo> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBUpdateContestInfo>() {
        @Override
        public @NotNull CBUpdateContestInfo decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new CBUpdateContestInfo(FriendlyByteBuf.readUUID(buf), FriendlyByteBuf.readNbt(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBUpdateContestInfo contestInfo) {
            FriendlyByteBuf.writeUUID(buf, contestInfo.getId());
            FriendlyByteBuf.writeNbt(buf, contestInfo.getTag());
        }
    };

    public CBUpdateContestInfo(UUID id, CompoundTag buf) {
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
        System.out.println("Recieving contest Update 2");
        if(Minecraft.getInstance().screen instanceof ContestScreen screen){
            //CompoundTag tag = buf.readNbt();
            System.out.println("Recieving contest Update 2a");
            screen.setUpdatedInfo(tag.getInt("index"), Contest.ContestPhase.fromTag(tag, "phase"), tag.getInt("seconds"));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
