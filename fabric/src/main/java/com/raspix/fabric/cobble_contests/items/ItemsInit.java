package com.raspix.fabric.cobble_contests.items;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.intellij.lang.annotations.Identifier;

import static net.minecraft.world.item.Items.registerItem;

public class ItemsInit {

    public static final Item CONTEST_CARD = registerItem("pokenav_item" + "_" + "contest_card",
            new Item(new Item.Properties().stacksTo(1)));


    /**private static void addItemsToIngredientItemGroup(Cr entries) {
        entries.add(RUBY);
        entries.add(RAW_RUBY);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(TutorialMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        TutorialMod.LOGGER.info("Registering Mod Items for " + TutorialMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
    @SuppressWarnings(value = "unused")
    public static final CreativeModeTab POKENAV_GROUP = Registry.register(Registries.CREATIVE_MODE_TAB, CobbleContestsFabric.MOD_ID).
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(CONTEST_CARD))
                    .displayName(Component.translatable("itemGroup.cobblenav.pokenav_group"))
                    .entries(((displayContext, output) -> {
                        output.add(CONTEST_CARD);
                    }))
                    .build());

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CobbleContestsForge.CREATIVE_MODE_TABS.register("cobble_contests_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("creativetab.cobble_contests"))
            .icon(() -> CONTEST_CARD.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(CONTEST_CARD.get());

            }).build());
    /**public static final Item RUBY = registerItem("ruby", new Item(new FabricItemSettings()));
    public static final Item RAW_RUBY = registerItem("raw_ruby", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(RUBY);
        entries.add(RAW_RUBY);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(CobbleContests.MOD_ID, name), item);
    }

    public static void registerModItems() {
        CobbleContests.LOGGER.info("Registering Mod Items for " + CobbleContests.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }*/

}
