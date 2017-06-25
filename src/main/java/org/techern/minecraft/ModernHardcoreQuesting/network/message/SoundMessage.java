package org.techern.minecraft.ModernHardcoreQuesting.network.message;

import org.techern.minecraft.ModernHardcoreQuesting.HardcoreQuesting;
import org.techern.minecraft.ModernHardcoreQuesting.client.ClientChange;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SoundMessage implements IMessage {

    private ClientChange update;
    private String data;

    public SoundMessage() {
    }

    public SoundMessage(ClientChange update, String data) {
        this.update = update;
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.update = ClientChange.values()[buf.readInt()];
        int size = buf.readInt();
        this.data = new String(buf.readBytes(size).array());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.update.ordinal());
        buf.writeInt(this.data.getBytes().length);
        buf.writeBytes(this.data.getBytes());
    }

    public static class Handler implements IMessageHandler<SoundMessage, IMessage> {

        @Override
        public IMessage onMessage(SoundMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(SoundMessage message, MessageContext ctx) {
            message.update.parse(HardcoreQuesting.proxy.getPlayer(ctx), message.data);
        }
    }
}
