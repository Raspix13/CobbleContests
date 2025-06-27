package com.raspix.fabric.cobble_contests.network.CB;

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

public class CBPlayerMoveAccepted implements CustomPacketPayload {

    public final UUID id;

    public static final CustomPacketPayload.Type<CBPlayerMoveAccepted> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.ACCEPT_MOVE);
    public static final StreamCodec<FriendlyByteBuf, CBPlayerMoveAccepted> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBPlayerMoveAccepted>() {
        @Override
        public @NotNull CBPlayerMoveAccepted decode(FriendlyByteBuf buf) {
            //CompoundTag compoundTag = new CompoundTag();
            return new CBPlayerMoveAccepted(FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBPlayerMoveAccepted acceptMove) {
            FriendlyByteBuf.writeUUID(buf, acceptMove.getId());
            //FriendlyByteBuf.writeNbt(buf, acceptMove.getTag());
        }
    };

    public CBPlayerMoveAccepted(UUID id) {
        this.id = id;
        //this.tag = buf;
    }

    public UUID getId(){
        return id;
    }


    public void recieve(Minecraft minecraft){
        System.out.println("Recieving accept Move");
        if(Minecraft.getInstance().screen instanceof ContestScreen screen){
            //CompoundTag tag = buf.readNbt();
            screen.setMoveSelected();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
