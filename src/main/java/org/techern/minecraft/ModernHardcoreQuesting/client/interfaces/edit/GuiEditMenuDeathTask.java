package org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.edit;

import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiBase;
import org.techern.minecraft.ModernHardcoreQuesting.quests.task.QuestTaskDeath;
import org.techern.minecraft.ModernHardcoreQuesting.util.SaveHelper;
import net.minecraft.entity.player.EntityPlayer;


public class GuiEditMenuDeathTask extends GuiEditMenuExtended {

    private int deaths;
    private QuestTaskDeath task;

    public GuiEditMenuDeathTask(GuiBase gui, EntityPlayer player, QuestTaskDeath task) {
        super(gui, player, true, -1, -1, 25, 30);

        deaths = task.getDeaths();
        this.task = task;

        textBoxes.add(new TextBoxNumber(gui, 0, "modernhardcorequesting.deathTask.reqDeathCount") {
            @Override
            protected int getValue() {
                return deaths;
            }

            @Override
            protected void setValue(int number) {
                deaths = number;
            }
        });
    }

    @Override
    public void save(GuiBase gui) {
        task.setDeaths(deaths);
        SaveHelper.add(SaveHelper.EditType.DEATH_CHANGE);
    }

    @Override
    protected void onArrowClick(boolean left) {

    }

    @Override
    protected String getArrowText() {
        return null;
    }

    @Override
    protected String getArrowDescription() {
        return null;
    }
}
