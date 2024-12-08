package com.raspix.fabric.cobble_contests.network;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.pokemon.CVs;
import com.raspix.fabric.cobble_contests.pokemon.Ribbons;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

import java.util.List;
import java.util.UUID;

public class SBWalletScreenParty {

    public static void recieve(MinecraftServer server, Player player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender sender) {
        System.out.println("Recieving SBWallet");
        PlayerPartyStore pps = null;
        CompoundTag tag = new CompoundTag();
        try {
            UUID id = buf.readUUID();
            pps = Cobblemon.INSTANCE.getStorage().getParty(id);
            List<Pokemon> poke = pps.toGappyList();
            for(int i = 0; i < 6; i++){
                if(poke.size() > i && poke.get(i) != null){
                    CompoundTag pers = poke.get(i).getPersistentData();
                    tag.put("poke" +i, pers.getCompound("CVs"));
                    tag.put("poke" +i + "ribbons", pers.getCompound("Ribbons"));
                }else{
                    tag.put("poke" +i, new CVs().saveToNBT());
                    tag.put("poke" +i + "ribbons", new Ribbons().saveToNBT());
                }

            }
            if (player != null && player instanceof ServerPlayer serverPlayer) {
                FriendlyByteBuf bufi = new FriendlyByteBuf(Unpooled.buffer());
                //bufi.writeUUID(id);
                bufi.writeNbt(tag);
                ServerPlayNetworking.send(serverPlayer, MessagesInit.WALLET_ID_2, bufi);//.sendToClient(new CBWalletScreenParty(id, tag), () -> serverPlayer);
            }

        } catch (NoPokemonStoreException e) {
            throw new RuntimeException(e);
        }catch (NullPointerException e){

        }
    }


    public final UUID id;

    public SBWalletScreenParty(UUID id){
        this.id = id;
    }

    public SBWalletScreenParty(FriendlyByteBuf buf){
        this(buf.readUUID());
    }

    /**public static void sendToServer(String message) {
        ClientPlayNetworking.send(new ResourceLocation("modid", "custom_message"), new FriendlyByteBuf(Unpooled.buffer()).writeString(message));
    }

    public static void sendToClient(Player player, String message) {
        ServerPlayNetworking.send(player, new ResourceLocation("modid", "custom_message"), new FriendlyByteBuf(Unpooled.buffer()).writeString(message));
    }*/

    public static SBWalletScreenParty read(FriendlyByteBuf buf) {
        return new SBWalletScreenParty(buf.readUUID());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.id);
    }

    public void handle(MinecraftServer server, Player player, ServerGamePacketListenerImpl handler, PacketSender sender) {

    }


    /**public static class Handler {
        public static boolean handle(SBWalletScreenParty packet, Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            context.get().enqueueWork(() -> {
                Player player = ctx.getSender();// context.get().getSender();
                PlayerPartyStore pps = null;
                CompoundTag tag = new CompoundTag();
                try {
                    pps = Cobblemon.INSTANCE.getStorage().getParty(packet.id);
                    List<Pokemon> poke = pps.toGappyList();
                    for(int i = 0; i < 6; i++){
                        if(poke.size() > i && poke.get(i) != null){
                            CompoundTag pers = poke.get(i).getPersistentData();
                            tag.put("poke" +i, pers.getCompound("CVs"));
                            tag.put("poke" +i + "ribbons", pers.getCompound("Ribbons"));
                        }else{
                            tag.put("poke" +i, new CVs().saveToNBT());
                            tag.put("poke" +i + "ribbons", new Ribbons().saveToNBT());
                        }

                    }
                    if (player != null && player instanceof ServerPlayer serverPlayer) {
                        PacketHandler.sendToClient(new CBInfoScreenParty(packet.id, tag), () -> serverPlayer);

                    }

                } catch (NoPokemonStoreException e) {
                    throw new RuntimeException(e);
                }catch (NullPointerException e){

                }

            });
            return true;
        }

    }*/
}

// Define a custom packet class
/**public class CustomMessagePacket implements Packet {
    private String message;

    public CustomMessagePacket(String message) {
        this.message = message;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void handle(PacketListener packetListener) {

    }


}*/
