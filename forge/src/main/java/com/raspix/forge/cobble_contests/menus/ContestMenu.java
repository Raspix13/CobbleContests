package com.raspix.forge.cobble_contests.menus;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.raspix.forge.cobble_contests.blocks.BlockInit;
import com.raspix.forge.cobble_contests.blocks.entity.ContestBlockEntity;
import com.raspix.forge.cobble_contests.network.CBERunContest;
import com.raspix.forge.cobble_contests.network.PacketHandler;
import com.raspix.forge.cobble_contests.network.SBInfoScreenParty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class ContestMenu extends AbstractContainerMenu {

    private final ContestBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private PlayerPartyStore playerPartyStore;
    private ClientParty playerPartyClient;

    //server constructor
    public ContestMenu(int containerID, Inventory playerInv, BlockEntity blockEntity){
        super(MenuInit.CONTEST_MENU.get(), containerID);
        if(blockEntity instanceof ContestBlockEntity be){
            this.blockEntity = be;
        }else {
            throw new IllegalStateException("Incorrect block entity class (%s) passed into ContestMenu"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        //try {
            UUID id = playerInv.player.getUUID();
            //playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(id);
            playerPartyClient = CobblemonClient.INSTANCE.getStorage().getMyParty();
        /**}catch (NoPokemonStoreException e){
            System.out.println("you failed");
        }*/
        if(playerPartyClient == null){
            System.out.println("is null");
        }else {
            System.out.println("not null");
        }


        //System.out.println("hewwo");
        createTestMenu();

    }

    private void createTestMenu() {


    }

    //client constructor
    protected ContestMenu(int containerID, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerID, playerInv, playerInv.player.level().getBlockEntity(additionalData.readBlockPos()));
    }

    @Override
    public ItemStack quickMoveStack(Player arg, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.levelAccess, player, BlockInit.CONTEST_BLOCK.get());
    }

    /**public ContestBlockEntity getBlockEntity() {
        return blockEntity;
    }*/

    public boolean playerStartHosting(UUID id){
        return this.blockEntity.tryHosting(id);
    }

    public boolean hostSelectType(UUID id, int type){
        return this.blockEntity.setContestType(id, type);
    }

    public PlayerPartyStore getPartyStore(){
        return playerPartyStore;
    }

    public ClientParty getPartyClient(){
        return playerPartyClient;
    }

    public void startContest(int color, int pokemonIdx, UUID player){

        this.blockEntity.tryHosting(player);
        this.blockEntity.startContest(color, player);
        this.blockEntity.addContestant(player, pokemonIdx);
        System.out.println(this.blockEntity.getCurrentContestInfo());
    }

    public String getContestResults(){
        return this.blockEntity.getContestResults();
    }

    public void startStatAssesment(UUID player, int pokemonIdx, int contestType, int contestLevel){
        PacketHandler.sendToServer(new CBERunContest(player, pokemonIdx, blockEntity.getBlockPos(), contestType, contestLevel));
    }
}
