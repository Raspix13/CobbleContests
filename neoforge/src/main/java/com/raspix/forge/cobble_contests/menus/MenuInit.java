package com.raspix.forge.cobble_contests.menus;

import com.raspix.common.cobble_contests.CobbleContests;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
//import net.minecraftforge.common.extensions.IForgeMenuType;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;

public class MenuInit {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CobbleContests.MOD_ID);

    public static final RegistryObject<MenuType<ContestMenu>> CONTEST_MENU = MENU_TYPES.register("contest_menu", () -> IForgeMenuType.create(ContestMenu::new));
    public static final RegistryObject<MenuType<PoffinPotMenu>> POFFIN_POT_MENU = MENU_TYPES.register("poffin_pot_menu", () -> IForgeMenuType.create(PoffinPotMenu::new));
    public static final RegistryObject<MenuType<PlayerContestInfoMenu>> PLAYER_CONTEST_INFO_MENU = MENU_TYPES.register("player_contest_info_menu", () -> IForgeMenuType.create(PlayerContestInfoMenu::new));

}
