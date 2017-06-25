package org.techern.minecraft.ModernHardcoreQuesting.commands;

import org.techern.minecraft.ModernHardcoreQuesting.quests.QuestingData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandEnable extends CommandBase {

    public CommandEnable() {
        super("enable");
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments) {
        QuestingData.disableVanillaHardcore(sender);
        if (sender.getServer().getEntityWorld().getWorldInfo().isHardcoreModeEnabled())
            sendChat(sender, "modernhardcorequesting.message.vanillaHardcoreOn");
        else
            sendChat(sender, QuestingData.isHardcoreActive() ? "modernhardcorequesting.message.hardcoreAlreadyActivated" : "modernhardcorequesting.message.questHardcore");
        sendChat(sender, QuestingData.isQuestActive() ? "modernhardcorequesting.message.questAlreadyActivated" : "modernhardcorequesting.message.questActivated");
        QuestingData.activateHardcore();
        QuestingData.activateQuest(true);
        if (QuestingData.isHardcoreActive() && sender instanceof EntityPlayer)
            currentLives((EntityPlayer) sender);
    }
}
