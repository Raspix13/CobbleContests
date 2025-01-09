package com.raspix.fabric.cobble_contests.menus;

import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.raspix.fabric.cobble_contests.blocks.BlockInit;
import com.raspix.fabric.cobble_contests.blocks.entity.ContestBlockEntity;
//import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.network.BlockPosPayload;
import com.raspix.fabric.cobble_contests.network.SBRunContest;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class ContestBoothMenu extends AbstractContainerMenu {

    private ContestBlockEntity blockEntity;
    private ContainerLevelAccess levelAccess;
    private PlayerPartyStore playerPartyStore;
    private ClientParty playerPartyClient;

    //server constructor
    public ContestBoothMenu(int containerID, Inventory playerInv, BlockEntity blockEntity){
        super(MenuInit.CONTEST_BOOTH_MENU, containerID); //MenuInit.CONTEST_MENU
        if(blockEntity instanceof ContestBlockEntity be){
            this.blockEntity = be;
        }else {
            throw new IllegalStateException("Incorrect block entity class (%s) passed into ContestMenu"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        UUID id = playerInv.player.getUUID();
        if(playerInv.player.level().isClientSide()){
            playerPartyClient = CobblemonClient.INSTANCE.getStorage().getMyParty();
        }


    }

    public ContestBoothMenu(int containerID, Inventory playerInv, BlockPosPayload payload) {
        this(containerID, playerInv, (ContestBlockEntity) playerInv.player.level().getBlockEntity(payload.pos()));
        /**super(MenuInit.CONTEST_MENU, containerID); //MenuInit.CONTEST_MENU
        UUID id = playerInv.player.getUUID();
        playerPartyClient = CobblemonClient.INSTANCE.getStorage().getMyParty();
        if(o == null){
            System.out.println("o in ContestMenu is null");
        }else{
            System.out.println("o in ContestMenu is not null");
            System.out.println(o);
        }*/
    }

    public ContestBoothMenu(int containerID, Inventory playerInv) {
        super(MenuInit.CONTEST_BOOTH_MENU, containerID); //MenuInit.CONTEST_MENU
        UUID id = playerInv.player.getUUID();
        playerPartyClient = CobblemonClient.INSTANCE.getStorage().getMyParty();

    }

    public void SetBlockEntity(ContestBlockEntity be){
        System.out.println("Setting block entity");
        blockEntity = be;
        levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        playerPartyClient = CobblemonClient.INSTANCE.getStorage().getMyParty();
    }

    public ContestBoothMenu(int containerID, Inventory playerInv, PacketByteBufs bytes) {
        this(containerID, playerInv, (BlockEntity) null);
    }


    public static ContestBoothMenu MenuFromBlockEntity(int containerID, Inventory playerInv, BlockEntity blockEntity){
        return new ContestBoothMenu(containerID, playerInv, blockEntity);
    }


    //client constructor
    protected ContestBoothMenu(int containerID, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerID, playerInv, playerInv.player.level().getBlockEntity(additionalData.readBlockPos()));
    }

    @Override
    public ItemStack quickMoveStack(Player arg, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.levelAccess, player, BlockInit.CONTEST_BOOTH);
    }

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

    public void startStatAssesment(Player player, UUID playerID, int pokemonIdx, int contestType){
        System.out.println("should be starting stat assesment");
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(playerID);
        buf.writeInt(pokemonIdx);
        buf.writeBlockPos(blockEntity.getBlockPos());
        buf.writeInt(contestType);
        buf.writeInt(0);
        ClientPlayNetworking.send(new SBRunContest(buf));
    }

}
