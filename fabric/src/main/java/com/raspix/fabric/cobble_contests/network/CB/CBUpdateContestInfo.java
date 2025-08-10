package com.raspix.fabric.cobble_contests.network.CB;

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

public class CBUpdateContestInfo implements CustomPacketPayload{

    public final UUID id;
    CompoundTag tag;
    //public int round;
    //public boolean shouldPickMoves;

    public static final CustomPacketPayload.Type<CBUpdateContestInfo> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.CONTEST_UPDATE_2);
    public static final StreamCodec<FriendlyByteBuf, CBUpdateContestInfo> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBUpdateContestInfo>() {
        @Override
        public @NotNull CBUpdateContestInfo decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new CBUpdateContestInfo(FriendlyByteBuf.readUUID(buf), FriendlyByteBuf.readNbt(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBUpdateContestInfo contestInfo) {
            FriendlyByteBuf.writeUUID(buf, contestInfo.getId());
            FriendlyByteBuf.writeNbt(buf, contestInfo.getTag());
        }
    };

    public CBUpdateContestInfo(UUID id, CompoundTag buf) {
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
        //System.out.println("Recieving contest Update 2");
        if(Minecraft.getInstance().screen instanceof ContestScreen screen){
            //CompoundTag tag = buf.readNbt();
            Contest.ContestPhase phase = Contest.ContestPhase.fromTag(tag, "phase");

            if(tag.contains("showcase_round")){
                screen.setUpdatedInfo(tag.getUUID("index"), phase, tag.getInt("seconds"), tag.getInt("showcase_round"),
                        tag.getBoolean("can_choose_move"), tag.getBoolean("all_moves_picked"), tag.getInt("applause"));
            }else {
                screen.setUpdatedInfo(tag.getUUID("index"), phase, tag.getInt("seconds"));
            }

            if(phase.equals(Contest.ContestPhase.RESULTS)){
                if(tag.contains("is_ranked") && tag.getInt("is_ranked") == 1){
                    boolean winStatus = false;
                    if(tag.contains("did_win") && tag.getBoolean("did_win")){
                        winStatus = true;
                    }
                    NetworkablePokemonData data = new NetworkablePokemonData(tag.getCompound("contestant"));

                    screen.setUpdatedRankResults(winStatus, data);
                } else if(tag.contains("num_first") ){
                    if(tag.getInt("num_first") == 1){ // TODO change this later
                        CompoundTag rankTag = tag.getCompound("first_rank");
                        NetworkablePokemonData data = new NetworkablePokemonData(rankTag.getCompound("contestant0"));
                        screen.setUpdatedResults(data);
                    }
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
