package com.raspix.fabric.cobble_contests;

import com.raspix.common.cobble_contests.ExampleCommandRegistry;
import com.raspix.fabric.cobble_contests.blocks.BlockInit;
import com.raspix.fabric.cobble_contests.blocks.entity.BlockEntityInit;
import com.raspix.fabric.cobble_contests.items.ItemInit;
import com.raspix.fabric.cobble_contests.menus.MenuInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobbleContestsFabric implements ModInitializer {
    public static final String MOD_ID = "cobble_contests";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**public static IConfig CONFIG;
    public static AbstractNetworkHandler NETWORK;

    public static void init() {
        NETWORK.registerMessagesServer();
    }

    public static void initClient() {
        InteractionGuiHandler.init();
    }*/
    @Override
    public void onInitialize() {
        //CobbleContests.init();
        CommandRegistrationCallback.EVENT.register(ExampleCommandRegistry::registerCommands);
        BlockEntityInit.registerBlockEntities();
        ItemInit.registerItems();
        BlockInit.registerBlocks();
        MenuInit.registerMenus();

    }



    public void registerItems() {
        //CobblemonItems.INSTANCE.register( (identifier, item) -> Registry.register(CobblemonItems.INSTANCE.getRegistry(), identifier, item) );
        //ItemsInit.getRegistry((identifier, item) -> Registry.register(ItemsInit.getRegistryKey(), identifier, item));
        //ItemsInit.register((identifier, item) -> Registry.register(ItemsInit.getRegistryKey(), identifier, item));//(identifier, item) -> Registry.register(ItemsInit1.registry, identifier, item)
        /**CobblemonItemGroups.register(provider ->
                Registry.register(Registries.CREATIVE_MODE_TAB, provider.key, FabricItemGroup.builder()
                        .displayName(provider.displayName)
                        .icon(provider.displayIconProvider)
                        .entries(provider.entryCollector)
                        .build())
        );

        for (Object key : CobblemonItemGroups.injectorKeys()) {
            ItemGroupEvents.modifyEntriesEvent(key).register(content -> {
                FabricItemGroupInjector fabricInjector = new FabricItemGroupInjector(content);
                CobblemonItemGroups.inject(key, fabricInjector);
            });
        }

        for (TradeOffer tradeOffer : CobblemonTradeOffers.tradeOffersForAll()) {
            TradeOfferHelper.registerVillagerOffers(tradeOffer.profession, tradeOffer.requiredLevel, factories -> factories.addAll(tradeOffer.tradeOffers));
        }

        // 1 = common trades, 2 = rare, it has no concept of levels
        for (TradeOffer tradeOffer : CobblemonTradeOffers.resolveWanderingTradeOffers()) {
            TradeOfferHelper.registerWanderingTraderOffers(tradeOffer.isRareTrade ? 2 : 1, factories -> factories.addAll(tradeOffer.tradeOffers));
        }*/
    }

    public void registerBlocks() {
        /**CobblemonBlocks.register((identifier, item) -> Registry.register(CobblemonBlocks.registry, identifier, item));
        for (Object block : CobblemonBlocks.strippedBlocks()) {
            StrippableBlockRegistry.register(block);
        }*/
    }

}
