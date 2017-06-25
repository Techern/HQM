package org.techern.minecraft.ModernHardcoreQuesting.io.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.techern.minecraft.ModernHardcoreQuesting.death.DeathStats;
import org.techern.minecraft.ModernHardcoreQuesting.death.DeathType;

import java.io.IOException;

/**
 * A utility class that contains a {@link TypeAdapter<DeathStats>}
 */
public class DeathAdapter {

    /**
     * A {@link TypeAdapter} for {@link DeathStats}
     */
    public static final TypeAdapter<DeathStats> DEATH_STATS_ADAPTER = new TypeAdapter<DeathStats>() {

        /**
         * Writes the {@link DeathStats} {@code statistics} to the {@link JsonWriter} {@code writer}
         *
         * @param writer The {@link JsonWriter} being written to
         * @param statistics The {@link DeathStats}
         * @throws IOException Something done fucked up
         */
        @Override
        public void write(JsonWriter writer, DeathStats statistics) throws IOException {
            writer.beginObject();
            writer.name(statistics.getUuid());
            writer.beginArray();
            for (DeathType type : DeathType.values())
                writer.value(statistics.getDeaths(type.ordinal()));
            writer.endArray();
            writer.endObject();
        }

        /**
         * Reads {@link DeathStats} from the {@link JsonWriter} {@code reader}
         *
         * @param reader The {@link JsonReader} being read from
         *
         * @return The {@link DeathStats}
         *
         * @throws IOException Something done fucked up again
         */
        @Override
        public DeathStats read(JsonReader reader) throws IOException {
            reader.beginObject();
            DeathStats stats = null;
            if (reader.hasNext()) {
                String uuid = reader.nextName();
                stats = new DeathStats(uuid);
                reader.beginArray();
                int deathTypeIndex = 0;
                while (reader.hasNext())
                    stats.increaseDeath(deathTypeIndex++, reader.nextInt(), false);
                reader.endArray();
            }
            reader.endObject();
            return stats; // Should never be null
        }
    };
}
