package org.techern.minecraft.ModernHardcoreQuesting.quests.task;

import org.techern.minecraft.ModernHardcoreQuesting.event.EventHandler;
import org.techern.minecraft.ModernHardcoreQuesting.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;

public class QuestTaskReputationTarget extends QuestTaskReputation {

    public QuestTaskReputationTarget(Quest parent, String description, String longDescription) {
        super(parent, description, longDescription, 0);

        register(EventHandler.Type.OPEN_BOOK, EventHandler.Type.REPUTATION_CHANGE);
    }

    private void checkReputation(EntityPlayer player) {
        if (parent.isEnabled(player) && parent.isAvailable(player) && this.isVisible(player) && !this.isCompleted(player)) {
            if (isPlayerInRange(player)) {
                completeTask(player.getUniqueID());
                parent.sendUpdatedDataToTeam(player);
            }

        }
    }

    @Override
    public void onUpdate(EntityPlayer player) {

    }

    @Override
    public void onOpenBook(EventHandler.BookOpeningEvent event) {
        checkReputation(event.getPlayer());
    }

    @Override
    public void onReputationChange(EventHandler.ReputationEvent event) {
        checkReputation(event.getPlayer());
    }
}
