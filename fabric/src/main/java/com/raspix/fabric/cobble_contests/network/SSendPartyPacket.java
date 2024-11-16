package com.raspix.fabric.cobble_contests.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SSendPartyPacket {

    //private final PlayerPartyStore playerPartyStore;
    private final UUID id;
    private final BlockPos pos;

    public SSendPartyPacket(UUID id, BlockPos pos){
        this.id = id;
        this.pos = pos;
    }

    public SSendPartyPacket(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readBlockPos());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeUUID(this.id);
    }


    public static class Handler {
        public static void handle(SSendPartyPacket packet, Supplier<NetworkEvent.Context> context) {
            System.out.println("hello");
            /**context.get().enqueueWork(()-> {
                ServerPlayer player = context.get().getSender();
                if (player != null) {
                    Inventory playerInv = player.getInventory();
                    try {
                        PlayerPartyStore partyStore = Cobblemon.INSTANCE.getStorage().getParty(packet.id);
                        Minecraft.getInstance().setScreen(new SecondTestScreen(new ContestMenu(player.containerCounter, playerInv, player.level().getBlockEntity(packet.pos)), playerInv, Component.translatable("container." + CobbleContests.MOD_ID + ".contest_block"), partyStore));
                    } catch (NoPokemonStoreException e) {
                        throw new RuntimeException(e);
                    }

                }
            });*/



            /**NetworkEvent.Context ctx = context.get();
            try{
                PlayerPartyStore partyStore = Cobblemon.INSTANCE.getStorage().getParty(packet.id);
                //Minecraft.getInstance().setScreen(new TestMenuScreen(null, null, Component.translatable("cobble_contests.ui.contest"), partyStore));
                Minecraft.getInstance().setScreen(new SecondTestScreen(Component.translatable("cobble_contests.ui.contest"), partyStore));
            }catch (NoPokemonStoreException e){
                System.out.println("No pokemon store");
            }*/

            //MinecraftClient.getInstance().setScreen(PCGUI(pc, CobblemonClient.storage.myParty, PCGUIConfiguration()))
        }

    }



}
