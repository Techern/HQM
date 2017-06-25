package org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.edit;

import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiBase;
import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiQuestBook;
import org.techern.minecraft.ModernHardcoreQuesting.quests.Quest;
import org.techern.minecraft.ModernHardcoreQuesting.quests.TriggerType;
import org.techern.minecraft.ModernHardcoreQuesting.util.SaveHelper;
import net.minecraft.entity.player.EntityPlayer;

public class GuiEditMenuTrigger extends GuiEditMenuExtended {

    private TriggerType type;
    private int triggerTasks;
    private String id;

    public GuiEditMenuTrigger(GuiQuestBook gui, EntityPlayer player, Quest quest) {
        super(gui, player, true, 25, 20, 25, 135);

        this.id = quest.getId();
        this.type = quest.getTriggerType();
        this.triggerTasks = quest.getTriggerTasks();

        textBoxes.add(new TextBoxNumber(gui, 0, "modernhardcorequesting.menuTrigger.taskCount") {
            @Override
            protected int getValue() {
                return triggerTasks;
            }

            @Override
            protected void setValue(int number) {
                triggerTasks = number;
            }

            @Override
            protected boolean isVisible() {
                return type.isUseTaskCount();
            }
        });
    }

    @Override
    protected void onArrowClick(boolean left) {
        if (left) {
            type = TriggerType.values()[(type.ordinal() + TriggerType.values().length - 1) % TriggerType.values().length];
        } else {
            type = TriggerType.values()[(type.ordinal() + 1) % TriggerType.values().length];
        }
    }

    @Override
    protected String getArrowText() {
        return type.getName();
    }

    @Override
    protected String getArrowDescription() {
        return type.getDescription();
    }

    @Override
    public void save(GuiBase gui) {
        Quest quest = Quest.getQuest(id);
        if (quest != null) {
            quest.setTriggerType(type);
            quest.setTriggerTasks(Math.max(1, triggerTasks));
            SaveHelper.add(SaveHelper.EditType.VISIBILITY_CHANGED);
        }
    }

}
