package com.raspix.forge.cobble_contests.menus.screens;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.storage.pc.PCStore;
import com.cobblemon.mod.common.client.gui.pc.StorageWidget;
import com.cobblemon.mod.common.client.storage.ClientPC;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.cobblemon.mod.common.net.messages.client.trade.TradeStartedPacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.forge.cobble_contests.menus.ContestMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class TestMenuScreen extends AbstractContainerScreen<ContestMenu> {


    private List<Button> buttons;
    private StorageWidget storageWidget;
    private static final int BASE_WIDTH = 349;
    private static final int BASE_HEIGHT = 205;
    private final int PARTY_SLOT_PADDING = 4;


    //private List<TradeStartedPacket.TradeablePokemon> party;
    private ClientPC pc;
    private ClientParty party;
    private ServerPlayer player;
    private Inventory playerInv;
    private ContestMenu menu;

    private static final ResourceLocation TEXTURE = new ResourceLocation(CobbleContests.MOD_ID, "textures/gui/pink_heart_bg.png");

    public TestMenuScreen(ContestMenu menu, Inventory playerInv, Component title) { // ClientPC pc, ClientParty party
        super(menu, playerInv, title);
        //this.leftPos = 0;
        //this.topPos = 0;
        this.imageWidth = 256;
        this.imageHeight = 192;
        this.playerInv = playerInv;
        this.menu = menu;

        //this.party = party;
        //this.pc = pc;
        //this.player = (ServerPlayer) playerInv.player;


    }


    @Override
    public void init() {
        buttons = new ArrayList<>();
        this.buttons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 12, this.topPos + 20, 0, 0, 0, 15, 15, TEXTURE, 1000, 750, btn -> {
            hostContest();
        })));



    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int xMousePos, int yMousePos) {
        //renderBackground(guiGraphics);
        //guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 388, this.imageWidth, this.imageHeight, 1000, 750);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 1000, 750);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xMousePos, int yMousePos, float partialTick) {
        //this.renderBackground(guiGraphics);
        super.render(guiGraphics, xMousePos, yMousePos, partialTick);
        //renderTooltip(guiGraphics, xMousePos, yMousePos);

        //PlayerPartyStore playerPartyStore = menu.getPartyStore();
        ClientParty clientParty = menu.getPartyClient();
        if(clientParty != null){
            Pokemon poke0 = clientParty.get(0);
            String output = "Pokemon is a " + poke0.getNature().getName() + " ";
            output += poke0.getSpecies().getName() + " ";
            output += "with " + poke0.getEvs().toString();
            //System.out.println(output);
            //System.out.println(poke0.getEvs());
            System.out.println(poke0.getNature().getDisplayName());
        }else {
            System.out.println("party is null");
        }
        /**try {
            UUID id = playerInv.player.getUUID();
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(id);


        } catch (NoPokemonStoreException e) {
            throw new RuntimeException(e);
        }*/
    }

    /**public void render(GuiGraphics arg, int m, int n, float g) {
        int i = this.leftPos;
        int j = this.topPos;
        this.renderBg(arg, g, m, n);
        MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Background(this, arg, m, n));
        RenderSystem.disableDepthTest();
        super.render(arg, m, n, g);
        arg.pose().pushPose();
        arg.pose().translate((float)i, (float)j, 0.0F);
        this.hoveredSlot = null;

        this.renderLabels(arg, m, n);
        MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Foreground(this, arg, m, n));

        arg.pose().popPose();
        RenderSystem.enableDepthTest();
    }*/

    //needed to prevent default labels being added
    @Override
    protected void renderLabels(GuiGraphics arg, int i, int j) {
        //arg.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        //arg.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    public void hostContest(){
        boolean didHost = menu.playerStartHosting(playerInv.player.getUUID());
        System.out.println(didHost);
    }


}
