package com.raspix.fabric.cobble_contests.network.CB;

import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.util.ContestManagerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.UUID;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class CBClearMessageQueue implements CustomPacketPayload {

    public UUID id;
    public String loc;
    public Vector3f position;

    public static final CustomPacketPayload.Type<CBClearMessageQueue> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.CLEAR_QUEUE);
    public static final StreamCodec<FriendlyByteBuf, CBClearMessageQueue> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBClearMessageQueue>() {
        @Override
        public @NotNull CBClearMessageQueue decode(FriendlyByteBuf buf) {
            return new CBClearMessageQueue(FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBClearMessageQueue contestInfo) {
            FriendlyByteBuf.writeUUID(buf, contestInfo.getId());
        }
    };

    public UUID getId() {
        return id;
    }

    public CBClearMessageQueue(UUID id) {
        this.id = id;
    }


    public void recieve(Minecraft minecraft){
        ContestManagerClient.INSTANCE.deleteContestantMessage(id);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
