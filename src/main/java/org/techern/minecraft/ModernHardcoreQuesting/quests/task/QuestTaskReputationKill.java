package org.techern.minecraft.ModernHardcoreQuesting.quests.task;

import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiColor;
import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiQuestBook;
import org.techern.minecraft.ModernHardcoreQuesting.event.EventHandler;
import org.techern.minecraft.ModernHardcoreQuesting.quests.Quest;
import org.techern.minecraft.ModernHardcoreQuesting.quests.data.QuestDataTask;
import org.techern.minecraft.ModernHardcoreQuesting.quests.data.QuestDataTaskReputationKill;
import org.techern.minecraft.ModernHardcoreQuesting.util.Translator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuestTaskReputationKill extends QuestTaskReputation {

    private int kills;

    public QuestTaskReputationKill(Quest parent, String description, String longDescription) {
        super(parent, description, longDescription, 20);

        register(EventHandler.Type.DEATH);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void draw(GuiQuestBook gui, EntityPlayer player, int mX, int mY) {
        super.draw(gui, player, mX, mY);
        int killCount = ((QuestDataTaskReputationKill) getData(player)).kills;
        if (Quest.isEditing) {
            gui.drawString(gui.getLinesFromText(Translator.translate(kills != 1, "modernhardcorequesting.repKil.kills", killCount, kills), 1F, 130), START_X, START_Y, 1F, 0x404040);
        } else {
            gui.drawString(gui.getLinesFromText(killCount == kills ? GuiColor.GREEN + Translator.translate(kills != 1, "modernhardcorequesting.repKil.killCount", kills) : Translator.translate("modernhardcorequesting.repKil.killCountOutOf", killCount, kills), 1F, 130), START_X, START_Y, 1F, 0x404040);
        }
    }

    @Override
    public float getCompletedRatio(String uuid) {
        return (float) ((QuestDataTaskReputationKill) getData(uuid)).kills / kills;
    }

    @Override
    public void mergeProgress(String uuid, QuestDataTask own, QuestDataTask other) {
        ((QuestDataTaskReputationKill) own).kills = Math.max(((QuestDataTaskReputationKill) own).kills, ((QuestDataTaskReputationKill) other).kills);

        if (((QuestDataTaskReputationKill) own).kills == kills) {
            completeTask(uuid);
        }
    }

    @Override
    public void autoComplete(String uuid) {
        kills = ((QuestDataTaskReputationKill) getData(uuid)).kills;
    }

    @Override
    protected EntityPlayer getPlayerForRender(EntityPlayer player) {
        return null;
    }

    @Override
    public Class<? extends QuestDataTask> getDataType() {
        return QuestDataTaskReputationKill.class;
    }

    @Override
    public void onUpdate(EntityPlayer player) {

    }

    @Override
    public void copyProgress(QuestDataTask own, QuestDataTask other) {
        super.copyProgress(own, other);

        ((QuestDataTaskReputationKill) own).kills = ((QuestDataTaskReputationKill) other).kills;
    }

    @Override
    public void onLivingDeath(LivingDeathEvent event) {
        EntityPlayer killer = QuestTaskMob.getKiller(event);
        if (killer != null && parent.isEnabled(killer) && parent.isAvailable(killer) && this.isVisible(killer) && !this.isCompleted(killer) && !killer.equals(event.getEntityLiving())) {
            if (event.getEntityLiving() instanceof EntityPlayer && isPlayerInRange((EntityPlayer) event.getEntityLiving())) {
                QuestDataTaskReputationKill killData = (QuestDataTaskReputationKill) getData(killer);
                if (killData.kills < kills) {
                    killData.kills += 1;

                    if (killData.kills == kills) {
                        completeTask(killer.getUniqueID());
                    }

                    parent.sendUpdatedDataToTeam(killer);
                }
            }
        }
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }
}
