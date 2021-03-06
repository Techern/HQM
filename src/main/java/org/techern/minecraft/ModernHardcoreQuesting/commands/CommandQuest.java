package org.techern.minecraft.ModernHardcoreQuesting.commands;

import org.techern.minecraft.ModernHardcoreQuesting.quests.QuestingData;
import net.minecraft.command.ICommandSender;

public class CommandQuest extends CommandBase {

    public CommandQuest() {
        super("quest");
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments) {
        sendChat(sender, QuestingData.isQuestActive() ? "modernhardcorequesting.message.questAlreadyActivated" : "modernhardcorequesting.message.questActivated");
        QuestingData.activateQuest(true);
    }
}
