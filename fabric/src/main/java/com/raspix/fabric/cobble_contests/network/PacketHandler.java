package com.raspix.fabric.cobble_contests.network;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.lwjgl.system.windows.MSG;

import java.util.function.Supplier;

public class PacketHandler {

    /**public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(CobbleContestsFabric.MOD_ID, "main"))
            .clientAcceptedVersions(a -> true)
            .serverAcceptedVersions(a -> true)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();
    /**private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
                    new ResourceLocation(CobbleContestsfabric.MOD_ID, "main"))
            .serverAcceptedVersions((status, version) -> true)
            .clientAcceptedVersions((status, version) -> true)
            .networkProtocolVersion(1)
            .simpleChannel();*/



    /**public static void register() {
        int id = 0;
        /**INSTANCE.messageBuilder(SSendPartyPacket.class, id++)
                .encoder(SSendPartyPacket::encode)
                .decoder(SSendPartyPacket::new)
                .consumerNetworkThread(SSendPartyPacket.Handler::handle)
                .add();*/

        /**INSTANCE.messageBuilder(CBInfoScreenParty.class, id++)
                .encoder(CBInfoScreenParty::encode)
                .decoder(CBInfoScreenParty::new)
                .consumerNetworkThread(CBInfoScreenParty.Handler::handle)
                .add();

        INSTANCE.messageBuilder(SBWalletScreenParty.class, id++)
                .encoder(SBWalletScreenParty::encode)
                .decoder(SBWalletScreenParty::new)
                .consumerNetworkThread(SBWalletScreenParty.Handler::handle)
                .add();

        INSTANCE.messageBuilder(CBERunContest.class, id++)
                .encoder(CBERunContest::encode)
                .decoder(CBERunContest::new)
                .consumerNetworkThread(CBERunContest.Handler::handle)
                .add();
        /**INSTANCE.messageBuilder(SKeyPressSpawnEntityPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SKeyPressSpawnEntityPacket::encode)
                .decoder(SKeyPressSpawnEntityPacket::new)
                .consumerMainThread(SKeyPressSpawnEntityPacket::handle)
                .add();*/
    /**}

    public static void sendToServer(MSG msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);
    }

    /**public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(msg, PacketDistributor.PLAYER.with(player), msg);
    }*/

    /**public static void sendToAllClients(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }*/

    /**public static void sendToClient(Object msg, Supplier<ServerPlayer> player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(player), msg);
    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }*/
}
