package com.raspix.fabric.cobble_contests.network;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.menus.screens.ContestScreen;
import com.raspix.fabric.cobble_contests.pokemon.CVs;
import com.raspix.fabric.cobble_contests.pokemon.Ribbons;
import com.raspix.fabric.cobble_contests.util.Contest;
import com.raspix.fabric.cobble_contests.util.ContestManager;
import io.netty.buffer.Unpooled;
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

import java.util.List;
import java.util.UUID;

public class SBUpdateContestInfo implements CustomPacketPayload {

    public final UUID id;

    public static final CustomPacketPayload.Type<SBUpdateContestInfo> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.CONTEST_UPDATE_1);
    public static final StreamCodec<FriendlyByteBuf, SBUpdateContestInfo> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, SBUpdateContestInfo>() {
        @Override
        public @NotNull SBUpdateContestInfo decode(FriendlyByteBuf buf) {
            return new SBUpdateContestInfo(FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, SBUpdateContestInfo walletScreenParty) {
            FriendlyByteBuf.writeUUID(buf, walletScreenParty.getId());
        }
    };

    public SBUpdateContestInfo(UUID uuid) {
        this.id = uuid;
    }

    public UUID getId(){
        return id;
    }

    public void recieve(Minecraft minecraft){
        System.out.println("Recieving CBWallet");
        if(Minecraft.getInstance().screen instanceof ContestScreen screen){
            //CompoundTag tag = buf.readNbt();
            //screen.setUpdatedInfo(tag.getInt("index"), Contest.ContestPhase.fromTag(tag, "phase"));
        }
    }

    public void recieve(MinecraftServer server, Player player) {
        //System.out.println("Recieving contest Update 1");
        //FriendlyByteBuf bufi = new FriendlyByteBuf(Unpooled.buffer());
        CompoundTag tag = new CompoundTag();


        if(ContestManager.INSTANCE.IsAlreadyInContest(id)){
            Contest con = ContestManager.INSTANCE.getPlayersContest(id);
            Contest.ContestPhase phase = con.getRound();
            System.out.println(phase);
            //System.out.println(con.getContestants().get(id));
            int pokemonIdx = con.getContestentPokemon(id);

            tag.putInt("index", pokemonIdx);
            phase.toTag(tag, "phase");
            tag.putInt("seconds", con.getTimer());
        }



        if (player != null && player instanceof ServerPlayer serverPlayer) {


            ServerPlayNetworking.send((ServerPlayer) player, new CBUpdateContestInfo(id, tag));
        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}
