package com.raspix.fabric.cobble_contests.network.CB;

import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.util.ContestManagerClient;
import com.raspix.fabric.cobble_contests.util.ContestMessagePane;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CBSendContestantMessage implements CustomPacketPayload{


    public final UUID id;
    private final List<Component> messages;

    public static final CustomPacketPayload.Type<CBSendContestantMessage> PACKET_ID = new CustomPacketPayload.Type<>(MessagesInit.CONTESTENT_MESSAGE_SENDER);
    public static final StreamCodec<FriendlyByteBuf, CBSendContestantMessage> PACKET_CODEC = new StreamCodec<FriendlyByteBuf, CBSendContestantMessage>() {
        @Override
        public @NotNull CBSendContestantMessage decode(FriendlyByteBuf buf) {
            //CompoundTag compoundTag = new CompoundTag();
            return new CBSendContestantMessage(FriendlyByteBuf.readUUID(buf), buf.readList(((FriendlyByteBuf buffer) -> ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.decode(buf))));
            //return new CBSendContestantMessage(FriendlyByteBuf.readUUID(buf), FriendlyByteBuf.readNbt(buf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, CBSendContestantMessage packet) {
            FriendlyByteBuf.writeUUID(buf, packet.getId());
            buf.writeCollection(packet.getMessages(), (pb, value) -> ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.encode(buf, value));
        }
    };

    public UUID getId(){
        return id;
    }
    
    public List<Component> getMessages(){
        return messages;
    }


    public void recieve(Minecraft minecraft){
        System.out.println("Recieving SendContestantMessage");

        Font textRenderer = Minecraft.getInstance().font;

        for (Component message : messages) {
            MutableComponent line = message.copy().withStyle(message.copy().getStyle().withFont(ResourceLocation.parse("uniform")).withBold(true));//.withStyle(ChatFormatting.BOLD)
            List<FormattedCharSequence> lines = Language.getInstance().getVisualOrder(textRenderer.getSplitter().splitLines(line, ContestMessagePane.LINE_WIDTH, line.getStyle()));
            ContestManagerClient.INSTANCE.getContestantMessages(id).add(lines);
        }
    }


    public CBSendContestantMessage(UUID id, List<Component> messages){
        this.id = id;
        this.messages = messages;
    }

    public CBSendContestantMessage(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readList(((FriendlyByteBuf buffer) -> ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.decode(buf))));
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }


}
