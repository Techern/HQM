package org.techern.minecraft.ModernHardcoreQuesting.blocks;


import org.techern.minecraft.ModernHardcoreQuesting.ModInformation;

public abstract class BlockInfo {

    public static final String LOCALIZATION_START = "modernhardcorequesting:";
    public static final String ITEMBARREL_UNLOCALIZED_NAME = "item_barrel";
    public static final String ITEMBARREL_TE_KEY = "ItemBarrel";
    public static final String QUEST_TRACKER_UNLOCALIZED_NAME = "quest_tracker";
    public static final String QUEST_TRACKER_TE_KEY = "QuestTracker";
    public static final String QUEST_PORTAL_UNLOCALIZED_NAME = "quest_portal";
    public static final String QUEST_PORTAL_TE_KEY = "QuestPortal";
    public static final String TILEENTITY_PREFIX = ModInformation.ID + "_";

    private BlockInfo() {
    }
}
