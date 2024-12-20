package com.raspix.fabric.cobble_contests.network;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import com.raspix.fabric.cobble_contests.pokemon.CVs;
import com.raspix.fabric.cobble_contests.pokemon.Ribbons;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import org.intellij.lang.annotations.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class SBWalletScreenParty implements CustomPacketPayload {//ServerPlayNetworking.PlayPayloadHandler {//


    public static final CustomPacketPayload.Type<SBWalletScreenParty> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.WALLET_ID_1);
    public static final StreamCodec<FriendlyByteBuf, SBWalletScreenParty> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, SBWalletScreenParty>() {
        @Override
        public @NotNull SBWalletScreenParty decode(FriendlyByteBuf buf) {
            return new SBWalletScreenParty(FriendlyByteBuf.readUUID(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, SBWalletScreenParty walletScreenParty) {
            FriendlyByteBuf.writeUUID(buf, walletScreenParty.getId());
        }
    };//StreamCodec.of((StreamEncoder)SBWalletScreenParty::new, SBWalletScreenParty::recieve).cast();

    private static FriendlyByteBuf recieve(FriendlyByteBuf buf) {
        System.out.println("recieve in SBWalletScreenParty was called");
        if(buf == null){
            System.out.println("buf is null");
        }else {
            System.out.println("buf is not null");
            System.out.println("buf is " + buf);
        }
        return buf;
    }

    public final UUID id;

    public SBWalletScreenParty(FriendlyByteBuf buf) {
        id = buf.readUUID();
    }

    public UUID getId(){
        return id;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.id);
    }

    public SBWalletScreenParty(UUID id){
        this.id = id;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }


    public void recieve(MinecraftServer server, Player player) {
        System.out.println("Recieving SBWallet");
        PlayerPartyStore pps = null;
        CompoundTag tag = new CompoundTag();
        try {
            pps = Cobblemon.INSTANCE.getStorage().getParty((ServerPlayer) player);
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
                ServerPlayNetworking.send(serverPlayer, new CBWalletScreenParty(id, tag));//MessagesInit.WALLET_ID_2, bufi);//.sendToClient(new CBWalletScreenParty(id, tag), () -> serverPlayer);
            }

        } catch (NullPointerException e){

            System.out.println(e.getMessage());
        }
    }


    /**public static void recieve(MinecraftServer server, Player player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender sender) {
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
    }*/


    //public final UUID id;



    /**public SBWalletScreenParty(FriendlyByteBuf buf){
        this(buf.readUUID());
    }*/

    /**public static void sendToServer(String message) {
        ClientPlayNetworking.send(new ResourceLocation("modid", "custom_message"), new FriendlyByteBuf(Unpooled.buffer()).writeString(message));
    }

    public static void sendToClient(Player player, String message) {
        ServerPlayNetworking.send(player, new ResourceLocation("modid", "custom_message"), new FriendlyByteBuf(Unpooled.buffer()).writeString(message));
    }*/




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
