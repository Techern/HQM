package org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.edit;

import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiBase;
import org.techern.minecraft.ModernHardcoreQuesting.quests.Quest;
import org.techern.minecraft.ModernHardcoreQuesting.util.SaveHelper;
import org.techern.minecraft.ModernHardcoreQuesting.util.Translator;
import net.minecraft.entity.player.EntityPlayer;

public class GuiEditMenuParentCount extends GuiEditMenuExtended {

    private boolean useModifiedParentRequirement;
    private int parentRequirementCount;
    private String id;

    public GuiEditMenuParentCount(GuiBase gui, EntityPlayer player, Quest quest) {
        super(gui, player, true, 25, 20, 25, 105);

        this.id = quest.getId();
        this.useModifiedParentRequirement = quest.getUseModifiedParentRequirement();
        if (useModifiedParentRequirement) {
            this.parentRequirementCount = quest.getParentRequirementCount();
        } else {
            this.parentRequirementCount = quest.getRequirements().size();
        }


        textBoxes.add(new TextBoxNumber(gui, 0, "modernhardcorequesting.parentCount.count") {
            @Override
            protected int getValue() {
                return parentRequirementCount;
            }

            @Override
            protected void setValue(int number) {
                parentRequirementCount = number;
            }

            @Override
            protected boolean isVisible() {
                return useModifiedParentRequirement;
            }
        });
    }

    @Override
    protected void onArrowClick(boolean left) {
        useModifiedParentRequirement = !useModifiedParentRequirement;
    }

    @Override
    protected String getArrowText() {
        return Translator.translate("modernhardcorequesting.parentCount.req" + (useModifiedParentRequirement ? "Count" : "All") + ".title");
    }

    @Override
    protected String getArrowDescription() {
        return Translator.translate("modernhardcorequesting.parentCount.req" + (useModifiedParentRequirement ? "Count" : "All") + ".desc");
    }

    @Override
    public void save(GuiBase gui) {
        Quest quest = Quest.getQuest(id);
        if (quest != null) {
            quest.setUseModifiedParentRequirement(useModifiedParentRequirement);
            quest.setParentRequirementCount(parentRequirementCount);
            SaveHelper.add(SaveHelper.EditType.PARENT_REQUIREMENT_CHANGED);
        }
    }
}
