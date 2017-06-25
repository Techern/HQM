package org.techern.minecraft.ModernHardcoreQuesting.client.interfaces;

import org.techern.minecraft.ModernHardcoreQuesting.ModInformation;
import org.techern.minecraft.ModernHardcoreQuesting.config.ModConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

public class HQMConfigGui extends GuiConfig {

    public HQMConfigGui(GuiScreen parentScreen) {
        super(parentScreen,
                ModConfig.getConfigElements(),
                ModInformation.ID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(ModConfig.config.toString()));
    }
}
