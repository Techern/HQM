package org.techern.minecraft.ModernHardcoreQuesting.quests.data;


import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.techern.minecraft.ModernHardcoreQuesting.io.adapter.QuestTaskAdapter;
import org.techern.minecraft.ModernHardcoreQuesting.quests.task.QuestTask;
import org.techern.minecraft.ModernHardcoreQuesting.quests.task.QuestTaskMob;

import java.io.IOException;

public class QuestDataTaskMob extends QuestDataTask {

    private static final String COUNT = "count";
    private static final String KILLED = "killed";
    public int[] killed;

    public QuestDataTaskMob(QuestTask task) {
        super(task);
        this.killed = new int[((QuestTaskMob) task).mobs.length];
    }

    protected QuestDataTaskMob() {
        super();
        this.killed = new int[0];
    }

    public static QuestDataTask construct(JsonReader in) {
        QuestDataTaskMob taskData = new QuestDataTaskMob();
        try {
            int count = 0;
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case COMPLETED:
                        taskData.completed = in.nextBoolean();
                        break;
                    case COUNT:
                        count = in.nextInt();
                        taskData.killed = new int[count];
                        break;
                    case KILLED:
                        in.beginArray();
                        for (int i = 0; i < count; i++)
                            taskData.killed[i] = in.nextInt();
                        in.endArray();
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException ignored) {
        }
        return taskData;
    }

    @Override
    public QuestTaskAdapter.QuestDataType getDataType() {
        return QuestTaskAdapter.QuestDataType.MOB;
    }

    @Override
    public void write(JsonWriter out) throws IOException {
        super.write(out);
        out.name(COUNT).value(killed.length);
        out.name(KILLED).beginArray();
        for (int i : killed)
            out.value(i);
        out.endArray();
    }

    @Override
    public void update(QuestDataTask taskData) {
        super.update(taskData);
        this.killed = ((QuestDataTaskMob) taskData).killed;
    }
}
