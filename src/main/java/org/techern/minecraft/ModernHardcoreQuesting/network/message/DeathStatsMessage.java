package org.techern.minecraft.ModernHardcoreQuesting.network.message;

import org.techern.minecraft.ModernHardcoreQuesting.death.DeathStats;
import org.techern.minecraft.ModernHardcoreQuesting.io.SaveHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.io.PrintWriter;

public class DeathStatsMessage implements IMessage {

    private boolean local;
    private String deaths;

    public DeathStatsMessage() {
    }

    public DeathStatsMessage(boolean local) {
        this.local = local;
        if (local) DeathStats.saveAll();
        this.deaths = SaveHandler.saveDeaths();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.local = buf.readBoolean();
        if (this.local) return;
        int size = buf.readInt();
        this.deaths = new String(buf.readBytes(size).array());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.local);
        if (local) return;
        buf.writeInt(this.deaths.getBytes().length);
        buf.writeBytes(this.deaths.getBytes());
    }

    public static class Handler implements IMessageHandler<DeathStatsMessage, IMessage> {

        @Override
        public IMessage onMessage(DeathStatsMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(DeathStatsMessage message, MessageContext ctx) {
            if (!message.local) {
                try (PrintWriter out = new PrintWriter(SaveHandler.getRemoteFile("deaths"))) {
                    out.print(message.deaths);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            DeathStats.loadAll(true, !message.local);
        }
    }
}
