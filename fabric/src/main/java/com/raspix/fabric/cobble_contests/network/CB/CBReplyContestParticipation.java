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

public class CBReplyContestParticipation implements CustomPacketPayload {

    public final UUID id;
    CompoundTag tag;

    public static final CustomPacketPayload.Type<CBReplyContestParticipation> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.CONTESTANT_STATUS_REP);
    public static final StreamCodec<FriendlyByteBuf, CBReplyContestParticipation> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBReplyContestParticipation>() {
        @Override
        public @NotNull CBReplyContestParticipation decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new CBReplyContestParticipation(FriendlyByteBuf.readUUID(buf), FriendlyByteBuf.readNbt(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBReplyContestParticipation contestInfo) {
            FriendlyByteBuf.writeUUID(buf, contestInfo.getId());
            FriendlyByteBuf.writeNbt(buf, contestInfo.getTag());
        }
    };

    public CBReplyContestParticipation(UUID id, CompoundTag buf) {
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
        System.out.println("Recieving CBReplyContestParticipation");
        if(Minecraft.getInstance().screen instanceof ContestBoothScreen screen){
            //CompoundTag tag = buf.readNbt();

            screen.setScreenForContestState(tag.getBoolean("in_contest"), tag.getBoolean("is_host"), Contest.ContestPhase.fromTag(tag, "phase"));

            //screen.setShowdownContestantsData(tag);

        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}