package com.raspix.forge.cobble_contests;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.logging.LogUtils;

import com.raspix.forge.cobble_contests.blocks.BlockInit;
import com.raspix.forge.cobble_contests.blocks.entity.BlockEntityInit;
import com.raspix.forge.cobble_contests.items.ItemInit;
import com.raspix.forge.cobble_contests.menus.MenuInit;
import com.raspix.forge.cobble_contests.menus.screens.PlayerContestInfoScreen;
import com.raspix.forge.cobble_contests.menus.screens.PoffinPotScreen;
import com.raspix.forge.cobble_contests.menus.screens.SecondTestScreen;
import com.raspix.forge.cobble_contests.menus.screens.TestMenuScreen;
import com.raspix.forge.cobble_contests.network.PacketHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.slf4j.Logger;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

/**TODO:
 * -make sure poffins dissapear on use in survival
 * -figure out how to make hexagon translusent
 * -clean up all print statements
 *
 */




@Mod(CobbleContestsForge.MOD_ID)
public class CobbleContestsForge {

    public static final String MOD_ID = "cobble_contests";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    //public static ContestMoves contestMoves;
    public static final CobbleContestsDataProvider dataProvider = new CobbleContestsDataProvider();




    public CobbleContestsForge(){
        //CobbleContests.init();
        //MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::initialize);

        BlockInit.BLOCKS.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);
        MenuInit.MENU_TYPES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);



        /**PlatformEvents.SERVER_STARTED.subscribe(Priority.LOWEST, event ->{
            contestMoves = new ContestMoves();
            return null;
        });*/

    }

    /**@SubscribeEvent
    public void onCommandRegistration(RegisterCommandsEvent event) {
        ExampleCommandRegistry.registerCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }*/

    public void initialize(FMLCommonSetupEvent event){
        dataProvider.registerDefaults();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class Registration {

        @SubscribeEvent
        public static void onCommandRegistration(final RegisterCommandsEvent event) {
            event.getDispatcher().register(Commands.literal("test")
                    .executes(context -> {
                        Species species = PokemonSpecies.INSTANCE.getByIdentifier(ResourceLocation.of("cobblemon:bidoof", ':'));
                        context.getSource().sendSystemMessage(
                                Component.literal("Got species: ")
                                        .withStyle(Style.EMPTY.withColor(0x03e3fc))
                                        .append(species.getTranslatedName())
                        );
                        return 0;
                    })
            );
        }

        //SimpleJsonResourceReloadListener
    }


    @Mod.EventBusSubscriber(modid = CobbleContestsForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientEventBusSubscriber {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer((BlockInit.CONTEST_BOOTH.get()), RenderType.translucent());
        }
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents{

        private ClientModEvents(){}


        @SubscribeEvent
        public static void clientSetUp(FMLClientSetupEvent event){
            event.enqueueWork(()-> {
                MenuScreens.register(MenuInit.CONTEST_MENU.get(), SecondTestScreen::new);
                MenuScreens.register(MenuInit.PLAYER_CONTEST_INFO_MENU.get(), PlayerContestInfoScreen::new);
                MenuScreens.register(MenuInit.POFFIN_POT_MENU.get(), PoffinPotScreen::new);

            });



        }

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModEvents{

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event){
            event.enqueueWork(() -> {
                PacketHandler.register();
            });

        }
        /**@SubscribeEvent
        public void addJsonListeners(AddReloadListenerEvent event) {
            event.addListener(JsonLoadMoves.instance);
        }*/

    }



}
