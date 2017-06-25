package org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.edit;

import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiBase;
import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiQuestBook;
import org.techern.minecraft.ModernHardcoreQuesting.reputation.ReputationMarker;
import org.techern.minecraft.ModernHardcoreQuesting.util.SaveHelper;
import net.minecraft.entity.player.EntityPlayer;

public class GuiEditMenuReputationValue extends GuiEditMenuExtended {

    private ReputationMarker marker;
    private int value;

    public GuiEditMenuReputationValue(GuiBase gui, EntityPlayer player, ReputationMarker marker) {
        super(gui, player, true, -1, -1, 25, 30);

        this.marker = marker;
        this.value = marker.getValue();

        textBoxes.add(new TextBoxNumber(gui, 0, "hqm.repValue.tierValue") {
            @Override
            protected boolean isNegativeAllowed() {
                return true;
            }

            @Override
            protected int getValue() {
                return value;
            }

            @Override
            protected void setValue(int number) {
                value = number;
            }
        });
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

    @Override
    public void save(GuiBase gui) {
        marker.setValue(value);
        GuiQuestBook.selectedReputation.sort();
        SaveHelper.add(SaveHelper.EditType.REPUTATION_MARKER_CHANGE);
    }
}
