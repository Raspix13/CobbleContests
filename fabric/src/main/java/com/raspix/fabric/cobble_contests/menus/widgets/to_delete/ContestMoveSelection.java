package com.raspix.fabric.cobble_contests.menus.widgets.to_delete;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.menus.widgets.buttons.FixedImageButton;
import net.minecraft.resources.ResourceLocation;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class ContestMoveSelection extends FixedImageButton {
    public static final int MOVE_WIDTH = 92;
    public static final int MOVE_HEIGHT = 24;
    public static final float MOVE_VERTICAL_SPACING = 5F;
    public static final float MOVE_HORIZONTAL_SPACING = 13F;

    public static final ResourceLocation moveTexture =  ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/battle/battle_move.png"); //cobblemonResource("textures/gui/battle/battle_move.png"); //
    public static final ResourceLocation moveOverlayTexture = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/battle/battle_move_overlay.png");// cobblemonResource("textures/gui/battle/battle_move_overlay.png");


    //private final List<MoveTile> moveTiles;


    public ContestMoveSelection(int screenX, int screenY, int buttonWidth, int buttonHeight, int xTexStart, int yTexStart, ResourceLocation resourceLocation, OnPress onPress) {
        super(screenX, screenY, buttonWidth, buttonHeight, xTexStart, yTexStart, resourceLocation, onPress);

        //super(battleGUI, request, 20, Minecraft.getInstance().getWindow().getGuiScaledHeight() - 84, 100, 100, battleLang("ui.select_move"));

        /**MoveSet moveSet = request.moveSet;
        List<MoveTile> baseTiles = new ArrayList<>();
        for (int index = 0; index < moveSet.getMoves().size(); index++) {
            InBattleMove inBattleMove = moveSet.moves.get(index);
            boolean isEven = index % 2 == 0;
            float x = isEven ? this.getX() : this.getX() + MOVE_HORIZONTAL_SPACING + MOVE_WIDTH;
            float y = index > 1 ? this.getY() + MOVE_HEIGHT + MOVE_VERTICAL_SPACING : this.getY();

            baseTiles.add(new MoveTile(this, inBattleMove, x, y));
        }
        this.moveTiles = baseTiles;*/
    }





    /**public static class MoveTile {
        private final ContestMoveSelection moveSelection;
        private final InBattleMove move;
        private final float x;
        private final float y;
        private final MoveTemplate moveTemplate;
        private final Pokemon pokemon;
        private final ContestType conType;
        private final RGB rgb;

        public MoveTile(ContestMoveSelection moveSelection, InBattleMove move, float x, float y) {
            this.moveSelection = moveSelection;
            this.move = move;
            this.x = x;
            this.y = y;
            this.moveTemplate = Moves.getByNameOrDummy(move.id);
            this.pokemon = moveSelection.request.activePokemon.actor.pokemon.stream()
                    .filter(p -> p.uuid.equals(moveSelection.request.activePokemon.battlePokemon.uuid))
                    .findFirst().orElse(null);

            ContestMoves.MoveData2 data = ContestMoves.instance.getMoveData(moveName);
            if(data != null){
                this.conType = data.getType();
            }else {
                this.conType = ContestType.None;
            }

            //this.conType = moveTemplate.getEffectiveElementalType(pokemon);
            this.rgb = conType.hue.toRGB();
        }

        public boolean isSelectable() {
            return !move.disabled;
        }


        public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
            float selectConditionOpacity = moveSelection.opacity * (isSelectable() ? 1F : 0.5F);

            blitk(context.pose(),
                    moveTexture,
                    x,
                    y,
                    MOVE_WIDTH,
                    MOVE_HEIGHT,
                    isHovered(mouseX, mouseY) ? MOVE_HEIGHT : 0,
                    MOVE_HEIGHT * 2,
                    rgb.first,
                    rgb.second,
                    rgb.third,
                    selectConditionOpacity);

            blitk(context.pose(), moveOverlayTexture, x, y, MOVE_WIDTH, MOVE_HEIGHT, moveSelection.opacity);

            // Type Icon
            new ContestTypeIcon(x - 9, y + 2, conType).render(context);
        }
    }*/

}


