package com.raspix.fabric.cobble_contests.network.SB;

import com.raspix.fabric.cobble_contests.network.CB.CBHostListToConBoothScreen;
import com.raspix.fabric.cobble_contests.network.CB.CBUpdateContestInfo;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.util.Contest;
import com.raspix.fabric.cobble_contests.util.ContestManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Used by the contest booth screen to request a list of the hosts
public class SBReqJoinLob implements CustomPacketPayload {

    public final UUID id;
    public final UUID pokeID;
    public final UUID hostID;


    public static final CustomPacketPayload.Type<SBReqJoinLob> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.REQ_JOIN_LOB);
    public static final StreamCodec<FriendlyByteBuf, SBReqJoinLob> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, SBReqJoinLob>() {
        @Override
        public @NotNull SBReqJoinLob decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new SBReqJoinLob(FriendlyByteBuf.readUUID(buf), FriendlyByteBuf.readUUID(buf), FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, SBReqJoinLob payload) {
            FriendlyByteBuf.writeUUID(buf, payload.getId());
            FriendlyByteBuf.writeUUID(buf, payload.getPokeId());
            FriendlyByteBuf.writeUUID(buf, payload.getHostId());
        }
    };

    public UUID getId(){
        return id;
    }

    public UUID getPokeId(){
        return pokeID;
    }

    public UUID getHostId(){
        return hostID;
    }

    public SBReqJoinLob(UUID id, UUID pokeID, UUID hostID){
        this.id = id;
        this.pokeID = pokeID;
        this.hostID = hostID;
    }

    public SBReqJoinLob(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readUUID(), buf.readUUID());
    }

    /**public void encode(FriendlyByteBuf buf){
        buf.writeUUID(this.id);
        buf.writeUUID(this.hostID);
    }*/


    public void recieve(MinecraftServer server, Player player) {
        System.out.println("Recieving ReqJoinLob");

        Contest goalContest = ContestManager.INSTANCE.getPlayersContest(getHostId());

        goalContest.addContestants(server ,(ServerPlayer) player, getId(), getPokeId());

        //ServerPlayNetworking.send((ServerPlayer) player, new CBHostListToConBoothScreen(id, tag));
    }




    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
