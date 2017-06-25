package org.techern.minecraft.ModernHardcoreQuesting.quests.reward;

import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiColor;
import org.techern.minecraft.ModernHardcoreQuesting.reputation.Reputation;

public class ReputationReward extends QuestReward<Reputation> {

    private int value;

    public ReputationReward(Reputation reputation, int value) {
        super(reputation);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        if (reward != null) {
            String result = reward.getName() + ": ";

            if (value != 0) {
                result += value > 0 ? GuiColor.GREEN : GuiColor.RED;
            }
            if (value > 0) {
                result += "+";
            }
            result += value;
            return result;
        }
        return "";
    }
}
