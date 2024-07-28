package com.raspix.forge.cobble_contests.network;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.forge.cobble_contests.blocks.entity.ContestBlockEntity;
import com.raspix.forge.cobble_contests.pokemon.Badges;
import com.raspix.forge.cobble_contests.pokemon.CVs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class CBERunContest {

    public final UUID id;
    public final int index;
    public final BlockPos pos;
    public final int contestType;
    public final int contestLevel;

    public CBERunContest(UUID id, int index, BlockPos pos, int contestType, int contestLevel){
        this.id = id;
        this.index = index;
        this.pos = pos;
        this.contestType = contestType;
        this.contestLevel = contestLevel;
    }

    public CBERunContest(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readInt(), buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeUUID(this.id);
        buf.writeInt(this.index);
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.contestType);
        buf.writeInt(this.contestLevel);
    }

    public static CBERunContest decode(FriendlyByteBuf buf) {
        return new CBERunContest(buf.readUUID(), buf.readInt(), buf.readBlockPos(), buf.readInt(), buf.readInt());
    }


    public static class Handler {
        public static boolean handle(CBERunContest packet, Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            context.get().enqueueWork(() -> {
                Player player = ctx.getSender();
                Level level = player.level();
                BlockEntity be = level.getBlockEntity(packet.pos);
                if(be instanceof ContestBlockEntity cbe){
                    cbe.runStatAssesment(packet.id, packet.index, packet.contestType, packet.contestLevel);
                }


            });
            return true;
        }

    }

}
