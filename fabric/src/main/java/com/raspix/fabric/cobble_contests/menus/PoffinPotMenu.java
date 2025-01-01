package com.raspix.fabric.cobble_contests.menus;

import com.cobblemon.mod.common.api.tags.CobblemonItemTags;
import com.raspix.fabric.cobble_contests.blocks.BlockInit;
import com.raspix.fabric.cobble_contests.blocks.entity.PoffinPotBlockEntity;
import com.raspix.fabric.cobble_contests.items.ItemInit;
import com.raspix.fabric.cobble_contests.menus.MenuInit;
import com.raspix.fabric.cobble_contests.util.TagsInit;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PoffinPotMenu extends AbstractContainerMenu {
    //private final PoffinPotBlockEntity blockEntity;

    //private final ContainerLevelAccess levelAccess;

    private final Container container;
    private final ContainerData containerData;
    private Slot ingredientSlot;
    //private final ContainerLevelAccess levelAccess;

    public PoffinPotMenu(int containerID, Inventory playerInv, FriendlyByteBuf additionalData){
        this(containerID, playerInv, playerInv.player.level().getBlockEntity(additionalData.readBlockPos()), new SimpleContainer(7), new SimpleContainerData(4));
    }

    public PoffinPotMenu(int containerID, Inventory playerInv, BlockEntity blockEntity, Container cont, ContainerData poffinPotData) {
        //super(MenuInit.POFFIN_POT_MENU, containerID);
        super(MenuInit.POFFIN_POT_MENU, containerID);
        System.out.println("At the pot menu");

        if(blockEntity instanceof PoffinPotBlockEntity be){
            //this.blockEntity = be;
        }else if(blockEntity == null){
            System.out.println("Warning: Not sure where this will matter, but the block entity for poffinpotmenu is null");
        }else {
            throw new IllegalStateException("Incorrect block entity class (%s) passed into PoffinPotMenu"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }
        //this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        //this.container = new SimpleContainer(7);
        this.container = cont;
        this.containerData = poffinPotData;
        //this.containerData = new SimpleContainerData(2);
        checkContainerSize(container, 7);
        checkContainerDataCount(containerData, 4);


        createPoffinPotMenu(playerInv);
    }

    public PoffinPotMenu(int id, Inventory inventory) {
        this(id, inventory, null, new SimpleContainer(7), new SimpleContainerData(4));
        //super(null, i);
    }

    private void createPoffinPotMenu(Inventory playerInv) {
        this.addSlot(new BerrySlot(container, 0, 48, 22)); //berry slots 1-4
        this.addSlot(new BerrySlot(container, 1, 69, 8));
        this.addSlot(new BerrySlot(container, 2, 91, 8));
        this.addSlot(new BerrySlot(container, 3, 112, 22));
        this.ingredientSlot = this.addSlot(new IngredientsSlot(container, 4, 80, 40)); //dough slot
        this.addSlot(new FuelSlot(container, 5, 80, 70)); //fuel slot
        this.addSlot(new PoffinSlot(container, 6, 149, 40)); //result slot
        this.addDataSlots(containerData);

        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + k * 9 + 9, 8 + j * 18, 94 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 152));
        }

    }

    @Override
    public ItemStack quickMoveStack(Player arg, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(i);
        System.out.println("Index is: " + i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if((i < 0 || i > 6)) { //player slots to pot
                if (BerrySlot.mayPlaceItem(itemstack)) { //berry slots
                    if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.ingredientSlot.mayPlace(itemstack)) { // dough
                    if (!this.moveItemStackTo(itemstack1, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (FuelSlot.mayPlaceItem(itemstack)) { //fuel
                    if (!this.moveItemStackTo(itemstack1, 5, 6, false)) {
                        return ItemStack.EMPTY;
                    }
                /**} else if (PoffinSlot.mayPlaceItem(itemstack)) { //result poffin
                    if (!this.moveItemStackTo(itemstack1, 6, 7, false)) {
                        return ItemStack.EMPTY;
                    }*/
                }else if (i >= 7 && i < 34) { //result poffin
                    if (!this.moveItemStackTo(itemstack1, 34, 43, false)) {
                        return ItemStack.EMPTY;
                    }
                }else if (i >= 34 && i < 42) { //result poffin
                    if (!this.moveItemStackTo(itemstack1, 7, 34, false)) {
                        return ItemStack.EMPTY;
                    }
                }else if (!this.moveItemStackTo(itemstack1, 7, 43, false)) {
                    return ItemStack.EMPTY;
                }

            }else{ //pot slots to player
                if (!this.moveItemStackTo(itemstack1, 7, 42, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(arg, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
        //return stillValid(this.levelAccess, player, BlockInit.POFFIN_POT);
    }


    /**public int getFuel() {
        return this.brewingStandData.get(1);
    }

    public int getBrewingTicks() {
        return this.brewingStandData.get(0);
    }*/

    static class PoffinSlot extends Slot {
        public PoffinSlot(Container arg, int i, int j, int k) {
            super(arg, i, j, k);
        }

        public boolean mayPlace(ItemStack arg) {
            return false;//mayPlaceItem(arg);
        }

        public int getMaxStackSize() {
            return 1;
        }

        public void onTake(Player arg, ItemStack arg2) {
            //Potion potion = PotionUtils.getPotion(arg2);
            if (arg instanceof ServerPlayer) {
                //fabricEventFactory.onPlayerBrewedPotion(arg, arg2);
                //CriteriaTriggers.BREWED_POTION.trigger((ServerPlayer)arg, potion);
            }

            super.onTake(arg, arg2);
        }

        public static boolean mayPlaceItem(ItemStack arg) {
            return true;
        }
    }

    static class BerrySlot extends Slot {
        public BerrySlot(Container arg, int i, int j, int k) {
            super(arg, i, j, k);
        }

        public boolean mayPlace(ItemStack arg) {
            return arg.is(TagsInit.Items.COBBLEMON_BERRIES);
        }

        public int getMaxStackSize() {
            return 64;
        }

        public static boolean mayPlaceItem(ItemStack arg) {
            return arg.is(TagsInit.Items.COBBLEMON_BERRIES);
        }
    }

    static class IngredientsSlot extends Slot {
        public IngredientsSlot(Container arg, int i, int j, int k) {
            super(arg, i, j, k);
        }

        public boolean mayPlace(ItemStack arg) {
            return arg.is(ItemInit.POFFIN_DOUGH_BASE);
        }

        public int getMaxStackSize() {
            return 64;
        }
    }



    static class FuelSlot extends Slot {
        public FuelSlot(Container arg, int i, int j, int k) {
            super(arg, i, j, k);

        }

        protected static boolean isFuel(ItemStack arg) {
            return AbstractFurnaceBlockEntity.isFuel(arg);//ForgeHooks.getBurnTime(arg, this.recipeType) > 0;//FuelRegistry.INSTANCE.get(arg);//fabricHooks.getBurnTime(arg, RecipeType.SMELTING) > 0;
        }

        public boolean mayPlace(ItemStack arg) {
            return mayPlaceItem(arg);
        }

        public static boolean mayPlaceItem(ItemStack arg) {
            return isFuel(arg);
        }

        public int getMaxStackSize() {
            return 64;
        }
    }

    public int getLitProgress() {
        int i = this.containerData.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.containerData.get(0) * 15 / i;
    }

    public boolean isLit() {
        return this.containerData.get(0) > 0;
    }

    public int getBurnProgress() {
        int i = this.containerData.get(2);
        int j = this.containerData.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }


}
