package com.raspix.fabric.cobble_contests.network.SB;

import com.raspix.fabric.cobble_contests.menus.screens.ContestScreen;
import com.raspix.fabric.cobble_contests.network.CB.CBReplyContestParticipation;
import com.raspix.fabric.cobble_contests.network.CB.CBSendContestantStatus;
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

public class SBCheckContestParticipation implements CustomPacketPayload {

    public final UUID id;

    public static final CustomPacketPayload.Type<SBCheckContestParticipation> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.CONTESTANT_STATUS_REQ);
    public static final StreamCodec<FriendlyByteBuf, SBCheckContestParticipation> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, SBCheckContestParticipation>() {
        @Override
        public @NotNull SBCheckContestParticipation decode(FriendlyByteBuf buf) {
            return new SBCheckContestParticipation(FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, SBCheckContestParticipation walletScreenParty) {
            FriendlyByteBuf.writeUUID(buf, walletScreenParty.getId());
        }
    };

    public SBCheckContestParticipation(UUID uuid) {
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
        //System.out.println("Recieving contest Update 1");
        //FriendlyByteBuf bufi = new FriendlyByteBuf(Unpooled.buffer());
        CompoundTag tag = new CompoundTag();
        CompoundTag tagContestants = new CompoundTag();

        boolean isInContest = ContestManager.INSTANCE.IsAlreadyInContest(id);
        boolean isPlayerHost = false;
        Contest.ContestPhase phase = Contest.ContestPhase.IDLE;

        if(isInContest){
            Contest con = ContestManager.INSTANCE.getPlayersContest(id);
            phase = con.getRound();
            isPlayerHost = con.isPlayerHost(id);
            tagContestants = con.generateContestantDataTag(server);
        }

        tag.putBoolean("in_contest", isInContest);
        tag.putBoolean("is_host", isPlayerHost);
        phase.toTag(tag, "phase");



        if (player != null && player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new CBReplyContestParticipation(id, tag));
            if(isInContest){

                ServerPlayNetworking.send(serverPlayer, new CBSendContestantStatus(id, tagContestants.copy()));
            }
        }



    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}