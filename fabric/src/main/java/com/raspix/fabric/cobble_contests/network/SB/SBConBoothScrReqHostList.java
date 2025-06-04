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
public class SBConBoothScrReqHostList implements CustomPacketPayload {

    public final UUID id;

    public static final CustomPacketPayload.Type<SBConBoothScrReqHostList> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.CONBOOTHSCREEN_REQ_HOSTLIST);
    public static final StreamCodec<FriendlyByteBuf, SBConBoothScrReqHostList> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, SBConBoothScrReqHostList>() {
        @Override
        public @NotNull SBConBoothScrReqHostList decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new SBConBoothScrReqHostList(FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, SBConBoothScrReqHostList payload) {
            FriendlyByteBuf.writeUUID(buf, payload.getId());
        }
    };

    public UUID getId(){
        return id;
    }

    public SBConBoothScrReqHostList(UUID id){
        this.id = id;
    }

    public SBConBoothScrReqHostList(FriendlyByteBuf buf){
        this(buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeUUID(this.id);
    }

    public static SBRunContest decode(FriendlyByteBuf buf) {
        return new SBRunContest(buf.readUUID(), buf.readUUID(), buf.readBlockPos(), buf.readInt(), buf.readInt());
    }


    public void recieve(MinecraftServer server, Player player) {
        System.out.println("Recieving ConBoothScrReqHostList");

        List<Contest> contests = ContestManager.INSTANCE.getContests();
        CompoundTag tag = generateSendableTag(server, contests);


        UUID id = getId();

        ServerPlayNetworking.send((ServerPlayer) player, new CBHostListToConBoothScreen(id, tag));
    }

    private CompoundTag generateSendableTag(MinecraftServer server, List<Contest> contests){
        PlayerList playerList = server.getPlayerList();
        ListTag listTag = new ListTag(); // names

        for(Contest contest: contests){
            CompoundTag tag = new CompoundTag();

            tag.putUUID("host_id", contest.getHost());
            if(playerList.getPlayer(contest.getHost()) != null){
                tag.putString("host_name", playerList.getPlayer(contest.getHost()).getDisplayName().getString());
            }else {
                tag.putString("host_name", "Player Not Found");
            }
            tag.putInt("contest_type", contest.getContestType());

            listTag.add(tag);

        }

        CompoundTag sinlgeTag = new CompoundTag();
        sinlgeTag.put("contest_list", listTag);
        return sinlgeTag;
    }




    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
