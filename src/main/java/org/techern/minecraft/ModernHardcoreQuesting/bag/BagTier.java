package org.techern.minecraft.ModernHardcoreQuesting.bag;


import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiColor;
import org.techern.minecraft.ModernHardcoreQuesting.util.Translator;

public enum BagTier {
    BASIC(GuiColor.GRAY),
    GOOD(GuiColor.GREEN),
    GREATER(GuiColor.BLUE),
    EPIC(GuiColor.ORANGE),
    LEGENDARY(GuiColor.PURPLE);


    private GuiColor color;

    BagTier(GuiColor color) {
        this.color = color;
    }

    public GuiColor getColor() {
        return color;
    }

    public String getName() {
        return Translator.translate("modernhardcorequesting.bag." + this.name().toLowerCase());
    }

    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
