package com.raspix.forge.cobble_contests.items;

import com.cobblemon.mod.common.CobblemonBlocks;
import com.cobblemon.mod.common.item.RevivalHerbItem;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.forge.cobble_contests.CobbleContestsForge;
import com.raspix.forge.cobble_contests.blocks.BlockInit;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;

public class ItemInit {

    private ItemInit(){}
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CobbleContestsForge.MOD_ID);

    //public static final DeferredItem<BlockItem> CONTEST_BOOTH = ITEMS.register("contest_booth", () -> new BlockItem(BlockInit.CONTEST_BOOTH.get(), new Item.Properties()));
    //public static final DeferredItem<BlockItem> POFFIN_POT = ITEMS.register("poffin_pot", () -> new BlockItem(BlockInit.POFFIN_POT.get(), new Item.Properties()));

    public static final DeferredItem<Item> POFFIN_DOUGH_BASE = ITEMS.register("poffin_dough_base", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FOUL_POFFIN = ITEMS.register("foul_poffin", () -> new PoffinItem(new Item.Properties(), -1, -1)); //random
    public static final DeferredItem<Item> SPICY_POFFIN = ITEMS.register("spicy_poffin", () -> new PoffinItem(new Item.Properties(), 0, -1)); //spicy/cool
    public static final DeferredItem<Item> SPICY_DRY_POFFIN = ITEMS.register("spicy_dry_poffin", () -> new PoffinItem(new Item.Properties(), 0, 1));
    public static final DeferredItem<Item> SPICY_SWEET_POFFIN = ITEMS.register("spicy_sweet_poffin", () -> new PoffinItem(new Item.Properties(), 0, 2));
    public static final DeferredItem<Item> SPICY_BITTER_POFFIN = ITEMS.register("spicy_bitter_poffin", () -> new PoffinItem(new Item.Properties(), 0, 3));
    public static final DeferredItem<Item> SPICY_SOUR_POFFIN = ITEMS.register("spicy_sour_poffin", () -> new PoffinItem(new Item.Properties(), 0, 4));
    public static final DeferredItem<Item> DRY_POFFIN = ITEMS.register("dry_poffin", () -> new PoffinItem(new Item.Properties(), 1, -1)); //beauty
    public static final DeferredItem<Item> DRY_SPICY_POFFIN = ITEMS.register("dry_spicy_poffin", () -> new PoffinItem(new Item.Properties(), 1, 0));
    public static final DeferredItem<Item> DRY_SWEET_POFFIN = ITEMS.register("dry_sweet_poffin", () -> new PoffinItem(new Item.Properties(), 1, 2));
    public static final DeferredItem<Item> DRY_BITTER_POFFIN = ITEMS.register("dry_bitter_poffin", () -> new PoffinItem(new Item.Properties(), 1, 3));
    public static final DeferredItem<Item> DRY_SOUR_POFFIN = ITEMS.register("dry_sour_poffin", () -> new PoffinItem(new Item.Properties(), 1, 4));
    public static final DeferredItem<Item> SWEET_POFFIN = ITEMS.register("sweet_poffin", () -> new PoffinItem(new Item.Properties(), 2, -1)); //cute
    public static final DeferredItem<Item> SWEET_SPICY_POFFIN = ITEMS.register("sweet_spicy_poffin", () -> new PoffinItem(new Item.Properties(), 2, 0));
    public static final DeferredItem<Item> SWEET_DRY_POFFIN = ITEMS.register("sweet_dry_poffin", () -> new PoffinItem(new Item.Properties(), 2, 1));
    public static final DeferredItem<Item> SWEET_BITTER_POFFIN = ITEMS.register("sweet_bitter_poffin", () -> new PoffinItem(new Item.Properties(), 2, 3));
    public static final DeferredItem<Item> SWEET_SOUR_POFFIN = ITEMS.register("sweet_sour_poffin", () -> new PoffinItem(new Item.Properties(), 2, 4));
    public static final DeferredItem<Item> BITTER_POFFIN = ITEMS.register("bitter_poffin", () -> new PoffinItem(new Item.Properties(), 3, -1)); //smart
    public static final DeferredItem<Item> BITTER_SPICY_POFFIN = ITEMS.register("bitter_spicy_poffin", () -> new PoffinItem(new Item.Properties(), 3, 0));
    public static final DeferredItem<Item> BITTER_DRY_POFFIN = ITEMS.register("bitter_dry_poffin", () -> new PoffinItem(new Item.Properties(), 3, 1));
    public static final DeferredItem<Item> BITTER_SWEET_POFFIN = ITEMS.register("bitter_sweet_poffin", () -> new PoffinItem(new Item.Properties(), 3, 2));
    public static final DeferredItem<Item> BITTER_SOUR_POFFIN = ITEMS.register("bitter_sour_poffin", () -> new PoffinItem(new Item.Properties(), 3, 4));
    public static final DeferredItem<Item> SOUR_POFFIN = ITEMS.register("sour_poffin", () -> new PoffinItem(new Item.Properties(), 4, -1)); //tough
    public static final DeferredItem<Item> SOUR_SPICY_POFFIN = ITEMS.register("sour_spicy_poffin", () -> new PoffinItem(new Item.Properties(), 4, 0));
    public static final DeferredItem<Item> SOUR_DRY_POFFIN = ITEMS.register("sour_dry_poffin", () -> new PoffinItem(new Item.Properties(), 4, 1));
    public static final DeferredItem<Item> SOUR_SWEET_POFFIN = ITEMS.register("sour_sweet_poffin", () -> new PoffinItem(new Item.Properties(), 4, 2));
    public static final DeferredItem<Item> SOUR_BITTER_POFFIN = ITEMS.register("sour_bitter_poffin", () -> new PoffinItem(new Item.Properties(), 4, 3));


    //public static final DeferredItem<Item> POFFIN_CASE = ITEMS.register("poffin_case", () -> new PoffinItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CONTEST_CARD = ITEMS.register("contest_card", () -> new ContestWallet(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BALL_SWAPPER = ITEMS.register("ball_swapper", () -> new BallSwapper(new Item.Properties().stacksTo(1)));

    public static final Supplier<CreativeModeTab> EXAMPLE_TAB = CobbleContestsForge.CREATIVE_MODE_TABS.register("cobble_contests_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("creativetab.cobble_contests"))
            .icon(() -> CONTEST_CARD.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(BlockInit.CONTEST_BOOTH);

                output.accept(CONTEST_CARD.get());
                output.accept(BALL_SWAPPER.get());
                output.accept(BlockInit.POFFIN_POT.get());
                //output.accept(POFFIN_CASE.get());
                output.accept(POFFIN_DOUGH_BASE.get());
                output.accept(FOUL_POFFIN.get());
                output.accept(SPICY_POFFIN.get());
                output.accept(SPICY_DRY_POFFIN.get());
                output.accept(SPICY_SWEET_POFFIN.get());
                output.accept(SPICY_BITTER_POFFIN.get());
                output.accept(SPICY_SOUR_POFFIN.get());
                output.accept(DRY_POFFIN.get());
                output.accept(DRY_SPICY_POFFIN.get());
                output.accept(DRY_SWEET_POFFIN.get());
                output.accept(DRY_BITTER_POFFIN.get());
                output.accept(DRY_SOUR_POFFIN.get());
                output.accept(SWEET_POFFIN.get());
                output.accept(SWEET_SPICY_POFFIN.get());
                output.accept(SWEET_DRY_POFFIN.get());
                output.accept(SWEET_BITTER_POFFIN.get());
                output.accept(SWEET_SOUR_POFFIN.get());
                output.accept(BITTER_POFFIN.get());
                output.accept(BITTER_SPICY_POFFIN.get());
                output.accept(BITTER_DRY_POFFIN.get());
                output.accept(BITTER_SWEET_POFFIN.get());
                output.accept(BITTER_SOUR_POFFIN.get());
                output.accept(SOUR_POFFIN.get());
                output.accept(SOUR_SPICY_POFFIN.get());
                output.accept(SOUR_DRY_POFFIN.get());
                output.accept(SOUR_SWEET_POFFIN.get());
                output.accept(SOUR_BITTER_POFFIN.get());

            }).build());
}
