package com.raspix.fabric.cobble_contests.menus.screens;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.trade.ModelWidget;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.menus.ContestMenu;
import com.raspix.fabric.cobble_contests.menus.widgets.ContestMessagePane;
import com.raspix.fabric.cobble_contests.menus.widgets.FixedImageButton;
import com.raspix.fabric.cobble_contests.network.SBUpdateContestInfo;
import com.raspix.fabric.cobble_contests.util.Contest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Matrix4fStack;

import java.util.UUID;

import static com.cobblemon.mod.common.client.gui.PokemonGuiUtilsKt.drawProfilePokemon;
import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class ContestScreen extends AbstractContainerScreen<ContestMenu> {

    private UUID playerId;
    private Pokemon pokemon;
    private Contest.ContestPhase phase;

    private static final ResourceLocation BATTLE_MESSAGE_PANE_FRAME_RESOURCE = cobblemonResource("textures/gui/battle/battle_log.png");
    private static final ResourceLocation CONTEST_PANEL_TEXTURE = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/intro_editor_screen.png");
    private static final ResourceLocation CONTEST_STICKERS = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/seals.png");
    private static final ResourceLocation NUMBERS = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/numbers.png");

    private static final int FRAME_WIDTH = 169;
    private static final int FRAME_HEIGHT = 55;

    private String placeholderText = "UWU this is a test of the something something service, this is just a test, blah " +
            "blah blah. This needs more words to check for the thing hmm \n test next line. \n wooooooooo um words, " +
            "still not long enough yet. hmmmmmmmmmm maybe need a copypasta";

    private static final Camera CAMERA = new Camera();
    private ModelWidget modelWidget; //for the pokemon being rendered
    private ContestMessagePane messageLog;
    private FixedImageButton particleEffectButton;




    public ContestScreen(ContestMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 291;//362;
        this.imageHeight = 194;
        this.playerId = inventory.player.getUUID();
        //this.playerInv = playerInv;
        //this.contestInfoMenu = containerID;
        phase = Contest.ContestPhase.WAITING;

    }

    @Override
    protected void init() {
        super.init();
        this.messageLog = this.addRenderableWidget(new ContestMessagePane(getAppropriateX(), getAppropriateY(), 165, 55,
                Component.literal(placeholderText), Minecraft.getInstance().font));
        this.addRenderableWidget(new FixedImageButton(5, 5, 25, 25, 0, 0, 25, CONTEST_STICKERS, 275, 200, btn -> {
            debugNextScreen();
        }));
        this.particleEffectButton = this.addRenderableWidget(new FixedImageButton(this.leftPos + 236, this.topPos + 146, 16, 15, 2, 204, 17, CONTEST_PANEL_TEXTURE, 948, 600, btn ->{
            createParticleEffect(1, 1, 1);
        }));
        ClientPlayNetworking.send(new SBUpdateContestInfo(playerId));
        //updateInfo();
        updateGUI();
        //get updates here
    }

    // needed to prevent always having grayed out background
    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        //this.renderTransparentBackground(guiGraphics);
        this.renderBg(guiGraphics, f, i, j);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        //guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 948, 600);
        /**else if(pageIndex == 1) { //stat page
            guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 194, this.imageWidth, this.imageHeight, 948, 600);
        }else{
            guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 388, this.imageWidth, this.imageHeight, 948, 600);
        }*/

        switch (phase){
            case WAITING:
                guiGraphics.blit(CONTEST_PANEL_TEXTURE, this.leftPos, this.topPos, 152, 204, 116, 49, 948, 600);
                break;
            case DRESSUP:
                this.renderTransparentBackground(guiGraphics);
                guiGraphics.blit(CONTEST_PANEL_TEXTURE, this.leftPos, this.topPos, 0, 0, 304, 202, 948, 600); //texture, screen x, screen y, texture offset x, texture offset y, portion width, portion height, png width, png height
                //guiGraphics.blit(CONTEST_PANEL_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 948, 600);
                break;
            case INTRODUCTION:
                break;
            case SHOWOFF:
                break;
            case RESULTS:
                this.renderTransparentBackground(guiGraphics);
                guiGraphics.blit(CONTEST_PANEL_TEXTURE, this.leftPos, this.topPos, 0, 337, this.imageWidth, this.imageHeight, 948, 600);
                break;
            default:
                break;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xMousePos, int yMousePos, float partialTick) {
        super.render(guiGraphics, xMousePos, yMousePos, partialTick);
        if(modelWidget != null) {
            modelWidget.visible = false;
        }
        switch (phase){
            case WAITING:
                renderWaitingGUI(guiGraphics);
                break;
            case DRESSUP:
                if(modelWidget != null) {
                    modelWidget.visible = true;
                    modelWidget.render(guiGraphics, xMousePos, yMousePos, partialTick);
                }
                renderDressUpGUI(guiGraphics, partialTick);
                break;
            case INTRODUCTION:
                renderIntroGUI(guiGraphics);
                break;
            case SHOWOFF:
                renderShowOffGUI(guiGraphics);
                break;
            case RESULTS:
                renderResults(guiGraphics);
                break;
            default:
                break;
        }
        //renderMessagePaneTemp(guiGraphics);

    }

    private void setSelectedModel() {
        if(pokemon != null){
            modelWidget = new ModelWidget(
                    this.leftPos + 21 + 180,
                    this.topPos + 27 -10,
                    66,
                    66,
                    pokemon.asRenderablePokemon(),
                    2F,
                    35,
                    -10.0
            );
        }else {
            modelWidget = null;
        }
    }

    private void debugNextScreen(){
        Contest.ContestPhase newPhase = switch (phase) {
            case Contest.ContestPhase.WAITING -> Contest.ContestPhase.DRESSUP;
            case Contest.ContestPhase.DRESSUP -> Contest.ContestPhase.INTRODUCTION;
            case Contest.ContestPhase.INTRODUCTION -> Contest.ContestPhase.SHOWOFF;
            case Contest.ContestPhase.SHOWOFF -> Contest.ContestPhase.RESULTS;
            case Contest.ContestPhase.RESULTS -> Contest.ContestPhase.WAITING;
            default -> Contest.ContestPhase.WAITING;
        };
        setPhase(newPhase);
    }

    public static void renderEntityOnScreen(int pPosX, int pPosY, int pScale, float pMouseX, float pMouseY, LivingEntity pLivingEntity) {
        /**float f = (float)Math.atan((double)(pMouseX / 40.0F));
        float f1 = (float)Math.atan((double)(pMouseY / 40.0F));
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double)pPosX, (double)pPosY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float)pScale, (float)pScale, (float)pScale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        posestack1.mulPose(quaternion);
        /**float f2 = pLivingEntity.yBodyRot;
        float f3 = pLivingEntity.getYRot();
        float f4 = pLivingEntity.getXRot();
        float f5 = pLivingEntity.yHeadRotO;
        float f6 = pLivingEntity.yHeadRot;
        pLivingEntity.yBodyRot = 180.0F + f * 20.0F;
        pLivingEntity.setYRot(180.0F + f * 40.0F);
        pLivingEntity.setXRot(-f1 * 20.0F);
        pLivingEntity.yHeadRot = pLivingEntity.getYRot();
        pLivingEntity.yHeadRotO = pLivingEntity.getYRot();*/
        /**Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(pLivingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        /**pLivingEntity.yBodyRot = f2;
        pLivingEntity.setYRot(f3);
        pLivingEntity.setXRot(f4);
        pLivingEntity.yHeadRotO = f5;
        pLivingEntity.yHeadRot = f6;*/
        /**posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();*/
    }

    private void createParticleEffect(int x, int y, float pScale){
        ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
        Particle particle = particleManager.createParticle(ParticleTypes.HEART, 100, 65, 100, 1.0, 1.0, 1.0);
    }

    private void renderParticleEffect(GuiGraphics guiGraphics, int x, int y, float pScale) {
        System.out.println("trying to render particle");
        PoseStack matrices = guiGraphics.pose();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();

        // Create and render a particle effect at the specified position
        //Entity ent = EntityType.FIREWORK_ROCKET.create(Minecraft.getInstance().level);
        //Entity particleEntity = EntityType.FIREWORK_ROCKET.create(Minecraft.getInstance().level);//new Entity(EntityType.FIREWORK_ROCKET, Minecraft.getInstance().level);
        //Entity heartparticleEntity = ParticleTypes.HEART; // EntityType.FIREWORK_ROCKET.create(Minecraft.getInstance().level);//new Entity(EntityType.FIREWORK_ROCKET, Minecraft.getInstance().level);
        //particleEntity.updatePosition(x, y, 0); // Set the position of the particle effect
        //MinecraftClient.getInstance().particleManager.addParticle(particleEntity, Identifier("your_particle_type"));
        ParticleEngine particleManager = Minecraft.getInstance().particleEngine;



        /**PoseStack posestack = guiGraphics.pose();
        posestack.pushPose();
        posestack.translate((double)1, (double)1, 1050.0D);
        posestack.scale(pScale, pScale, - pScale);

        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float)pScale, (float)pScale, (float)pScale);*/

        matrices.pushPose();
        Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushMatrix();
        matrixStack.translate(0, 0, 500);
        matrixStack.scale(24, 24, 1);
        matrixStack.translate(0, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 24f, 0);
        matrixStack.scale(1, -1, 1);
        //matrixStack.multiplyPositionMatrix(matrices.last());
        RenderSystem.applyModelViewMatrix();

        Tesselator tessellator = Tesselator.getInstance();

        //ParticleManagerAccessor.SimpleSpriteProviderAccessor
        Particle particle = particleManager.createParticle(ParticleTypes.HEART, 100, 65, 100, 1.0, 1.0, 1.0);
        if(particle == null){
            System.out.println("IS NULL");
        }else {
            System.out.println(particle);
        }



        /**RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Particle particle = particleManager.createParticle(ParticleTypes.FLAME, x, y, 0, 0.0, 0.0, 0.0);

        BufferBuilder bufferBuilder = particle.getRenderType().begin(tessellator, Minecraft.getInstance().getTextureManager());*/

        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferBuilder bufferBuilder = particle.getRenderType().begin(tessellator, Minecraft.getInstance().getTextureManager());



        //EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        //entityrenderdispatcher.setRenderShadow(false);
        //RenderSystem.runAsFancy(() -> {

        try {

            //particle.render(bufferBuilder, CAMERA, Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());



        } catch (Throwable var17) {
            System.out.println("OH NO A PARTICLE ERROR");
        }
            //entityrenderdispatcher.render(particleEntity, x, y, 0.0D, 0.0F, 1.0F, posestack1, Minecraft.getInstance().renderBuffers().bufferSource(), 15728880);
        //});

        //Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
        //entityrenderdispatcher.setRenderShadow(true);
        /**pLivingEntity.yBodyRot = f2;
         pLivingEntity.setYRot(f3);
         pLivingEntity.setXRot(f4);
         pLivingEntity.yHeadRotO = f5;
         pLivingEntity.yHeadRot = f6;*/

        //BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());


        //Mirage.getAlwaysBrightLTM().disable();
        matrixStack.popMatrix();
        RenderSystem.applyModelViewMatrix();
        matrices.popPose();

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();


        /**BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();*/
    }

    protected void renderWaitingGUI(GuiGraphics guiGraphics){

    }

    protected void renderDressUpGUI(GuiGraphics guiGraphics, float partialTick){
        if (pokemon != null) {
            float halfScale = 0.5f;
            PoseStack poses = guiGraphics.pose();
            ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
            for (int i = 0; i < 5; i++) {
                double posX = this.leftPos + 10;
                double posY = this.topPos + 5;
                particleManager.createParticle(ParticleTypes.HEART, posX, posY, 0.0, 0.0, 0.0, 0.0);
            }

        }
        //renderParticleEffect(guiGraphics, 10, 5, 20);

    }

    protected void renderIntroGUI(GuiGraphics guiGraphics){
        renderParticleEffect(guiGraphics, 1, 1, 1);
    }

    protected void renderShowOffGUI(GuiGraphics guiGraphics){

    }

    protected void renderResults(GuiGraphics guiGraphics){

    }

    protected void updateInfo(){
        //get updated info
    }

    public void setUpdatedInfo(int pokemonSlot, Contest.ContestPhase phase){
        this.pokemon = CobblemonClient.INSTANCE.getStorage().getMyParty().get(pokemonSlot);
        setPhase(phase);
        setSelectedModel();
    }

    private void setPhase(Contest.ContestPhase phased){
        this.phase = phased;
        updateGUI();
    }

    protected void updateGUI(){
        switch (phase){
            case WAITING:
                messageLog.visible = true;
                particleEffectButton.visible = false;
                break;
            case DRESSUP:
                this.imageWidth = 304;
                this.imageHeight = 202;
                messageLog.visible = false;
                particleEffectButton.visible = true;
                break;
            case INTRODUCTION:
                messageLog.visible = true;
                particleEffectButton.visible = false;
                break;
             case SHOWOFF:
                 messageLog.visible = false; // turn back on later
                 particleEffectButton.visible = true;
                break;
            case RESULTS:
                this.imageWidth = 291;
                this.imageHeight = 194;
                messageLog.visible = false;
                particleEffectButton.visible = false;
                break;
            default:
                messageLog.visible = true;
                particleEffectButton.visible = false;
                break;
        }

    }



    public int getAppropriateX() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() - (FRAME_WIDTH + 12);
    }

    public int getAppropriateY() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() - (30 + FRAME_HEIGHT);
    }

    @Override
    protected void renderLabels(GuiGraphics arg, int i, int j) {
    }





}
