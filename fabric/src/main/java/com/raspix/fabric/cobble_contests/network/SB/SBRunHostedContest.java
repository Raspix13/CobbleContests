package com.raspix.fabric.cobble_contests.network.SB;

import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.util.Contest;
import com.raspix.fabric.cobble_contests.util.ContestManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SBRunHostedContest implements CustomPacketPayload{


    public final UUID id;



    public static final CustomPacketPayload.Type<SBRunHostedContest> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.CONTEST_HOST_STARTER);
    public static final StreamCodec<FriendlyByteBuf, SBRunHostedContest> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, SBRunHostedContest>() {
        @Override
        public @NotNull SBRunHostedContest decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new SBRunHostedContest(FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, SBRunHostedContest payload) {
            FriendlyByteBuf.writeUUID(buf, payload.getId());
        }
    };

    public UUID getId(){
        return id;
    }

    public SBRunHostedContest(UUID id){
        this.id = id;
    }

    public SBRunHostedContest(FriendlyByteBuf buf){
        this(buf.readUUID());
    }



    public void recieve(MinecraftServer server, Player player) {
        System.out.println("Recieving RunHostedContest");

        UUID id = getId();
        Contest con = ContestManager.INSTANCE.getPlayersContest(id);
        if (con != null){
            System.out.println("A Contest Was Found");
            con.startContest(id);
        }else{
            System.out.println("No Contest Was Found");
        }
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
