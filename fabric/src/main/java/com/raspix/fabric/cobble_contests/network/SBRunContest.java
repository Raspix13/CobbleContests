package com.raspix.fabric.cobble_contests.network;

import com.raspix.fabric.cobble_contests.blocks.entity.ContestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SBRunContest implements CustomPacketPayload {

    public final UUID id;
    public final int index;
    public final BlockPos pos;
    public final int contestType;
    public final int contestLevel;



    public static final CustomPacketPayload.Type<SBRunContest> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.RUN_CONTEST);
    public static final StreamCodec<FriendlyByteBuf, SBRunContest> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, SBRunContest>() {
        @Override
        public @NotNull SBRunContest decode(FriendlyByteBuf buf) {
            CompoundTag compoundTag = new CompoundTag();
            return new SBRunContest(FriendlyByteBuf.readUUID(buf), buf.readInt(), FriendlyByteBuf.readBlockPos(buf), buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(FriendlyByteBuf buf, SBRunContest payload) {
            FriendlyByteBuf.writeUUID(buf, payload.getId());
            buf.writeInt(payload.getIndex());
            FriendlyByteBuf.writeBlockPos(buf, payload.getPos());
            buf.writeInt(payload.getContestType());
            buf.writeInt(payload.getContestLevel());
        }
    };

    public UUID getId(){
        return id;
    }

    public int getIndex(){
        return index;
    }

    public BlockPos getPos(){
        return pos;
    }

    public int getContestType(){
        return contestType;
    }

    public int getContestLevel(){
        return contestLevel;
    }

    public SBRunContest(UUID id, int index, BlockPos pos, int contestType, int contestLevel){
        this.id = id;
        this.index = index;
        this.pos = pos;
        this.contestType = contestType;
        this.contestLevel = contestLevel;
    }

    public SBRunContest(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readInt(), buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeUUID(this.id);
        buf.writeInt(this.index);
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.contestType);
        buf.writeInt(this.contestLevel);
    }

    public static SBRunContest decode(FriendlyByteBuf buf) {
        return new SBRunContest(buf.readUUID(), buf.readInt(), buf.readBlockPos(), buf.readInt(), buf.readInt());
    }


    public void recieve(MinecraftServer server, Player player) {
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
        UUID id = getId();
        int index = getIndex();
        BlockPos pos = getPos();
        int contestType = getContestType();
        int contestLevel = getContestLevel();
        //BlockEntity be = level.getBlockEntity(pos);
        BlockEntity be  = level.getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE); //not sure why getBlockEntity does not work
        //Block blockie = level.getBlockState(pos).getBlock();
        if(be instanceof ContestBlockEntity cbe){
            System.out.println("Should be right entity");
            cbe.runStatAssesment(id, index, contestType, contestLevel, (ServerPlayer) player);
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


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
