package com.raspix.forge.cobble_contests.items;

import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.raspix.forge.cobble_contests.menus.PlayerContestInfoMenu;
import com.raspix.forge.cobble_contests.network.PacketHandler;
import com.raspix.forge.cobble_contests.network.CBInfoScreenParty;
import com.raspix.forge.cobble_contests.network.SBInfoScreenParty;
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
import net.minecraftforge.network.NetworkHooks;

public class ContestWallet extends Item {
    public ContestWallet(Properties arg) {
        super(arg);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.openItemGui(itemstack, pHand);
        if(!pLevel.isClientSide()){

            NetworkHooks.openScreen((ServerPlayer) pPlayer, getMenuProvider(pPlayer));
            //PacketHandler.sendToClient(new CBInfoScreenParty(pPlayer.getUUID()), (() -> (ServerPlayer) pPlayer));

        }else {

        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    public MenuProvider getMenuProvider(Player player1) {
        PlayerPartyStore playerPartyStore = null;
        System.out.println("test 2");
        /**try {
            playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player1.getUUID());
            System.out.println("test 3");
        }catch (NoPokemonStoreException e){
            System.out.println("Oopsie tootsie");
        }*/
        return new SimpleMenuProvider((containerId, playerInventory, player) -> new PlayerContestInfoMenu(containerId, playerInventory), this.getDisplayName()); //
    }

    public Component getDisplayName() {
        return Component.translatable("Contest Info");
        /**return PlayerTeam.formatNameForTeam(this.getTeam(), this.getName()).withStyle((p_185975_) -> {
         return p_185975_.withHoverEvent(this.createHoverEvent()).withInsertion(this.getStringUUID());
         });*/
    }

}
