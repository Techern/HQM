package org.techern.minecraft.ModernHardcoreQuesting.commands;

import org.techern.minecraft.ModernHardcoreQuesting.items.ItemQuestBook;
import org.techern.minecraft.ModernHardcoreQuesting.quests.QuestingData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandEdit extends CommandBase {

    public CommandEdit() {
        super("edit");
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments) {
        if (sender instanceof EntityPlayer && isPlayerOp(sender)) {
            EntityPlayer player = (EntityPlayer) sender;
            if (arguments.length == 1)
                player = sender.getEntityWorld().getPlayerEntityByName(arguments[0]);
            if (QuestingData.hasData(player)) {
                player.inventory.addItemStackToInventory(ItemQuestBook.getOPBook(player));
            } else {
                sendChat(player, "hqm.message.noPlayer");
            }
        }
    }
}
