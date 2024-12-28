package com.raspix.neoforge.cobble_contests;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.logging.LogUtils;
import com.raspix.neoforge.cobble_contests.blocks.BlockInit;
import com.raspix.neoforge.cobble_contests.blocks.entity.BlockEntityInit;
import com.raspix.neoforge.cobble_contests.items.ItemInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.RegisterCommandsEvent;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.ModLoadingContext;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.config.ModConfig;
//import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
//import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import net.minecraftforge.registries.DeferredRegister;
//import org.slf4j.Logger;
//import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;


/**TODO future:
 * -texture booth
 * -clean up all print statements
 * -make sure contest results are fully translatable
 *
 *
 * DONE:
 *  -make contests tell result
 *  -make poffins tell if full
 *  -figure out how to make hexagon translusent + fix color issue
 *  -make default values for creative poffins
 *  -make sure nature eating poffin bonus is properly applied
 *  -make sure poffins dissapear on use in survival
 *  -get tooltips to show up on poffinpot
 *  -fix up poffins with unclear colors
 *  -finish booth gui
 *
 *
 */




@Mod(CobbleContestsForge.MOD_ID)
public class CobbleContestsForge {

    public static final String MOD_ID = "cobble_contests";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    //public static ContestMoves contestMoves;
    //public static final CobbleContestsDataProvider dataProvider = new CobbleContestsDataProvider();




    public CobbleContestsForge(IEventBus modEventBus, ModContainer modContainer){
        //CobbleContests.init();
        //MinecraftForge.EVENT_BUS.register(this);

        //IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        NeoForge.EVENT_BUS.register(this);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::initialize);



        BlockInit.BLOCKS.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);
        //MenuInit.MENU_TYPES.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);



        /**PlatformEvents.SERVER_STARTED.subscribe(Priority.LOWEST, event ->{
            contestMoves = new ContestMoves();
            return null;
        });*/

    }

    @SubscribeEvent
    public void onCommandRegistration(RegisterCommandsEvent event) {
        //ExampleCommandRegistry.registerCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    /**@SubscribeEvent
    public void onCommandRegistration(RegisterCommandsEvent event) {
        ExampleCommandRegistry.registerCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }*/

    public void initialize(FMLCommonSetupEvent event){
        //dataProvider.registerDefaults();
    }

    /**@EventBusSubscriber(bus = EventBusSubscriber.Bus.NEOFORGE)
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
    }*/


    @EventBusSubscriber(modid = CobbleContestsForge.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientEventBusSubscriber {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer((BlockInit.CONTEST_BOOTH.get()), RenderType.translucent());
        }
    }


    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents{

        private ClientModEvents(){}


        @SubscribeEvent
        public static void clientSetUp(FMLClientSetupEvent event){
            event.enqueueWork(()-> {
                //MenuScreens.register(MenuInit.CONTEST_MENU.get(), SecondTestScreen::new);
                //MenuScreens.register(MenuInit.PLAYER_CONTEST_INFO_MENU.get(), PlayerContestInfoScreen::new);
                //MenuScreens.register(MenuInit.POFFIN_POT_MENU.get(), PoffinPotScreen::new);

            });



        }

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class CommonModEvents{

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event){
            event.enqueueWork(() -> {
                //PacketHandler.register();
            });

        }
        /**@SubscribeEvent
        public void addJsonListeners(AddReloadListenerEvent event) {
            event.addListener(JsonLoadMoves.instance);
        }*/

    }



}
