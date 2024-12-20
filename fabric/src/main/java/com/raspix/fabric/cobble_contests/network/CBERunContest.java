package com.raspix.fabric.cobble_contests.network;

import com.raspix.fabric.cobble_contests.blocks.entity.ContestBlockEntity;
import com.raspix.fabric.cobble_contests.menus.screens.PlayerContestInfoScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

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


    public static void recieve(MinecraftServer server, Player player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender sender) {
        System.out.println("Recieving RunContest");

        Level level = player.level();
        /**if(player instanceof ServerPlayer){
            System.out.println("player is serverplayer");
        }else {
            System.out.println("player is client");
        }
        if (!level.isClientSide()){
            System.out.println("Is server");
        }else {
            System.out.println("Is client");
        }*/
        UUID id = buf.readUUID();
        int index = buf.readInt();
        BlockPos pos = buf.readBlockPos();
        int contestType = buf.readInt();
        int contestLevel = buf.readInt();
        //BlockEntity be = level.getBlockEntity(pos);
        BlockEntity be  = level.getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE); //not sure why getBlockEntity does not work
        //Block blockie = level.getBlockState(pos).getBlock();
        if(be instanceof ContestBlockEntity cbe){
            System.out.println("Should be right entity");
            cbe.runStatAssesment(id, index, contestType, contestLevel);
        }else {
            System.out.println("Nope, wrong entity");
            /**System.out.println(player);
            System.out.println(level);
            System.out.println("ID: " + id + ", Index: " + index + ", Pos: " + pos);
            System.out.println("Type of Block: " + blockie.getName());
            System.out.println("Does have block entity?: " + level.getBlockState(pos).hasBlockEntity());
            System.out.println("Block Entity Attempt #1: " + be);
            System.out.println("The Block Entity at " + pos + " is reading as " + level.getBlockEntity(pos));
            System.out.println("State: " + level.getBlockState(pos));
            System.out.println("Menu Provider: " + level.getBlockState(pos).getMenuProvider(level, pos));
            //System.out.println("Menu instance of CBE: " + (level.getBlockState(pos).getMenuProvider(level, pos) instanceof ContestBlockEntity));
            if(be != null){
                System.out.println("Type: " + be.getType());
            }else {
                System.out.println("Type would cause null pointer error");
            }
            BlockEntity be2 = level.getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE);
            System.out.println("Be2 is: " + be2);*/
        }
    }


}
