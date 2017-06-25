package org.techern.minecraft.ModernHardcoreQuesting.team;

import org.techern.minecraft.ModernHardcoreQuesting.network.NetworkManager;
import org.techern.minecraft.ModernHardcoreQuesting.network.message.TeamErrorMessage;
import org.techern.minecraft.ModernHardcoreQuesting.util.Translator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public enum TeamError {
    INVALID_PLAYER("modernhardcorequesting.team.invalidPlayer.title", "modernhardcorequesting.team.invalidPlayer.desc"),
    IN_PARTY("modernhardcorequesting.team.playerInParty.title", "modernhardcorequesting.team.playerInParty.desc"),
    USED_NAME("modernhardcorequesting.team.usedTeamName.title", "modernhardcorequesting.team.usedTeamName.desc");

    //slightly ugly but there's no real way of getting hold of the interface, this works perfectly fine
    public static TeamError latestError;
    private String header;
    private String message;

    TeamError(String header, String message) {
        this.message = message;
        this.header = header;
    }

    public String getMessage() {
        return Translator.translate(message);
    }

    public String getHeader() {
        return Translator.translate(header);
    }

    public void sendToClient(EntityPlayer player) {
        if (player instanceof EntityPlayerMP)
            NetworkManager.sendToPlayer(new TeamErrorMessage(this), (EntityPlayerMP) player);
    }
}
