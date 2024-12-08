package com.raspix.fabric.cobble_contests.network;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.resources.ResourceLocation;

public class MessagesInit {
    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(CobbleContestsFabric.MOD_ID, "example");
    public static final ResourceLocation WALLET_ID_1 = new ResourceLocation(CobbleContestsFabric.MOD_ID, "wallet_1");
    public static final ResourceLocation WALLET_ID_2 = new ResourceLocation(CobbleContestsFabric.MOD_ID, "wallet_2");
    public static final ResourceLocation RUN_CONTEST_ID = new ResourceLocation(CobbleContestsFabric.MOD_ID, "run_contest");
    public static final ResourceLocation BOOTH_ID_2 = new ResourceLocation(CobbleContestsFabric.MOD_ID, "booth_2");



    public static void registerC2SPackets(){ // client to server
        ServerPlayNetworking.registerGlobalReceiver(WALLET_ID_1, SBWalletScreenParty::recieve);
        //ClientPlayNetworking.registerGlobalReceiver(WALLET_ID_2, CBWalletScreenParty::recieve);
        //ClientPlayNetworking.registerGlobalReceiver(RUN_CONTEST_ID, CBERunContest::recieve);
        ServerPlayNetworking.registerGlobalReceiver(RUN_CONTEST_ID, CBERunContest::recieve);
        //ServerPlayNetworking.registerGlobalReceiver(BOOTH_ID_2, SBBoothScreen::recieve);
    }

    public static void registerS2CPackets(){ //server to client
        ClientPlayNetworking.registerGlobalReceiver(WALLET_ID_2, CBWalletScreenParty::recieve);
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
