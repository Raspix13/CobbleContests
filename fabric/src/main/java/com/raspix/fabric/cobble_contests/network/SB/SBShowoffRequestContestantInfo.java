package com.raspix.fabric.cobble_contests.network.SB;

import com.raspix.fabric.cobble_contests.menus.screens.ContestScreen;
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

public class SBShowoffRequestContestantInfo implements CustomPacketPayload {

    public final UUID id;

    public static final CustomPacketPayload.Type<SBShowoffRequestContestantInfo> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.REQUEST_CONTESTANTS);
    public static final StreamCodec<FriendlyByteBuf, SBShowoffRequestContestantInfo> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, SBShowoffRequestContestantInfo>() {
        @Override
        public @NotNull SBShowoffRequestContestantInfo decode(FriendlyByteBuf buf) {
            return new SBShowoffRequestContestantInfo(FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, SBShowoffRequestContestantInfo walletScreenParty) {
            FriendlyByteBuf.writeUUID(buf, walletScreenParty.getId());
        }
    };

    public SBShowoffRequestContestantInfo(UUID uuid) {
        this.id = uuid;
    }

    public UUID getId(){
        return id;
    }

    /**public void recieve(Minecraft minecraft){
        System.out.println("Recieving ContestUpdate1");
        if(Minecraft.getInstance().screen instanceof ContestScreen screen){
            //CompoundTag tag = buf.readNbt();
            //screen.setUpdatedInfo(tag.getInt("index"), Contest.ContestPhase.fromTag(tag, "phase"));
        }
    }*/

    public void recieve(MinecraftServer server, Player player) {
        System.out.println("Recieving ShowoffRequestContestantInfo");

        if(ContestManager.INSTANCE.IsAlreadyInContest(id)){
            Contest con = ContestManager.INSTANCE.getPlayersContest(id);
            con.sendPlayerContestants(server, (ServerPlayer) player);


        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}