package com.raspix.fabric.cobble_contests.util;

import com.raspix.common.cobble_contests.CobbleContests;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.*;

public class ParticleTypeData {

    public String name;
    public ResourceLocation texture;
    public Vector2i textureSize;
    public List<Vector4f> uvs;


    public static ParticleTypeData HEART_DATA = new ParticleTypeData("heart",
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/particle/heart.png"), new Vector2i(16, 16),
            new ArrayList<>(Arrays.asList(new Vector4f(0, 0, 16, 16))));
    public static ParticleTypeData BUBBLE_POP = new ParticleTypeData("bubble_pop",
            ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/particle/bubble_pop.png"), new Vector2i(16, 80),
            new ArrayList<>(Arrays.asList(
                    new Vector4f(0, 0, 16, 16),
                    new Vector4f(0, 16, 16, 32),
                    new Vector4f(0, 32, 16, 48),
                    new Vector4f(0, 48, 16, 64),
                    new Vector4f(0, 64, 16, 80)
            )));
    public static ParticleTypeData DAMAGE_HEART_DATA = new ParticleTypeData("damage_heart",
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/particle/damage.png"), new Vector2i(8, 8),
            new ArrayList<>(Arrays.asList(new Vector4f(0, 0, 8, 8))));
    public static ParticleTypeData SWIRL = new ParticleTypeData("swirl",
            ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/particle/swirl.png"), new Vector2i(16, 32),
            new ArrayList<>(Arrays.asList(
                    new Vector4f(0, 0, 16, 16),
                    new Vector4f(0, 16, 16, 32)
            )));


    public final Map<String, ParticleTypeData> particleTypeData = new HashMap<String, ParticleTypeData>(){{
        put("heart", HEART_DATA);
        put("heart_2", BUBBLE_POP);
        put("damage_heart", DAMAGE_HEART_DATA);
        put("swirl", SWIRL);
    }};

    public ParticleTypeData(String spriteName, ResourceLocation texture, Vector2i textSize, List<Vector4f> uvs){
        name = spriteName;
        this.texture = texture;
        this.textureSize = textSize;
        this.uvs = uvs;
    }

}
