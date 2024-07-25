package com.raspix.forge.cobble_contests.blocks.entity;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.forge.cobble_contests.menus.ContestMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ContestBlockEntity extends BlockEntity implements MenuProvider {

    private static final Component TITLE = Component.translatable("container." + CobbleContests.MOD_ID + ".contest_block");

    private boolean isHost;
    private UUID hostId;
    private int contestType;
    private Map<UUID, ContestParticipation> participants;



    public ContestBlockEntity(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

    public ContestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.CONTEST_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, @NotNull Inventory playerInv, Player player) {
        return new ContestMenu(containerID, playerInv, this);
    }

    public boolean tryHosting(UUID id){
        if(!isHost){
            this.hostId = id;
            this.isHost = true;
            return true;
        }else {
            return false;
        }
    }

    public boolean setContestType(UUID id, int type){
        if(this.isHost && this.hostId == id){
            contestType = type;
            return true;
        }else if(!this.isHost){
            tryHosting(id);
            contestType = type;
            return true;
        }else {
            return false;
        }
    }

    private void removeHost(){
        this.hostId = null;
        this.isHost = false;
    }

    public void startContest(int color, UUID player){

    }

    public void addContestant(UUID player, int pokemonIdx){

    }

    public String getContestResults(){
        return "cobble_contests.contest_type.cool";
    }

    public void clearContest(){
        participants.clear();
        hostId = null;
        isHost = false;
        contestType = 0;
    }

    public void kickContestent(UUID id){
        if(participants.containsKey(id)){
            participants.remove(id);
        }
    }


    public class ContestParticipation{
        private UUID id;
        private int pokemonIdx;

        public ContestParticipation(UUID id, int pokemonIdx){
            this.id = id;
            this.pokemonIdx = pokemonIdx;
        }
    }

    public String getCurrentContestInfo(){
        String result = "";
        if(isHost){
            result += "Host: " + Minecraft.getInstance().level.getPlayerByUUID(hostId).getDisplayName().getString();
            result += ", Type: " + contestType;
        }else {
            result += "No current host";
        }
        return result;
    }

    public void runStubContest(){
        assert Minecraft.getInstance().level != null;
        if (!Minecraft.getInstance().level.isClientSide){
            System.out.println("Running contest");
        }
    }


}
