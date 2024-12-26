package com.raspix.fabric.cobble_contests.network;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

//https://fabricmc.net/2024/04/19/1205.html#:%7E:text=is%20already%20broken
//https://wiki.fabricmc.net/tutorial:networking#networking_in_1205
//

public class MessagesInit {
    public static final ResourceLocation CHANNEL_ID = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "example");
    public static final ResourceLocation WALLET_ID_1 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "wallet_conditions");
    public static final ResourceLocation WALLET_ID_2 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "wallet_conditions2");
    public static final ResourceLocation CONTEST_BOOTH = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contest_booth");
    public static final ResourceLocation RUN_CONTEST = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "run_contest");
    //public static final ResourceLocation BOOTH_ID_2 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "booth_2");



    public static void registerC2SPackets() { // client to server

        //SERVERBOUND
        PayloadTypeRegistry.playC2S().register(SBWalletScreenParty.PACKET_ID, SBWalletScreenParty.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBRunContest.PACKET_ID, SBRunContest.PACKET_CODEC);

        //CLIENTBOUND
        PayloadTypeRegistry.playS2C().register(CBWalletScreenParty.PACKET_ID, CBWalletScreenParty.PACKET_CODEC);


        ServerPlayNetworking.registerGlobalReceiver(SBWalletScreenParty.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(SBRunContest.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
    }

    public static void registerS2CPackets(){ //server to client
        ClientPlayNetworking.registerGlobalReceiver(CBWalletScreenParty.PACKET_ID, (payload, context) -> {
            // \] # written by cat (Parix), do not delete, she was helping
            payload.recieve(context.client());

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
