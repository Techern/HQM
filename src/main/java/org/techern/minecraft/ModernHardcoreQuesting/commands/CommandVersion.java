package org.techern.minecraft.ModernHardcoreQuesting.commands;

import org.techern.minecraft.ModernHardcoreQuesting.ModInformation;
import org.techern.minecraft.ModernHardcoreQuesting.util.Translator;
import net.minecraft.command.ICommandSender;

public class CommandVersion extends CommandBase {

    public CommandVersion() {
        super("version");
        permissionLevel = 0;
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments) {
        sendChat(sender, "\u00A7a" + Translator.translate("modernhardcorequesting.message.version", ModInformation.VERSION));
    }
}
