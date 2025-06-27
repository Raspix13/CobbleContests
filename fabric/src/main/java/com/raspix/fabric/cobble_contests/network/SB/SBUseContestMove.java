package com.raspix.fabric.cobble_contests.network.SB;

import com.raspix.fabric.cobble_contests.menus.screens.ContestScreen;
import com.raspix.fabric.cobble_contests.network.CB.CBPlayerMoveAccepted;
import com.raspix.fabric.cobble_contests.network.CB.CBUpdateContestInfo;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.util.Contest;
import com.raspix.fabric.cobble_contests.util.ContestManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SBUseContestMove implements CustomPacketPayload {

    public final UUID id;
    public final String moveName;

    public static final CustomPacketPayload.Type<SBUseContestMove> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.USE_MOVE);
    public static final StreamCodec<FriendlyByteBuf, SBUseContestMove> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, SBUseContestMove>() {
        @Override
        public @NotNull SBUseContestMove decode(FriendlyByteBuf buf) {
            return new SBUseContestMove(FriendlyByteBuf.readUUID(buf), new String(buf.readByteArray()));
        }

        @Override
        public void encode(FriendlyByteBuf buf, SBUseContestMove useContestMove) {
            FriendlyByteBuf.writeUUID(buf, useContestMove.getId());
            FriendlyByteBuf.writeByteArray(buf, useContestMove.getMoveNameBytes());
        }
    };

    public SBUseContestMove(UUID uuid, String moveName) {
        this.id = uuid;
        this.moveName = moveName;
    }

    public UUID getId(){
        return id;
    }

    public String getMoveName(){
        return moveName;
    }

    public byte[] getMoveNameBytes(){
        return moveName.getBytes();
    }

    /**public void recieve(Minecraft minecraft){
        System.out.println("Recieving SBUseContestMove");
        if(Minecraft.getInstance().screen instanceof ContestScreen screen){
            //CompoundTag tag = buf.readNbt();
            //screen.setUpdatedInfo(tag.getInt("index"), Contest.ContestPhase.fromTag(tag, "phase"));
        }
    }*/

    public void recieve(MinecraftServer server, Player player) {
        //System.out.println("Recieving contest Update 1");
        //FriendlyByteBuf bufi = new FriendlyByteBuf(Unpooled.buffer());
        CompoundTag tag = new CompoundTag();
        boolean wasMoveAccepted = false;


        if(ContestManager.INSTANCE.IsAlreadyInContest(id)){
            Contest con = ContestManager.INSTANCE.getPlayersContest(id);
            Contest.ContestPhase phase = con.getRound();
            System.out.println(phase);
            UUID pokemonIdx = con.getContestentPokemon(id);

            wasMoveAccepted = con.contestantPickMove(id, moveName);

            //tag.putUUID("index", pokemonIdx);
            //phase.toTag(tag, "phase");
            //tag.putInt("seconds", con.getTimer());
        }



        if (player != null && player instanceof ServerPlayer serverPlayer && wasMoveAccepted) {


            ServerPlayNetworking.send((ServerPlayer) player, new CBPlayerMoveAccepted(id));
        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}
