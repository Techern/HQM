package org.techern.minecraft.ModernHardcoreQuesting.quests.task;


import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.techern.minecraft.ModernHardcoreQuesting.client.ClientChange;
import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiBase;
import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiQuestBook;
import org.techern.minecraft.ModernHardcoreQuesting.client.sounds.Sounds;
import org.techern.minecraft.ModernHardcoreQuesting.event.EventHandler;
import org.techern.minecraft.ModernHardcoreQuesting.io.adapter.QuestTaskAdapter;
import org.techern.minecraft.ModernHardcoreQuesting.network.NetworkManager;
import org.techern.minecraft.ModernHardcoreQuesting.quests.Quest;
import org.techern.minecraft.ModernHardcoreQuesting.quests.QuestData;
import org.techern.minecraft.ModernHardcoreQuesting.quests.QuestingData;
import org.techern.minecraft.ModernHardcoreQuesting.quests.RepeatType;
import org.techern.minecraft.ModernHardcoreQuesting.quests.data.QuestDataTask;
import org.techern.minecraft.ModernHardcoreQuesting.team.RewardSetting;
import org.techern.minecraft.ModernHardcoreQuesting.team.TeamStats;
import org.techern.minecraft.ModernHardcoreQuesting.util.Translator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

public abstract class QuestTask {

    static final int START_X = 180;
    static final int START_Y = 95;
    public String description;
    protected Quest parent;
    private List<QuestTask> requirements;
    private String longDescription;
    private int id;
    private List<String> cachedDescription;

    public QuestTask(Quest parent, String description, String longDescription) {
        this.parent = parent;
        this.requirements = new ArrayList<>();
        this.description = description;
        this.longDescription = longDescription;
        updateId();
    }

    public static void completeQuest(Quest quest, String playerUuid) {
        if (!quest.isEnabled(playerUuid) || !quest.isAvailable(playerUuid)) return;
        for (QuestTask questTask : quest.getTasks()) {
            if (!questTask.getData(playerUuid).completed) {
                return;
            }
        }
        QuestData data = quest.getQuestData(playerUuid);

        data.completed = true;
        data.claimed = false;
        data.available = false;
        data.time = Quest.serverTicker.getHours();


        if (QuestingData.getQuestingData(playerUuid).getTeam().getRewardSetting() == RewardSetting.RANDOM) {
            int rewardId = (int) (Math.random() * data.reward.length);
            data.reward[rewardId] = true;
        } else {
            for (int i = 0; i < data.reward.length; i++) {
                data.reward[i] = true;
            }
        }
        quest.sendUpdatedDataToTeam(playerUuid);
        TeamStats.refreshTeam(QuestingData.getQuestingData(playerUuid).getTeam());

        for (Quest child : quest.getReversedRequirement()) {
            completeQuest(child, playerUuid);
            child.sendUpdatedDataToTeam(playerUuid);
        }

        if (quest.getRepeatInfo().getType() == RepeatType.INSTANT) {
            quest.reset(playerUuid);
        }

        EntityPlayer player = QuestingData.getPlayer(playerUuid);
        if (player instanceof EntityPlayerMP && !quest.hasReward(player)) {
            // when there is no reward and it just completes the quest play the music
            NetworkManager.sendToPlayer(ClientChange.SOUND.build(Sounds.COMPLETE), (EntityPlayerMP) player);
        }
    }

    public void updateId() {
        this.id = parent.nextTaskId++;
    }

    public boolean isCompleted(EntityPlayer player) {
        return getData(player).completed;
    }

    public boolean isCompleted(String uuid) {
        return getData(uuid).completed;
    }

    public boolean isVisible(EntityPlayer player) {
        Iterator itr = this.requirements.iterator();
        QuestTask requirement;
        do {
            if (!itr.hasNext()) return true;
            requirement = (QuestTask) itr.next();
        } while (requirement.isCompleted(player) && requirement.isVisible(player));
        return false;
    }

    public Class<? extends QuestDataTask> getDataType() {
        return QuestDataTask.class;
    }

    public void write(QuestDataTask task, JsonWriter out) throws IOException {
        task.write(out);
    }

    public void read(QuestDataTask task, JsonReader in) throws IOException {
        task.update(QuestTaskAdapter.QUEST_DATA_TASK_ADAPTER.read(in));
    }

    public QuestDataTask getData(EntityPlayer player) {
        return getData(QuestingData.getUserUUID(player));
    }

    public QuestDataTask getData(String uuid) {
        if (id < 0) {
            return newQuestData(); // possible fix for #247
        }
        QuestData questData = QuestingData.getQuestingData(uuid).getQuestData(parent.getId());
        if (id >= questData.tasks.length) {
            questData.tasks = Arrays.copyOf(questData.tasks, id + 1);
            questData.tasks[id] = newQuestData();
        }
        return questData.tasks[id] = validateData(questData.tasks[id]);
    }

    public QuestDataTask validateData(QuestDataTask data) {
        if (data == null || data.getClass() != getDataType()) {
            return newQuestData();
        }

        return data;
    }

    private QuestDataTask newQuestData() {
        try {
            Constructor<? extends QuestDataTask> constructor = getDataType().getConstructor(new Class[]{QuestTask.class});
            Object obj = constructor.newInstance(this);
            return (QuestDataTask) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getLangKeyDescription() {
        return description;
    }

    public String getDescription() {
        return Translator.translate(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLangKeyLongDescription() {
        return longDescription;
    }

    public String getLongDescription() {
        return Translator.translate(longDescription);
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
        cachedDescription = null;
    }

    @SideOnly(Side.CLIENT)
    public List<String> getCachedLongDescription(GuiBase gui) {
        if (cachedDescription == null) {
            cachedDescription = gui.getLinesFromText(Translator.translate(longDescription), 0.7F, 130);
        }

        return cachedDescription;
    }

    public void completeTask(UUID uuid) {
        completeTask(uuid.toString());
    }

    public void completeTask(String playerName) {
        getData(playerName).completed = true;
        completeQuest(parent, playerName);
    }

    @SideOnly(Side.CLIENT)
    public abstract void draw(GuiQuestBook gui, EntityPlayer player, int mX, int mY);

    @SideOnly(Side.CLIENT)
    public abstract void onClick(GuiQuestBook gui, EntityPlayer player, int mX, int mY, int b);

    public abstract void onUpdate(EntityPlayer player);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Quest getParent() {
        return parent;
    }

    public List<QuestTask> getRequirements() {
        return requirements;
    }

    public void addRequirement(QuestTask task) {
        requirements.add(task);
    }

    public void clearRequirements() {
        requirements.clear();
    }

    public abstract float getCompletedRatio(String uuid);

    public abstract void mergeProgress(String uuid, QuestDataTask own, QuestDataTask other);

    public abstract void autoComplete(String uuid);

    public void copyProgress(QuestDataTask own, QuestDataTask other) {
        own.completed = other.completed;
    }

    public void onDelete() {
        EventHandler.instance().remove(this);
    }

    public void register(EventHandler.Type... types) {
        EventHandler.instance().add(this, types);
    }

    //for these to be called one must register the task using the method above using the correct types
    public void onServerTick(TickEvent.ServerTickEvent event) {
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    }

    public void onLivingDeath(LivingDeathEvent event) {
    }

    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
    }

    public void onItemPickUp(EntityItemPickupEvent event) {
    }

    public void onOpenBook(EventHandler.BookOpeningEvent event) {
    }

    public void onReputationChange(EventHandler.ReputationEvent event) {
    }
}
