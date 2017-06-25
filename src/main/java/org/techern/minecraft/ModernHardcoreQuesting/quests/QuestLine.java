package org.techern.minecraft.ModernHardcoreQuesting.quests;

import org.techern.minecraft.ModernHardcoreQuesting.HardcoreQuesting;
import org.techern.minecraft.ModernHardcoreQuesting.bag.Group;
import org.techern.minecraft.ModernHardcoreQuesting.bag.GroupTier;
import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiQuestBook;
import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.edit.GuiEditMenuItem;
import org.techern.minecraft.ModernHardcoreQuesting.client.sounds.SoundHandler;
import org.techern.minecraft.ModernHardcoreQuesting.death.DeathStats;
import org.techern.minecraft.ModernHardcoreQuesting.io.SaveHandler;
import org.techern.minecraft.ModernHardcoreQuesting.network.NetworkManager;
import org.techern.minecraft.ModernHardcoreQuesting.network.message.DeathStatsMessage;
import org.techern.minecraft.ModernHardcoreQuesting.network.message.PlayerDataSyncMessage;
import org.techern.minecraft.ModernHardcoreQuesting.network.message.QuestLineSyncMessage;
import org.techern.minecraft.ModernHardcoreQuesting.reputation.Reputation;
import org.techern.minecraft.ModernHardcoreQuesting.team.Team;
import org.techern.minecraft.ModernHardcoreQuesting.util.SaveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuestLine {

    public static boolean doServerSync;
    private static QuestLine config = new QuestLine();
    private static QuestLine server;
    private static QuestLine world;
    private static boolean hasLoadedMainSound;
    public final List<GroupTier> tiers = new ArrayList<>();
    public final Map<String, Group> groups = new ConcurrentHashMap<>();
    public List<QuestSet> questSets;
    public Map<String, Quest> quests;
    public String mainDescription = "No description";
    public List<String> cachedMainDescription;
    public String mainPath;
    @SideOnly(Side.CLIENT)
    public ResourceLocation front;

    public QuestLine() {
        GroupTier.initBaseTiers(this);
    }

    public static QuestLine getActiveQuestLine() {
        return server != null ? server : world != null ? world : config;
    }

    public static void receiveServerSync(boolean local, boolean remote) {
        if (!hasLoadedMainSound) {
            SoundHandler.loadLoreReading(config.mainPath);
            hasLoadedMainSound = true;
        }
        GuiQuestBook.resetBookPosition();
        if (!local) {
            reset();
            server = new QuestLine();
            server.mainPath = config.mainPath;
            server.quests = new ConcurrentHashMap<>();
            server.questSets = new ArrayList<>();
        }
        loadAll(true, remote);
        SoundHandler.loadLoreReading(getActiveQuestLine().mainPath);
    }

    public static void reset() {
        server = null;
        world = null;
    }

    public static void sendServerSync(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            boolean side = HardcoreQuesting.loadingSide.isServer();
            if (FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) // Integrated server
                NetworkManager.sendToPlayer(new PlayerDataSyncMessage(true, false, player), playerMP);
            else {
                if (QuestLine.doServerSync) // Send actual data to player on server sync
                    NetworkManager.sendToPlayer(new QuestLineSyncMessage(), playerMP);
                NetworkManager.sendToPlayer(new PlayerDataSyncMessage(false, side, player), playerMP);
            }
            NetworkManager.sendToPlayer(new DeathStatsMessage(side), playerMP);
        }
    }

    public static void loadWorldData(File worldPath, boolean isClient) {
        File pathFile = new File(worldPath, "hqm");
        String path = pathFile.getAbsolutePath() + File.separator;
        if (!pathFile.exists()) pathFile.mkdirs();
        world = new QuestLine();
        init(path, isClient);
    }

    public static void saveDescription() {
        try {
            SaveHandler.saveDescription(SaveHandler.getLocalFile("description.txt"), QuestLine.getActiveQuestLine().mainDescription);
        } catch (IOException e) {
            FMLLog.log("HQM", Level.INFO, "Failed to load questing state");
        }
    }

    public static void saveDescriptionDefault() {
        try {
            SaveHandler.saveDescription(SaveHandler.getDefaultFile("description.txt"), QuestLine.getActiveQuestLine().mainDescription);
        } catch (IOException e) {
            FMLLog.log("HQM", Level.INFO, "Failed to load questing state");
        }
    }

    public static void loadDescription(boolean remote) {
        try {
            QuestLine.getActiveQuestLine().mainDescription = SaveHandler.loadDescription(SaveHandler.getFile("description.txt", remote));
            QuestLine.getActiveQuestLine().cachedMainDescription = null;
        } catch (IOException e) {
            FMLLog.log("HQM", Level.INFO, "Failed to load questing state");
        }
    }

    public static void saveAll() {
        QuestingData.saveState();
        QuestLine.saveDescription();
        DeathStats.saveAll();
        Reputation.saveAll();
        GroupTier.saveAll();
        QuestSet.saveAll();
        Team.saveAll();
        QuestingData.saveQuestingData();
        if (Quest.saveDefault && Quest.isEditing) { // Save the needed defaults during edit mode
            QuestLine.saveDescriptionDefault();
            Reputation.saveAllDefault();
            GroupTier.saveAllDefault();
            QuestSet.saveAllDefault();
        }
        SaveHelper.onSave();
    }

    public static void loadAll(boolean isClient, boolean remote) {
        QuestingData.loadState(remote);
        QuestLine.loadDescription(remote);
        DeathStats.loadAll(isClient, remote);
        Reputation.loadAll(remote);
        GroupTier.loadAll(remote);
        Team.loadAll(isClient, remote);
        QuestSet.loadAll(remote);
        QuestingData.loadQuestingData(remote);
        SaveHelper.onLoad();
        if (isClient)
            GuiEditMenuItem.Search.initItems();
    }

    public static void init(String path) {
        QuestLine.getActiveQuestLine().mainPath = path;
        QuestLine.getActiveQuestLine().quests = new ConcurrentHashMap<>();
        QuestLine.getActiveQuestLine().questSets = new ArrayList<>();
    }

    public static void init(String path, boolean isClient) {
        init(path);
        loadAll(isClient, false);
    }

    public static void copyDefaults(File worldPath) {
        File path = new File(worldPath, "hqm");
        if (!path.exists()) path.mkdirs();
        SaveHandler.copyFolder(SaveHandler.getDefaultFolder(), path);
    }
}
