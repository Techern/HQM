package org.techern.minecraft.ModernHardcoreQuesting.config;

import com.google.common.collect.Lists;
import org.techern.minecraft.ModernHardcoreQuesting.HardcoreQuesting;
import org.techern.minecraft.ModernHardcoreQuesting.client.KeyboardHandler;
import org.techern.minecraft.ModernHardcoreQuesting.quests.Quest;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigHandler {

    private static final String EDITOR_KEY = "UseEditor";
    private static final boolean EDITOR_DEFAULT = false;
    private static final String EDITOR_COMMENT = "Only use this as a map maker who wants to create quests. Leaving this off allows you the play the existing quests.";
    private static final String SAVE_DEFAULT_KEY = "SaveDefault";
    private static final boolean SAVE_DEFAULT_DEFAULT = true;
    private static final String SAVE_DEFAULT_COMMENT = "This will save quests in an general map used upon world creation";
    private static final String KEYMAP_KEY = "KeyMap";
    private static final String KEYMAP_COMMENT = "Hotkeys used in the book, one entry per line(Format: [key]:[mode]";
    public static Configuration syncConfig;
    private static String[] KEYMAP_DEFAULT = null;

    private static List<String> readMeText = Lists.newArrayList("How to copy quests to a server:",
            "In MHQ for 1.11.2 the file copying to a server works again. Copy the hqm/quests to the server side config hqm/quests.",
            "There is a config option to automatically sync the server quests to the client.");

    public static void initModConfig(String configPath) {
        ModConfig.init(new File(configPath + "MHQ_config.cfg"));
        MinecraftForge.EVENT_BUS.register(new ModConfig());
    }

    public static void initEditConfig(String configPath) {
        if(new File(configPath, "default").exists()){
            FMLLog.warning("[MHQ] Detected old HQM quest files! These aren't fully compatible with the newer versions. To disable this message, delete the 'default' folder.");
        }
        try {
            FileUtils.writeLines(new File(configPath, "ReadMe.txt"), readMeText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (syncConfig == null) {
            syncConfig = new Configuration(new File(configPath + "editmode.cfg"));
            loadSyncConfig();
        }
    }

    public static void loadSyncConfig() {
        Quest.isEditing = syncConfig.get(Configuration.CATEGORY_GENERAL, EDITOR_KEY, EDITOR_DEFAULT, EDITOR_COMMENT).getBoolean(EDITOR_DEFAULT);
        Quest.saveDefault = syncConfig.get(Configuration.CATEGORY_GENERAL, SAVE_DEFAULT_KEY, SAVE_DEFAULT_DEFAULT, SAVE_DEFAULT_COMMENT).getBoolean(SAVE_DEFAULT_DEFAULT);
        if (HardcoreQuesting.proxy.isClient()) {
            if (KEYMAP_DEFAULT == null) {
                KEYMAP_DEFAULT = KeyboardHandler.getDefault();
            }
            KeyboardHandler.fromConfig(syncConfig.get(Configuration.CATEGORY_GENERAL, KEYMAP_KEY, KEYMAP_DEFAULT, KEYMAP_COMMENT).getStringList());
        }
        if (syncConfig.hasChanged()) {
            syncConfig.save();
        }
    }
}
