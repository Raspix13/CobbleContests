package com.raspix.fabric.cobble_contests.menus.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.util.ParticleTypeData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.*;

public class ParticleScreenRenderer extends AbstractWidget {
    //private static final ResourceLocation MY_WHITE = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/heart.png");

    private int leftPos;
    private int topPos;

    private float MAX_AGE = 2f * 20f;

    private boolean shouldRender = false;
    private float particleAge = 0;
    private ParticleHolder currentParticles;



    public class ParticleHolder{

        public ParticleTypeData data;
        public float scale;
        int num_sprites;
        public List<Vector2f> spritePos;


        public ParticleHolder(ParticleTypeData data, float scale){
            this.data = data;
            this.scale = scale;
            RandomSource rand = Minecraft.getInstance().level.random;
            num_sprites = rand.nextInt(5 - 3 + 1) + 3;
            spritePos = new ArrayList<>();
            for(int i = 0; i < num_sprites; i++){
                spritePos.add(new Vector2f(2 +rand.nextFloat() * (76 - 2), 2 +rand.nextFloat() * (76 - 2)));
            }
        }


    }

    public ParticleScreenRenderer(int i, int j, int k, int l, Component component) {
        super(i, j, k, l, component);
        this.leftPos = i;
        this.topPos = j;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float particalTick) {
        if(shouldRender){
            particleAge += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
            System.out.println("Time: " + particleAge);

            //renderParticle(new Vector2f(this.leftPos, this.topPos), MY_WHITE, 16, 16, 5, 0, 16, 0, 16);
            renderParticleFromData(currentParticles);


            if(particleAge >= MAX_AGE || particleAge < 0){
                shouldRender = false;
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    private void renderParticleFromData(ParticleHolder holder){
        ParticleTypeData data = holder.data;

        for(int i = 0; i < holder.num_sprites; i++) {

            int uvsToUse = Math.min((int) (particleAge / MAX_AGE * data.uvs.size()), data.uvs.size() - 1);

            Vector2f addedPos = holder.spritePos.get(i);
            renderParticle(new Vector2f(this.leftPos + addedPos.x, this.topPos + addedPos.y), data.texture, data.textureSize, currentParticles.scale, data.uvs.get(uvsToUse));
        }
    }

    private void renderParticle(Vector2f screenPos, ResourceLocation imageLoc, Vector2i textureDims, float scale, Vector4f uvs){
        drawParticle(new Vector4f(1f, 1f, 1f, 1f),
                new Vector2f(-1, -1).mul(scale).add(screenPos), new Vector2f(-1, 1).mul(scale).add(screenPos),
                new Vector2f(1, 1).mul(scale).add(screenPos), new Vector2f(1, -1).mul(scale).add(screenPos),
                new Vector4f(uvs.x/textureDims.x, uvs.z/textureDims.x, uvs.y/textureDims.y, uvs.w/textureDims.y), 8, imageLoc); // vector4f(x, y, z, w)
    }

    /**
     * takes parameters for the particle and gives them to drawParticle
     * @param screenPos
     * @param imageLoc
     * @param textureWidth
     * @param textureHeight
     * @param scale
     * @param uvX1
     * @param uvX2
     * @param uvY1
     * @param uvY2
     */
    private void renderParticle(Vector2f screenPos, ResourceLocation imageLoc, int textureWidth, int textureHeight, float scale, float uvX1, float uvX2, float uvY1, float uvY2){
        drawParticle(new Vector4f(1f, 1f, 1f, 1f),
                new Vector2f(-1, -1).mul(scale).add(screenPos), new Vector2f(-1, 1).mul(scale).add(screenPos),
                new Vector2f(1, 1).mul(scale).add(screenPos), new Vector2f(1, -1).mul(scale).add(screenPos),
                new Vector4f(uvX1, uvX2/textureWidth, uvY1, uvY2/textureHeight), 8, imageLoc); // vector4f(x, y, z, w)
    }

    /**
     * Draws a single sprite of a particle
     * @param colour the color, should be 1, 1, 1, 1 for solid texture
     * @param v1 the bottom left vertex
     * @param v2 the top left vertex
     * @param v3 the top right vertex
     * @param v4 the bottom right vertex
     * @param uvs the uvs of the sprite, should be x1, x2, y1, y2
     * @param light the light level
     * @param imageLoc the resource location of the sprite
     */
    private void drawParticle(Vector4f colour, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4, Vector4f uvs, int light, ResourceLocation imageLoc) {

        System.out.println(uvs);
        RenderSystem.setShaderTexture(0, imageLoc);//CobblemonResources.INSTANCE.getWHITE());
        RenderSystem.setShaderColor(colour.x, colour.y, colour.z, colour.w);
        RenderSystem.depthMask(true);
        RenderSystem.enableBlend();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);//getBuilder();

        bufferBuilder.addVertex(v1.x, v1.y, 10.0F).setColor(colour.x, colour.y, colour.z, colour.w).setUv(uvs.x, uvs.z).setLight(light);
        bufferBuilder.addVertex(v2.x, v2.y, 10.0F).setColor(colour.x, colour.y, colour.z, colour.w).setUv(uvs.x, uvs.w).setLight(light);
        bufferBuilder.addVertex(v3.x, v3.y, 10.0F).setColor(colour.x, colour.y, colour.z, colour.w).setUv(uvs.y, uvs.w).setLight(light);
        bufferBuilder.addVertex(v4.x, v4.y, 10.0F).setColor(colour.x, colour.y, colour.z, colour.w).setUv(uvs.y, uvs.z).setLight(light);

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public void startParticleRender(){
        this.shouldRender = true;
        particleAge = 0;

        this.currentParticles = new ParticleHolder(ParticleTypeData.HEART_DATA, 5);
    }
}
