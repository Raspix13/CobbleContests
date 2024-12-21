package com.raspix.fabric.cobble_contests.network;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.resources.ResourceLocation;

//https://fabricmc.net/2024/04/19/1205.html#:%7E:text=is%20already%20broken
//https://wiki.fabricmc.net/tutorial:networking#networking_in_1205
//

public class MessagesInit {
    public static final ResourceLocation CHANNEL_ID = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "example");
    public static final ResourceLocation WALLET_ID_1 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "wallet_conditions");
    public static final ResourceLocation WALLET_ID_2 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "wallet_conditions2");
    public static final ResourceLocation CONTEST_BOOTH = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contest_booth");
    public static final ResourceLocation RUN_CONTEST_ID = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "run_contest");
    public static final ResourceLocation BOOTH_ID_2 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "booth_2");



    public static void registerC2SPackets() { // client to server
        //ServerPlayNetworking.registerGlobalReceiver(WALLET_ID_1, SBWalletScreenParty::recieve);
        //ClientPlayNetworking.registerGlobalReceiver(WALLET_ID_2, CBWalletScreenParty::recieve);
        //ClientPlayNetworking.registerGlobalReceiver(RUN_CONTEST_ID, CBERunContest::recieve);
        //ServerPlayNetworking.registerGlobalReceiver(RUN_CONTEST_ID, CBERunContest::recieve);
        //ServerPlayNetworking.registerGlobalReceiver(BOOTH_ID_2, SBBoothScreen::recieve);

        //PayloadTypeRegistry.playC2S().register(SBWalletScreenParty.PACKET_ID, SBWalletScreenParty.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBWalletScreenParty.PACKET_ID, SBWalletScreenParty.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBWalletScreenParty.PACKET_ID, CBWalletScreenParty.PACKET_CODEC);
        //PayloadTypeRegistry.playS2C().register(SBWalletScreenParty.PACKET_ID, SBWalletScreenParty.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SBWalletScreenParty.PACKET_ID, (payload, context) -> {
            //System.out.println("UM: " + context.getClass());
            //System.out.println("UM2: " + payload.getClass());
            payload.recieve(context.server(), context.player());
            //var world = context.player().getServerWorld();
        });
    }

    public static void registerS2CPackets(){ //server to client
        //ClientPlayNetworking.registerGlobalReceiver(WALLET_ID_2, CBWalletScreenParty::recieve);
        ClientPlayNetworking.registerGlobalReceiver(CBWalletScreenParty.PACKET_ID, (payload, context) -> {
                    //System.out.println("UM: " + context.getClass());
                    //System.out.println("UM2: " + payload.getClass());
                    payload.recieve(context.client());
                    //var world = context.player().getServerWorld();
        });

    }

    /**public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(WALLET_ID, (server, player, handler, buf, responseSender) -> {
            SBWalletScreenParty packet = SBWalletScreenParty.read(buf);//new SBWalletScreenParty();
            //packet.read(buf);
            server.execute(() -> packet.handle(server, player, handler, responseSender));
        });
    }*/




    // Register the packet
    /**public class ModInitializer implements ModInitializer {
        private static final Identifier CUSTOM_MESSAGE_PACKET_ID = new Identifier("modid", "custom_message");

        @Override
        public void onInitialize() {
            ServerPlayNetworking.registerGlobalReceiver(CUSTOM_MESSAGE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
                CustomMessagePacket packet = new CustomMessagePacket();
                packet.read(buf);
                server.execute(() -> packet.handle(server, player, handler, responseSender));
            });
        }
    }*/

}
