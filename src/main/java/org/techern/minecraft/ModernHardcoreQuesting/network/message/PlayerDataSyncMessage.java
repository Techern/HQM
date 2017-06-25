package org.techern.minecraft.ModernHardcoreQuesting.network.message;

import org.techern.minecraft.ModernHardcoreQuesting.io.SaveHandler;
import org.techern.minecraft.ModernHardcoreQuesting.quests.QuestLine;
import org.techern.minecraft.ModernHardcoreQuesting.quests.QuestingData;
import org.techern.minecraft.ModernHardcoreQuesting.team.Team;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.io.PrintWriter;

public class PlayerDataSyncMessage implements IMessage {

    private boolean local, serverWorld, questing, hardcore;
    private String team, data;

    public PlayerDataSyncMessage() {

    }

    public PlayerDataSyncMessage(boolean local, boolean serverWorld, EntityPlayer player) {
        this.local = local;
        this.serverWorld = serverWorld;
        this.questing = QuestingData.isQuestActive();
        this.hardcore = QuestingData.isHardcoreActive();
        this.team = Team.saveTeam(player);
        this.data = QuestingData.saveQuestingData(player);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.local = buf.readBoolean();
        this.serverWorld = buf.readBoolean();
        this.questing = buf.readBoolean();
        this.hardcore = buf.readBoolean();
        int size = buf.readInt();
        this.team = new String(buf.readBytes(size).array());
        size = buf.readInt();
        this.data = new String(buf.readBytes(size).array());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.local);
        buf.writeBoolean(this.serverWorld);
        buf.writeBoolean(this.questing);
        buf.writeBoolean(this.hardcore);
        buf.writeInt(this.team.getBytes().length);
        buf.writeBytes(this.team.getBytes());
        buf.writeInt(this.data.getBytes().length);
        buf.writeBytes(this.data.getBytes());
    }

    public static class Handler implements IMessageHandler<PlayerDataSyncMessage, IMessage> {

        @Override
        public IMessage onMessage(PlayerDataSyncMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PlayerDataSyncMessage message, MessageContext ctx) {
            if (!QuestLine.doServerSync) // Copy defaults when server sync is off
                SaveHandler.copyFolder(SaveHandler.getDefaultFolder(), SaveHandler.getRemoteFolder());
            try {
                try (PrintWriter out = new PrintWriter(SaveHandler.getRemoteFile("teams"))) {
                    out.print("[");
                    out.print(message.team);
                    out.print("]");
                }
                try (PrintWriter out = new PrintWriter(SaveHandler.getRemoteFile("data"))) {
                    out.print("[");
                    out.print(message.data);
                    out.print("]");
                }
                try (PrintWriter out = new PrintWriter(SaveHandler.getRemoteFile("state"))) {
                    out.print(SaveHandler.saveQuestingState(message.questing, message.hardcore));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            QuestLine.receiveServerSync(message.local, message.serverWorld);
        }
    }
}
