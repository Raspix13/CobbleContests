package com.raspix.fabric.cobble_contests.items;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import com.raspix.fabric.cobble_contests.blocks.BlockInit;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.item.Items.registerItem;

public class ItemInit {

    public static final Item CONTEST_BOOTH = registerItemBlock("contest_booth", BlockInit.CONTEST_BOOTH);
    public static final Item POFFIN_POT = registerItemBlock("poffin_pot", BlockInit.POFFIN_POT);


    public static final Item POFFIN_DOUGH_BASE = registerItem("poffin_dough_base");
    public static final Item FOUL_POFFIN = registerPoffinItem("foul_poffin", -1, -1);

    public static final Item SPICY_POFFIN = registerPoffinItem("spicy_poffin", 0, -1); //spicy/cool
    public static final Item SPICY_DRY_POFFIN = registerPoffinItem("spicy_dry_poffin", 0, 1);
    public static final Item SPICY_SWEET_POFFIN = registerPoffinItem("spicy_sweet_poffin", 0, 2);
    public static final Item SPICY_BITTER_POFFIN = registerPoffinItem("spicy_bitter_poffin", 0, 3);
    public static final Item SPICY_SOUR_POFFIN = registerPoffinItem("spicy_sour_poffin", 0, 4);
    public static final Item DRY_POFFIN = registerPoffinItem("dry_poffin", 1, -1); //beauty
    public static final Item DRY_SPICY_POFFIN = registerPoffinItem("dry_spicy_poffin", 1, 0);
    public static final Item DRY_SWEET_POFFIN = registerPoffinItem("dry_sweet_poffin", 1, 2);
    public static final Item DRY_BITTER_POFFIN = registerPoffinItem("dry_bitter_poffin", 1, 3);
    public static final Item DRY_SOUR_POFFIN = registerPoffinItem("dry_sour_poffin", 1, 4);
    public static final Item SWEET_POFFIN = registerPoffinItem("sweet_poffin", 2, -1); //cute
    public static final Item SWEET_SPICY_POFFIN = registerPoffinItem("sweet_spicy_poffin", 2, 0);
    public static final Item SWEET_DRY_POFFIN = registerPoffinItem("sweet_dry_poffin", 2, 1);
    public static final Item SWEET_BITTER_POFFIN = registerPoffinItem("sweet_bitter_poffin", 2, 3);
    public static final Item SWEET_SOUR_POFFIN = registerPoffinItem("sweet_sour_poffin", 2, 4);
    public static final Item BITTER_POFFIN = registerPoffinItem("bitter_poffin", 3, -1); //smart
    public static final Item BITTER_SPICY_POFFIN = registerPoffinItem("bitter_spicy_poffin", 3, 0);
    public static final Item BITTER_DRY_POFFIN = registerPoffinItem("bitter_dry_poffin", 3, 1);
    public static final Item BITTER_SWEET_POFFIN = registerPoffinItem("bitter_sweet_poffin", 3, 2);
    public static final Item BITTER_SOUR_POFFIN = registerPoffinItem("bitter_sour_poffin", 3, 4);
    public static final Item SOUR_POFFIN = registerPoffinItem("sour_poffin", 4, -1); //tough
    public static final Item SOUR_SPICY_POFFIN = registerPoffinItem("sour_spicy_poffin", 4, 0);
    public static final Item SOUR_DRY_POFFIN = registerPoffinItem("sour_dry_poffin", 4, 1);
    public static final Item SOUR_SWEET_POFFIN = registerPoffinItem("sour_sweet_poffin", 4, 2);
    public static final Item SOUR_BITTER_POFFIN = registerPoffinItem("sour_bitter_poffin", 4, 3);

    public static final Item CONTEST_CARD = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CobbleContestsFabric.MOD_ID, "contest_card"), new Item(new Item.Properties().stacksTo(1)));
    public static final Item BALL_SWAPPER = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CobbleContestsFabric.MOD_ID,"ball_swapper"), new BallSwapper(new Item.Properties().stacksTo(1)));

    @SuppressWarnings(value = "unused")
    public static final CreativeModeTab POKENAV_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(CobbleContestsFabric.MOD_ID, "cobble_contests_tab"),
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                    .icon(() -> new ItemStack(CONTEST_CARD))
                    .title(Component.translatable("creativetab.cobble_contests"))
                    .displayItems(((displayContext, entries) -> {
                        entries.accept(CONTEST_BOOTH);
                        entries.accept(CONTEST_CARD);
                        entries.accept(BALL_SWAPPER);
                        entries.accept(POFFIN_POT);
                        entries.accept(POFFIN_DOUGH_BASE);
                        entries.accept(FOUL_POFFIN);
                        entries.accept(SPICY_POFFIN);
                        entries.accept(SPICY_DRY_POFFIN);
                        entries.accept(SPICY_SWEET_POFFIN);
                        entries.accept(SPICY_BITTER_POFFIN);
                        entries.accept(SPICY_SOUR_POFFIN);
                        entries.accept(DRY_POFFIN);
                        entries.accept(DRY_SPICY_POFFIN);
                        entries.accept(DRY_SWEET_POFFIN);
                        entries.accept(DRY_BITTER_POFFIN);
                        entries.accept(DRY_SOUR_POFFIN);
                        entries.accept(SWEET_POFFIN);
                        entries.accept(SWEET_SPICY_POFFIN);
                        entries.accept(SWEET_DRY_POFFIN);
                        entries.accept(SWEET_BITTER_POFFIN);
                        entries.accept(SWEET_SOUR_POFFIN);
                        entries.accept(BITTER_POFFIN);
                        entries.accept(BITTER_SPICY_POFFIN);
                        entries.accept(BITTER_DRY_POFFIN);
                        entries.accept(BITTER_SWEET_POFFIN);
                        entries.accept(BITTER_SOUR_POFFIN);
                        entries.accept(SOUR_POFFIN);
                        entries.accept(SOUR_SPICY_POFFIN);
                        entries.accept(SOUR_DRY_POFFIN);
                        entries.accept(SOUR_SWEET_POFFIN);
                        entries.accept(SOUR_BITTER_POFFIN);
                    }))
                    .build());

    private static Item registerItem(@Nullable String type) {
        return registerItem(type,
                new Item(new Item.Properties().stacksTo(1)));
    }

    private static Item registerItemBlock(@Nullable String type, Block block) {
        return registerItem(type,
                new ItemNameBlockItem(block, new Item.Properties()));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CobbleContestsFabric.MOD_ID, name), item);
    }

    private static Item registerPoffinItem(String name, int mainF, int secF) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CobbleContestsFabric.MOD_ID, name), new PoffinItem(new Item.Properties()));
    }

    public static void registerItems() {
        CobbleContestsFabric.LOGGER.info("Registering items for " + CobbleContestsFabric.MOD_ID);
    }

}
