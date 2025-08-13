package com.raspix.fabric.cobble_contests.network;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import com.raspix.fabric.cobble_contests.network.CB.*;
import com.raspix.fabric.cobble_contests.network.SB.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

//https://fabricmc.net/2024/04/19/1205.html#:%7E:text=is%20already%20broken
//https://wiki.fabricmc.net/tutorial:networking#networking_in_1205
//

public class MessagesInit {
    //public static final ResourceLocation CHANNEL_ID = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "example");
    public static final ResourceLocation WALLET_ID_1 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "wallet_conditions"); // Used by wallet to request pokemon info
    public static final ResourceLocation WALLET_ID_2 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "wallet_conditions2");  // Response to wallet request for pokemon info
    public static final ResourceLocation CONTEST_BOOTH = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contest_booth"); // Does block position stuff? TODO: figure out what this is
    public static final ResourceLocation RUN_CONTEST = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "run_contest"); // (Unsure) Sends from contest booth to start contest
    public static final ResourceLocation CONTESTANT_STATUS_REQ = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contestant_status_req"); // Sent by contestbooth to check if player is already in contest
    public static final ResourceLocation CONTESTANT_STATUS_REP = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contestant_status_rep"); // Response to contestbooth to tell if player is already in contest
    public static final ResourceLocation CONTEST_UPDATE_1 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contest_update_1"); // Sent by contest screen to get updates
    public static final ResourceLocation CONTEST_UPDATE_2 = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contest_update_2"); // Response to contest screen for contest updates
    public static final ResourceLocation PARTICLE_SENDER = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "particle_sender"); //
    public static final ResourceLocation CONTEST_HOST_STARTER= ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contest_host_sender"); // Used by contest hosts to start contests
    public static final ResourceLocation CONTESTENT_MESSAGE_SENDER= ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "contest_message_sender"); // Used to send messages to the client during contests
    public static final ResourceLocation CONBOOTHSCREEN_REQ_HOSTLIST = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "conbooth_req_hostlist"); // Used by the contest booth screen to request a list of the hosts, CBHostListToConBoothScreen
    public static final ResourceLocation HOSTLIST_TO_CONBOOTHSCREEN = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "hostlist_conbooth"); // a return package to give the contest booth screen the contest list, SBConBoothScrReqHostList
    public static final ResourceLocation REQ_JOIN_LOB = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "req_join_lob"); // a package to add a player to a host's lobby, SBReqJoinLob
    public static final ResourceLocation LOB_RET_REQ = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "lob_ret_req"); // a package to let the player know if they can join and update their screen, or not join
    public static final ResourceLocation USE_MOVE = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "use_move"); // a package to have a player choose a contest move
    public static final ResourceLocation ACCEPT_MOVE = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "accept_move"); // a package to let the player client know their move was used
    public static final ResourceLocation CLEAR_QUEUE = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "clear_message_queue"); // clears the message queue for a player
    public static final ResourceLocation ALERT_START = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "alert_start"); // lets all contestants know contest is starting if in booth

    public static final ResourceLocation SEND_CONTESTANTS = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "send_contestants"); // sends the pokemon and contestant data to the client
    public static final ResourceLocation REQUEST_CONTESTANTS = ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, "request_contestants"); // sends the pokemon and contestant data to the client


    public static void registerC2SPackets() { // client to server

        //SERVERBOUND
        PayloadTypeRegistry.playC2S().register(SBWalletScreenParty.PACKET_ID, SBWalletScreenParty.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBRunContest.PACKET_ID, SBRunContest.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBRunHostedContest.PACKET_ID, SBRunHostedContest.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBCheckContestParticipation.PACKET_ID, SBCheckContestParticipation.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBUpdateContestInfo.PACKET_ID, SBUpdateContestInfo.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBConBoothScrReqHostList.PACKET_ID, SBConBoothScrReqHostList.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBReqJoinLob.PACKET_ID, SBReqJoinLob.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBUseContestMove.PACKET_ID, SBUseContestMove.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SBShowoffRequestContestantInfo.PACKET_ID, SBShowoffRequestContestantInfo.PACKET_CODEC);

        //CLIENTBOUND
        PayloadTypeRegistry.playS2C().register(CBWalletScreenParty.PACKET_ID, CBWalletScreenParty.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBReplyContestParticipation.PACKET_ID, CBReplyContestParticipation.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBUpdateContestInfo.PACKET_ID, CBUpdateContestInfo.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBSendPlayersParticles.PACKET_ID, CBSendPlayersParticles.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBSendContestantMessage.PACKET_ID, CBSendContestantMessage.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBHostListToConBoothScreen.PACKET_ID, CBHostListToConBoothScreen.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBLobRetReq.PACKET_ID, CBLobRetReq.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBPlayerMoveAccepted.PACKET_ID, CBPlayerMoveAccepted.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBClearMessageQueue.PACKET_ID, CBClearMessageQueue.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBSendContestantStatus.PACKET_ID, CBSendContestantStatus.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(CBAlertContestStarting.PACKET_ID, CBAlertContestStarting.PACKET_CODEC);


        ServerPlayNetworking.registerGlobalReceiver(SBWalletScreenParty.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(SBRunContest.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(SBRunHostedContest.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(SBCheckContestParticipation.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(SBUpdateContestInfo.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(SBConBoothScrReqHostList.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(SBReqJoinLob.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(SBUseContestMove.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(SBShowoffRequestContestantInfo.PACKET_ID, (payload, context) -> {
            payload.recieve(context.server(), context.player());
        });
    }

    public static void registerS2CPackets(){ //server to client
        ClientPlayNetworking.registerGlobalReceiver(CBWalletScreenParty.PACKET_ID, (payload, context) -> {
            // \] # written by cat (Parix), do not delete, she was helping <3
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBReplyContestParticipation.PACKET_ID, (payload, context) -> {
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBUpdateContestInfo.PACKET_ID, (payload, context) -> {
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBSendPlayersParticles.PACKET_ID, (payload, context) -> {
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBSendContestantMessage.PACKET_ID, (payload, context) -> {
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBHostListToConBoothScreen.PACKET_ID, (payload, context) -> {
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBLobRetReq.PACKET_ID, (payload, context) -> {
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBPlayerMoveAccepted.PACKET_ID, (payload, context) -> {
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBClearMessageQueue.PACKET_ID, (payload, context) -> {
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBSendContestantStatus.PACKET_ID, (payload, context) -> {
            payload.recieve(context.client());
        });
        ClientPlayNetworking.registerGlobalReceiver(CBAlertContestStarting.PACKET_ID, (payload, context) -> {
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
