package com.raspix.fabric.cobble_contests.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record BlockPosPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<BlockPosPayload> ID = new CustomPacketPayload.Type<>(MessagesInit.CONTEST_BOOTH);
    public static final StreamCodec<FriendlyByteBuf, BlockPosPayload> PACKET_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, BlockPosPayload::pos, BlockPosPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
