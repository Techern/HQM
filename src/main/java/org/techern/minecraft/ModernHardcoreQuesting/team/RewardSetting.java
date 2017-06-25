package org.techern.minecraft.ModernHardcoreQuesting.team;

import org.techern.minecraft.ModernHardcoreQuesting.util.Translator;

public enum RewardSetting {
    ALL("modernhardcorequesting.team.allReward.title", "modernhardcorequesting.team.allReward.desc"),
    ANY("modernhardcorequesting.team.anyReward.title", "modernhardcorequesting.team.anyReward.desc"),
    RANDOM("modernhardcorequesting.team.randomReward.title", "modernhardcorequesting.team.randomReward.desc");

    public static boolean isAllModeEnabled;
    private String title;
    private String description;

    RewardSetting(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public static RewardSetting getDefault() {
        return isAllModeEnabled ? ALL : ANY;
    }

    public String getTitle() {
        return Translator.translate(title);
    }

    public String getDescription() {
        return Translator.translate(description);
    }
}
