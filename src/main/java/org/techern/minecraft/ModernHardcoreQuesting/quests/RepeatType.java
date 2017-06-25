package org.techern.minecraft.ModernHardcoreQuesting.quests;

import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiColor;
import org.techern.minecraft.ModernHardcoreQuesting.util.Translator;
import net.minecraft.entity.player.EntityPlayer;

public enum RepeatType {
    NONE("none", false) {
        @Override
        public String getMessage(Quest quest, EntityPlayer player, int days, int hours) {
            return null;
        }
    },
    INSTANT("instant", false) {
        @Override
        public String getMessage(Quest quest, EntityPlayer player, int days, int hours) {
            return super.getMessage(quest, player, days, hours) + GuiColor.GRAY + Translator.translate("modernhardcorequesting.repeat.instant.message");
        }

        @Override
        public String getShortMessage(int days, int hours) {
            return GuiColor.YELLOW + Translator.translate("modernhardcorequesting.repeat.instant.message");
        }
    },
    INTERVAL("interval", true) {
        @Override
        public String getMessage(Quest quest, EntityPlayer player, int days, int hours) {
            return super.getMessage(quest, player, days, hours) + GuiColor.GRAY + Translator.translate("modernhardcorequesting.repeat.interval.message") + "\n" + formatTime(days, hours) + "\n" + formatResetTime(quest, player, days, hours);
        }

        @Override
        public String getShortMessage(int days, int hours) {
            return GuiColor.YELLOW + Translator.translate("modernhardcorequesting.repeat.interval.message") + " (" + days + ":" + hours + ")";
        }
    },
    TIME("time", true) {
        @Override
        public String getMessage(Quest quest, EntityPlayer player, int days, int hours) {
            return super.getMessage(quest, player, days, hours) + GuiColor.GRAY + Translator.translate("modernhardcorequesting.repeat.time.message") + "\n" + formatTime(days, hours) + formatRemainingTime(quest, player, days, hours);
        }

        @Override
        public String getShortMessage(int days, int hours) {
            return GuiColor.YELLOW + Translator.translate("modernhardcorequesting.repeat.time.message") + " (" + days + ":" + hours + ")";
        }
    };

    private String id;
    private boolean useTime;

    RepeatType(String id, boolean useTime) {
        this.id = id;
        this.useTime = useTime;
    }

    private static String formatRemainingTime(Quest quest, EntityPlayer player, int days, int hours) {
        if (!quest.getQuestData(player).available) {
            int total = days * 24 + hours;
            int time = quest.getQuestData(player).time;
            int current = Quest.clientTicker.getHours();

            total = time + total - current;

            return "\n" + formatResetTime(quest, player, total / 24, total % 24);
        } else {
            return "";
        }
    }

    private static String formatResetTime(Quest quest, EntityPlayer player, int days, int hours) {
        if (days == 0 && hours == 0) {
            return GuiColor.RED + Translator.translate("modernhardcorequesting.repeat.invalid");
        }

        int total = days * 24 + hours;
        int resetHoursTotal = total - Quest.clientTicker.getHours() % total;

        int resetDays = resetHoursTotal / 24;
        int resetHours = resetHoursTotal % 24;

        if (!quest.isAvailable(player)) {
            return GuiColor.YELLOW + Translator.translate("modernhardcorequesting.repeat.resetIn", formatTime(resetDays, resetHours));
        } else {
            return GuiColor.GRAY + Translator.translate("modernhardcorequesting.repeat.nextReset", formatTime(resetDays, resetHours));
        }
    }

    private static String formatTime(int days, int hours) {
        String str = GuiColor.GRAY.toString();
        if (days > 0) {
            str += GuiColor.LIGHT_GRAY;
        }
        str += days;
        str += " ";
        str += Translator.translate("modernhardcorequesting.repeat." + (days == 1 ? "day" : "days"));

        str += GuiColor.GRAY;

        str += " " + Translator.translate("modernhardcorequesting.repeat.and") + " ";

        if (hours > 0) {
            str += GuiColor.LIGHT_GRAY;
        }

        str += hours;
        str += " ";
        str += Translator.translate("modernhardcorequesting.repeat." + (hours == 1 ? "hour" : "hours"));

        return str;
    }

    public String getName() {
        return Translator.translate("modernhardcorequesting.repeat." + id + ".title");
    }

    public String getDescription() {
        return Translator.translate("modernhardcorequesting.repeat." + id + ".desc");
    }

    public boolean isUseTime() {
        return useTime;
    }

    public String getMessage(Quest quest, EntityPlayer player, int days, int hours) {
        return GuiColor.YELLOW + Translator.translate("modernhardcorequesting.repeat.repeatable") + "\n";
    }

    public String getShortMessage(int days, int hours) {
        return null;
    }
}
