package com.raspix.fabric.cobble_contests.menus.screens;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.trade.ModelWidget;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.menus.ContestMenu;
import com.raspix.fabric.cobble_contests.menus.widgets.ContestMessagePane;
import com.raspix.fabric.cobble_contests.menus.widgets.DressUpCounter;
import com.raspix.fabric.cobble_contests.menus.widgets.FixedImageButton;
import com.raspix.fabric.cobble_contests.menus.widgets.ParticleScreenRenderer;
import com.raspix.fabric.cobble_contests.network.SBUpdateContestInfo;
import com.raspix.fabric.cobble_contests.util.Contest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.joml.*;

import java.util.ArrayList;
import java.util.List;
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

    private static final ResourceLocation TEMP_PARTICLE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/particle/heart.png");

    private boolean isModelSet;

    private static final int FRAME_WIDTH = 169;
    private static final int FRAME_HEIGHT = 55;

    private String placeholderText = "UWU this is a test of the something something service, this is just a test, blah " +
            "blah blah. This needs more words to check for the thing hmm \n test next line. \n wooooooooo um words, " +
            "still not long enough yet. hmmmmmmmmmm maybe need a copypasta";

    private static final Camera CAMERA = new Camera();
    private ModelWidget modelWidget; //for the pokemon being rendered
    private ContestMessagePane messageLog;
    private FixedImageButton particleEffectButton;
    private List<FixedImageButton> dressUpButtons;
    private DressUpCounter counter;
    private ParticleScreenRenderer particleBox;




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

        dressUpButtons = new ArrayList<>();

        this.particleEffectButton = this.addRenderableWidget(new FixedImageButton(this.leftPos + 236, this.topPos + 146, 16, 15, 2, 204, 17, CONTEST_PANEL_TEXTURE, 948, 600, btn ->{
            startParticles();
            //createParticleEffect(1, 1, 1);
        }));
        dressUpButtons.add(particleEffectButton);

        dressUpButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 103, this.topPos + 171, 93, 19, 55, 204, 21, CONTEST_PANEL_TEXTURE, 948, 600, btn ->{
            //createParticleEffect(1, 1, 1);
        }))); // confirm
        dressUpButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 15, this.topPos + 170, 11, 17, 25, 204, 19, CONTEST_PANEL_TEXTURE, 948, 600, btn ->{
            //createParticleEffect(1, 1, 1);
        }))); // back
        dressUpButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 30, this.topPos + 170, 11, 17, 38, 204, 19, CONTEST_PANEL_TEXTURE, 948, 600, btn ->{
            //createParticleEffect(1, 1, 1);
        }))); //forward

        this.counter = this.addRenderableWidget(new DressUpCounter(this.leftPos + 231, this.topPos + 125, 16, 15, Component.literal("")));

        isModelSet = false;
        ClientPlayNetworking.send(new SBUpdateContestInfo(playerId));
        //updateInfo();
        updateGUI();

        this.particleBox = this.addRenderableWidget(new ParticleScreenRenderer(this.leftPos + 206, this.topPos + 12, 40, 40, Component.literal("Hi")));
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
            case TALENT:
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
            case TALENT:
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
            if(!isModelSet) {
                modelWidget = new ModelWidget(
                        this.leftPos + 25 + 180,
                        this.topPos + 32 - 10,
                        66,
                        66,
                        pokemon.asRenderablePokemon(),
                        2F,
                        35,
                        -10.0
                );
                isModelSet = true;
            }
        }else {
            modelWidget = null;
        }
    }

    private void debugNextScreen(){
        Contest.ContestPhase newPhase = switch (phase) {
            case Contest.ContestPhase.WAITING -> Contest.ContestPhase.DRESSUP;
            case Contest.ContestPhase.DRESSUP -> Contest.ContestPhase.INTRODUCTION;
            case Contest.ContestPhase.INTRODUCTION -> Contest.ContestPhase.TALENT;
            case Contest.ContestPhase.TALENT -> Contest.ContestPhase.RESULTS;
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

    private void startParticles(){
        this.particleBox.startParticleRender();
    }


    /**protected void renderRotatedQuad(VertexConsumer vertexConsumer, Camera camera, Quaternionf quaternionf, float f) {
        Vec3 vec3 = camera.getPosition();
        float g = (float)(Mth.lerp((double)f, this.xo, this.x) - vec3.x());
        float h = (float)(Mth.lerp((double)f, this.yo, this.y) - vec3.y());
        float i = (float)(Mth.lerp((double)f, this.zo, this.z) - vec3.z());
        this.renderRotatedQuad(vertexConsumer, quaternionf, g, h, i, f);
    }*/

    /**protected void renderRotatedQuad(VertexConsumer vertexConsumer, Quaternionf quaternionf, float f, float g, float h, float i) {
        float j = 1;//this.getQuadSize(i);
        float k = this.getU0();
        float l = this.getU1();
        float m = this.getV0();
        float n = this.getV1();
        int o = 5;//this.getLightColor(i);
        this.renderVertex(vertexConsumer, quaternionf, f, g, h, 1.0F, -1.0F, j, l, n, o);
        this.renderVertex(vertexConsumer, quaternionf, f, g, h, 1.0F, 1.0F, j, l, m, o);
        this.renderVertex(vertexConsumer, quaternionf, f, g, h, -1.0F, 1.0F, j, k, m, o);
        this.renderVertex(vertexConsumer, quaternionf, f, g, h, -1.0F, -1.0F, j, k, n, o);
    }*/

    //vert, qua, x, y, z, vertx, verty, scale, uvX, uvY, light
    private void renderVertex(VertexConsumer vertexConsumer, Quaternionf quaternionf, float f, float g, float h, float i, float j, float k, float l, float m, int n) {
        Vector3f vector3f = (new Vector3f(i, j, 0.0F)).rotate(quaternionf).mul(k).add(f, g, h);
        vertexConsumer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(l, m).setColor(1f, 1f, 1f, 1f).setLight(n);
    }



    protected void renderDressUpGUI(GuiGraphics guiGraphics, float partialTick){
        if (pokemon != null) {
            float halfScale = 0.5f;
            PoseStack poses = guiGraphics.pose();
            ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
            /**for (int i = 0; i < 5; i++) {
                double posX = this.leftPos + 10;
                double posY = this.topPos + 5;
                particleManager.createParticle(ParticleTypes.HEART, posX, posY, 0.0, 0.0, 0.0, 0.0);
            }*/

        }
        //renderParticleEffect(guiGraphics, 10, 5, 20);

    }

    protected void renderIntroGUI(GuiGraphics guiGraphics){
       // renderParticleEffect(guiGraphics, 1, 1, 1);
        //renderParticle(new Vector2f(this.leftPos, this.topPos), TEMP_PARTICLE, 30, 0, 8, 0, 8);
    }

    protected void renderShowOffGUI(GuiGraphics guiGraphics){

    }

    protected void renderResults(GuiGraphics guiGraphics){

    }

    protected void updateInfo(){
        //get updated info
    }

    public void setUpdatedInfo(int pokemonSlot, Contest.ContestPhase phase, int time){
        this.pokemon = CobblemonClient.INSTANCE.getStorage().getMyParty().get(pokemonSlot);
        setPhase(phase);
        setSelectedModel();
        if(phase == Contest.ContestPhase.DRESSUP){
            counter.updateTime(time);
        }



    }

    private void setPhase(Contest.ContestPhase phased){
        this.phase = phased;
        updateGUI();
    }

    protected void updateGUI(){
        switch (phase){
            case WAITING:
                messageLog.visible = true;
                counter.visible = false;
                //particleEffectButton.visible = false;
                toggleButtonList(false, dressUpButtons);
                break;
            case DRESSUP:
                this.imageWidth = 304;
                this.imageHeight = 202;
                messageLog.visible = false;
                counter.visible = true;
                toggleButtonList(true, dressUpButtons);
                //particleEffectButton.visible = true;
                break;
            case INTRODUCTION:
                messageLog.visible = true;
                counter.visible = false;
                toggleButtonList(false, dressUpButtons);
                //particleEffectButton.visible = false;
                break;
             case TALENT:
                 messageLog.visible = false; // turn back on later
                 counter.visible = false;
                 toggleButtonList(false, dressUpButtons);
                 //particleEffectButton.visible = false;
                break;
            case RESULTS:
                this.imageWidth = 291;
                this.imageHeight = 194;
                messageLog.visible = false;
                counter.visible = false;
                toggleButtonList(false, dressUpButtons);
                //particleEffectButton.visible = false;
                break;
            default:
                messageLog.visible = true;
                counter.visible = false;
                toggleButtonList(false, dressUpButtons);
                //particleEffectButton.visible = false;
                break;
        }

    }

    public void toggleButtonList(boolean state, List<FixedImageButton> butList){
        for(int i = 0; i < butList.size(); i++){
            butList.get(i).visible = state;
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





    /**private void renderParticle(Vector2f screenPos, ResourceLocation imageLoc, float scale,float uvX1, float uvX2, float uvY1, float uvY2){
        System.out.println("Rendering Particle");

        int maximum = 50;
        int MAX_STATS = 255;

        float upperHexX = (float) (Math.sin(Math.toRadians(72.0)) * (float) maximum);
        float upperHexY = (float) (Math.cos(Math.toRadians(72.0)) * (float) maximum);
        float lowerHexX = (float) (Math.sin(Math.toRadians(36.0)) * (float) maximum);
        float lowerHexY = (float) (Math.cos(Math.toRadians(36.0)) * (float) maximum);

        float hexCenterX = this.leftPos + (this.imageWidth / 2.0F) + 47;
        float hexCenterY = this.topPos + (this.imageHeight / 2.0F) - 13;

        float coolRatio = Math.max(0.0F, Math.min(1.0F, (float)  255/ (float)MAX_STATS))+ 0.05f;
        float beautyRatio = Math.max(0.0F, Math.min(1.0F, (float)  255/ (float)MAX_STATS))+ 0.05f;

        Vector2f centerPoint = new Vector2f(hexCenterX, hexCenterY);
        Vector2f coolPoint = new Vector2f(hexCenterX, hexCenterY - (maximum * coolRatio));
        Vector2f beautyPoint = new Vector2f(hexCenterX + (upperHexX * beautyRatio), hexCenterY - (upperHexY * beautyRatio));
        Vector2f upperLeftPoint = new Vector2f(hexCenterX + (upperHexX * beautyRatio), hexCenterY - (maximum * coolRatio));

        drawParticle(new Vector4f(1f, 1f, 1f, 1f), coolPoint, centerPoint, beautyPoint, upperLeftPoint, new Vector4f(0, 4, 0, 4), 0, imageLoc);



        /**drawParticle(new Vector4f(45f/255f, 237f/255f, 96f/255f, 1f),
         new Vector2f(1, -1).mul(scale).add(screenPos), new Vector2f(1, 1).mul(scale).add(screenPos),
         new Vector2f(-1, 1).mul(scale).add(screenPos), new Vector2f(-1, -1).mul(scale).add(screenPos),
         new Vector4f(uvX1, uvX2, uvY1, uvY2), 8, imageLoc); // vector4f(x, y, z, w)*/

    /**    drawParticle(new Vector4f(45f/255f, 237f/255f, 96f/255f, 0.6f),
                new Vector2f(-1, -1).mul(scale).add(screenPos), new Vector2f(-1, 1).mul(scale).add(screenPos),
                new Vector2f(1, 1).mul(scale).add(screenPos), new Vector2f(1, -1).mul(scale).add(screenPos),
                new Vector4f(uvX1, uvX2/8f, uvY1, uvY2/8f), 8, imageLoc); // vector4f(x, y, z, w)
    }

    private static final ResourceLocation MY_WHITE = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/heart.png");

    private void drawParticle(Vector4f colour, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4, Vector4f uvs, int light, ResourceLocation imageLoc) {
        //Vector4f test = new Vector4f(1, 2, 3, 4);

        System.out.println(v1 + ", " + v2 + ", " + v3 + ", " + v4);
        //(267, 58), (267, 111), (316, 94), (316, 58)   : lb, lt, rt, rb
        //(98, 38), (98, 98), (38, 98), (38, 38)        : br, tr, tl, bl

        RenderSystem.setShaderTexture(0, MY_WHITE);//CobblemonResources.INSTANCE.getWHITE());
        //RenderSystem.setShaderTexture(0, imageLoc);
        RenderSystem.setShaderColor(colour.x, colour.y, colour.z, colour.w);
        RenderSystem.depthMask(true);
        RenderSystem.enableBlend();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);//getBuilder();

        //bufferBuilder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION);
        bufferBuilder.addVertex(v1.x, v1.y, 10.0F).setColor(colour.x, colour.y, colour.z, colour.w).setUv(uvs.x, uvs.z).setLight(light);
        bufferBuilder.addVertex(v2.x, v2.y, 10.0F).setColor(colour.x, colour.y, colour.z, colour.w).setUv(uvs.x, uvs.w).setLight(light);
        bufferBuilder.addVertex(v3.x, v3.y, 10.0F).setColor(colour.x, colour.y, colour.z, colour.w).setUv(uvs.y, uvs.w).setLight(light);
        bufferBuilder.addVertex(v4.x, v4.y, 10.0F).setColor(colour.x, colour.y, colour.z, colour.w).setUv(uvs.y, uvs.z).setLight(light);

        //bufferBuilder.nextElement();
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        //tessellator.end();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }*/



}
