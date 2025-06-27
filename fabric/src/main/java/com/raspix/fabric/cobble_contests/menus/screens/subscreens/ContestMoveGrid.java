package com.raspix.fabric.cobble_contests.menus.screens.subscreens;

import com.cobblemon.mod.common.api.gui.ParentWidget;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.blaze3d.platform.InputConstants;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.events.ContestMoves;
import com.raspix.fabric.cobble_contests.network.SB.SBUpdateContestInfo;
import com.raspix.fabric.cobble_contests.network.SB.SBUseContestMove;
import com.raspix.fabric.cobble_contests.util.data.ContestType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ContestMoveGrid extends ParentWidget {

    public static final int MOVE_WIDTH = 92;
    public static final int MOVE_HEIGHT = 24;
    public static final float MOVE_VERTICAL_SPACING = 5F;
    public static final float MOVE_HORIZONTAL_SPACING = 13F;

    private List<MoveTile> buttons;

    private MoveTile move1;
    private MoveTile move2;
    private MoveTile move3;
    private MoveTile move4;

    private UUID playerId;

    private static final ResourceLocation moveTex = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_move.png");


    public ContestMoveGrid(int i, int j, int k, int l, Component component, UUID playerID) {
        super(i, j, k, l, component);
        this.buttons = new ArrayList<>();
        this.playerId = playerID;

        move1 = new MoveTile(this.getX(), this.getY());
        move2 = new MoveTile(this.getX() + MOVE_HORIZONTAL_SPACING + MOVE_WIDTH, this.getY());
        move3 = new MoveTile(this.getX(), this.getY() + MOVE_HEIGHT + MOVE_VERTICAL_SPACING);
        move4 = new MoveTile(this.getX() + MOVE_HORIZONTAL_SPACING + MOVE_WIDTH, this.getY() + MOVE_HEIGHT + MOVE_VERTICAL_SPACING);
        //buttons.add(new ContestMoveButton(this.getX(), this.getY(), MOVE_WIDTH, MOVE_HEIGHT, btn -> { pressMove();}));
        //buttons.add(new FixedImageButton(5, 5, MOVE_WIDTH, MOVE_HEIGHT, 0, 0, 24, moveTex, 92, 48, btn -> { pressMove();}));
        /**for (int index = 0; index < 4; index++) {
            boolean isEven = index % 2 == 0;
            float x = isEven ? this.getX() : this.getX() + MOVE_HORIZONTAL_SPACING + MOVE_WIDTH;
            float y = index > 1 ? this.getY() + MOVE_HEIGHT + MOVE_VERTICAL_SPACING : this.getY();

            buttons.add(new ContestMoveButton((int) x, (int) y, MOVE_WIDTH, MOVE_HEIGHT, btn -> { pressMove();}));
        }*/

        /**for (int index = 0; index < 4; index++) {
            boolean isEven = index % 2 == 0;
            float x = isEven ? this.getX() : this.getX() + MOVE_HORIZONTAL_SPACING + MOVE_WIDTH;
            float y = index > 1 ? this.getY() + MOVE_HEIGHT + MOVE_VERTICAL_SPACING : this.getY();

            buttons.add(new MoveTile((int) x, (int) y));
        }*/
    }

    public void initializeMoves(Pokemon pokemon){
        ClientParty clientParty = CobblemonClient.INSTANCE.getStorage().getMyParty();
        List<Move> moves = pokemon.getMoveSet().getMoves();

        ArrayList<MoveTile> tiles = new ArrayList<>(Arrays.asList(move1, move2, move3, move4));



        for(int i = 0; i < 4; i++){
            if(moves.size() > i && moves.get(i) != null){
                Move move = moves.get(i);
                String name = move.getName();
                ContestMoves.MoveData data = ContestMoves.instance.getMoveData(name);
                String desc = "cobble_contests.move_function_description." + data.getFunctionType(); //ContestMoves.instance.ALL_FUNCTION_DATA.get(data.getFunctionType()).getDescription();
                int app = ContestMoves.instance.ALL_FUNCTION_DATA.get(data.getFunctionType()).getAppeal();
                int jam = ContestMoves.instance.ALL_FUNCTION_DATA.get(data.getFunctionType()).getJam();
                tiles.get(i).setupButton(data.getType(), name, desc, app, jam);
            }else {
                tiles.get(i).setupButton(ContestType.None, "", "", 0, 0);
            }
        }


    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        /**for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).render(guiGraphics, mouseX, mouseX, partialTicks);
        }*/
        move4.render(guiGraphics, mouseX, mouseY, partialTicks);
        move3.render(guiGraphics, mouseX, mouseY, partialTicks);
        move2.render(guiGraphics, mouseX, mouseY, partialTicks);
        move1.render(guiGraphics, mouseX, mouseY, partialTicks);


    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == InputConstants.MOUSE_BUTTON_LEFT) {
            return mousePrimaryClicked(pMouseX, pMouseY);
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean mousePrimaryClicked(double mouseX, double mouseY) {

        if(this.visible){
            if(move1.isHovered(mouseX, mouseY)){
                move1.onClick();
                ClientPlayNetworking.send(new SBUseContestMove(playerId, move1.getName()));
            }else if (move2.isHovered(mouseX, mouseY)){
                move2.onClick();
                ClientPlayNetworking.send(new SBUseContestMove(playerId, move2.getName()));
            }else if (move3.isHovered(mouseX, mouseY)){
                move3.onClick();
                ClientPlayNetworking.send(new SBUseContestMove(playerId, move3.getName()));
            }else if (move4.isHovered(mouseX, mouseY)){
                move4.onClick();
                ClientPlayNetworking.send(new SBUseContestMove(playerId, move4.getName()));
            }
        }

        /**if (temp.isHovered(mouseX, mouseY)) {
            //move = m;
            temp.onClick();
           // break;
            return true;
        }


        /**if (move != null) {
            if (this.request.activePokemon.getFormat().battleType.pokemonPerSide == 1) {
                move.onClick();
            } else {
                battleGUI.changeActionSelection(new BattleTargetSelection(battleGUI, request, move.move));
                playDownSound(Minecraft.getInstance().soundManager);
            }
            return true;
        } else if (backButton.isHovered(mouseX, mouseY)) {
            playDownSound(Minecraft.getInstance().soundManager);
            battleGUI.changeActionSelection(null);
        } else if (gimmick != null) {
            for (GimmickButton g : gimmickButtons) {
                if (g != gimmick) {
                    g.toggled = false;
                }
            }
            moveTiles = gimmick.toggle() ? gimmick.tiles : baseTiles;
        } else if (shiftButton.isHovered(mouseX, mouseY)) {
            playDownSound(Minecraft.getInstance().soundManager);
            battleGUI.selectAction(request, new ShiftActionResponse());
        }*/
        return false;
    }



    public void pressMove(){
        System.out.println("Hey");
    }
}
