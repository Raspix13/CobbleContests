package com.raspix.fabric.cobble_contests.network.CB;

import com.raspix.fabric.cobble_contests.menus.screens.ContestBoothScreen;
import com.raspix.fabric.cobble_contests.menus.screens.ContestScreen;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.network.NetworkablePokemonData;
import com.raspix.fabric.cobble_contests.util.Contest;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CBAlertContestStarting implements CustomPacketPayload {

    public final UUID id;
    CompoundTag tag;
    //public int round;
    //public boolean shouldPickMoves;

    public static final CustomPacketPayload.Type<CBAlertContestStarting> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.ALERT_START);
    public static final StreamCodec<FriendlyByteBuf, CBAlertContestStarting> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBAlertContestStarting>() {
        @Override
        public @NotNull CBAlertContestStarting decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new CBAlertContestStarting(FriendlyByteBuf.readUUID(buf), FriendlyByteBuf.readNbt(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBAlertContestStarting contestInfo) {
            FriendlyByteBuf.writeUUID(buf, contestInfo.getId());
            FriendlyByteBuf.writeNbt(buf, contestInfo.getTag());
        }
    };

    public CBAlertContestStarting(UUID id, CompoundTag buf) {
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

        if(Minecraft.getInstance().screen instanceof ContestBoothScreen screen){

            screen.setScreenForContestState(tag.getBoolean("in_contest"), tag.getBoolean("is_host"), Contest.ContestPhase.fromTag(tag, "phase"));

        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
