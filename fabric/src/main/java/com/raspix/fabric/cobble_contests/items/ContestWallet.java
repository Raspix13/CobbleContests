package com.raspix.fabric.cobble_contests.items;

import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
//import com.raspix.fabric.cobble_contests.menus.PlayerContestInfoMenu;
//import com.raspix.fabric.cobble_contests.blocks.entity.PoffinPotBlockEntity;
//import com.raspix.fabric.cobble_contests.menus.PlayerContestInfoMenu;
import com.raspix.fabric.cobble_contests.menus.PlayerContestInfoMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ContestWallet extends Item {
    public ContestWallet(Properties arg) {
        super(arg);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.openItemGui(itemstack, pHand);
        if(!pLevel.isClientSide()){
            //NetworkHooks.openScreen((ServerPlayer) pPlayer, getMenuProvider(pPlayer));
            pPlayer.openMenu(getMenuProvider(pPlayer));
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    public MenuProvider getMenuProvider(Player player1) {
        PlayerPartyStore playerPartyStore = null;
        return new SimpleMenuProvider((containerId, playerInventory, player) -> new PlayerContestInfoMenu(containerId, playerInventory), this.getDisplayName()); //
    }

    public Component getDisplayName() {
        return Component.translatable("Contest Info");
    }

}
