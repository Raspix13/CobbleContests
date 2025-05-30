package com.raspix.fabric.cobble_contests.menus;

import com.raspix.fabric.cobble_contests.util.Contest;
import com.raspix.fabric.cobble_contests.util.ContestManager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ContestMenu extends AbstractContainerMenu {


    public ContestMenu(int containerID, Inventory playerInv) {
        super(MenuInit.CONTEST_MENU, containerID);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public Contest getJoinedContest(UUID playerID){
        return ContestManager.INSTANCE.getPlayersContest(playerID);
    }

    public boolean isHostingContest(UUID playerId){
        Contest con = getJoinedContest(playerId);
        if(con == null){
            System.out.println("can not host a missing contest");
            return false;
        }

        return con.isPlayerHost(playerId);
    }
}
