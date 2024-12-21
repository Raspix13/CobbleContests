package com.raspix.fabric.cobble_contests.menus;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import com.raspix.fabric.cobble_contests.menus.screens.PlayerContestInfoScreen;
import com.raspix.fabric.cobble_contests.menus.screens.PoffinPotScreen;
import com.raspix.fabric.cobble_contests.menus.screens.SecondTestScreen;
import com.raspix.fabric.cobble_contests.network.BlockPosPayload;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.awt.*;

public class MenuInit {

    //public static final MenuType POFFIN_POT_MENU = registerMenu("poffin_pot_menu", new MenuType<>(() -> PoffinPotMenu::new));


    public static final MenuType<ContestMenu> CONTEST_MENU = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contest_menu"), new ExtendedScreenHandlerType<>(ContestMenu::new, BlockPosPayload.PACKET_CODEC)); // new MenuType(ContestMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final MenuType<PoffinPotMenu> POFFIN_POT_MENU = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "poffin_pot_menu"), new MenuType(PoffinPotMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final MenuType<PlayerContestInfoMenu> PLAYER_CONTEST_INFO_MENU = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "player_contest_info_menu"), new MenuType(PlayerContestInfoMenu::new, FeatureFlags.DEFAULT_FLAGS));

    //public static final MenuType<PoffinPotMenu> POFFIN_POT_MENU = registerMenu(new ResourceLocation(CobbleContestsFabric.MOD_ID, "poffin_pot_menu"), new MenuType<>(PoffinPotMenu::new, FeatureFlags.DEFAULT_FLAGS));
    //public static final MenuType POFFIN_POT_MENU = Registry.register(Registries.MENU, new ResourceLocation(CobbleContestsFabric.MOD_ID, "poffin_pot_menu"), new MenuType<>(PoffinPotMenu::new));

    private static MenuType registerMenu(String name, MenuType menu) {
        return Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, name), menu);
    }

    //public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(fabricRegistries.MENU_TYPES, CobbleContests.MOD_ID);

    //public static final RegistryObject<MenuType<ContestMenu>> CONTEST_MENU = MENU_TYPES.register("contest_menu", () -> IfabricMenuType.create(ContestMenu::new));
    //public static final RegistryObject<MenuType<PoffinPotMenu>> POFFIN_POT_MENU = MENU_TYPES.register("poffin_pot_menu", () -> IfabricMenuType.create(PoffinPotMenu::new));
    //public static final RegistryObject<MenuType<PlayerContestInfoMenu>> PLAYER_CONTEST_INFO_MENU = MENU_TYPES.register("player_contest_info_menu", () -> IfabricMenuType.create(PlayerContestInfoMenu::new));

    public static void registerMenus() {
        CobbleContestsFabric.LOGGER.info("Registering Menus for " + CobbleContestsFabric.MOD_ID);
        //registerScreens();
    }

    public static void registerScreens(){
        MenuScreens.register(MenuInit.POFFIN_POT_MENU, PoffinPotScreen::new);
        MenuScreens.register(MenuInit.PLAYER_CONTEST_INFO_MENU, PlayerContestInfoScreen::new);
        MenuScreens.register(MenuInit.CONTEST_MENU, SecondTestScreen::new);
    }

}
