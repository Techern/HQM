package org.techern.minecraft.ModernHardcoreQuesting.quests.data;


import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.techern.minecraft.ModernHardcoreQuesting.io.adapter.QuestTaskAdapter;
import org.techern.minecraft.ModernHardcoreQuesting.quests.task.QuestTask;
import org.techern.minecraft.ModernHardcoreQuesting.quests.task.QuestTaskItems;

import java.io.IOException;

public class QuestDataTaskItems extends QuestDataTask {

    private static final String COUNT = "count";
    private static final String PROGRESS = "progress";
    public int[] progress;

    public QuestDataTaskItems(QuestTask task) {
        super(task);
        this.progress = new int[((QuestTaskItems) task).getItems().length];
    }

    protected QuestDataTaskItems() {
        super();
        this.progress = new int[0];
    }

    public static QuestDataTask construct(JsonReader in) {
        QuestDataTaskItems taskData = new QuestDataTaskItems();
        try {
            int count = 0;
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case QuestDataTask.COMPLETED:
                        taskData.completed = in.nextBoolean();
                        break;
                    case COUNT:
                        count = in.nextInt();
                        taskData.progress = new int[count];
                        break;
                    case PROGRESS:
                        in.beginArray();
                        for (int i = 0; i < count; i++)
                            taskData.progress[i] = in.nextInt();
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
        return QuestTaskAdapter.QuestDataType.ITEMS;
    }

    @Override
    public void write(JsonWriter out) throws IOException {
        super.write(out);
        out.name(COUNT).value(progress.length);
        out.name(PROGRESS).beginArray();
        for (int i : progress)
            out.value(i);
        out.endArray();
    }

    @Override
    public void update(QuestDataTask taskData) {
        super.update(taskData);
        this.progress = ((QuestDataTaskItems) taskData).progress;
    }
}
