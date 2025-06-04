package com.raspix.fabric.cobble_contests.network.CB;

import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Arrays;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class CBSendPlayersParticles implements CustomPacketPayload {

    public int id;
    public String loc;
    public Vector3f position;

    public static final CustomPacketPayload.Type<CBSendPlayersParticles> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.PARTICLE_SENDER);
    public static final StreamCodec<FriendlyByteBuf, CBSendPlayersParticles> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBSendPlayersParticles>() {
        @Override
        public @NotNull CBSendPlayersParticles decode(FriendlyByteBuf buf) {
            CompoundTag tag = FriendlyByteBuf.readNbt(buf);
            return new CBSendPlayersParticles(tag.getInt("id"), tag.getString("location"), new Vector3f(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z")));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBSendPlayersParticles contestInfo) {
            //FriendlyByteBuf.(buf, contestInfo.getId());
            CompoundTag tag = new CompoundTag();
            tag.putFloat("id", contestInfo.getId());
            tag.putString("location", contestInfo.getLoc());
            Vector3f pos = contestInfo.getPosition();
            tag.putFloat("x", pos.x);
            tag.putFloat("y", pos.y);
            tag.putFloat("z", pos.z);
            FriendlyByteBuf.writeNbt(buf, tag);
        }
    };

    public int getId(){
        return id;
    }

    public String getLoc(){
        return loc;
    }

    public Vector3f getPosition(){
        return position;
    }

    public CBSendPlayersParticles(int id, String location, Vector3f pos) {
        this.id = id;
        this.loc = location;
        this.position = pos;
    }


    public void recieve(Minecraft minecraft){
        System.out.println("Recieving particleSender");
        new SpawnSnowstormEntityParticlePacket(cobblemonResource(getLoc()), getId(), Arrays.asList())
                .sendToPlayersAround(position.x, position.y, position.z, 64.0, minecraft.level.dimension(), serverPlayer -> {
                    return false;
                });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
