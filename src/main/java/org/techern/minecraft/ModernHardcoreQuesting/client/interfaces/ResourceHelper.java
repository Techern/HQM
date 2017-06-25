package org.techern.minecraft.ModernHardcoreQuesting.client.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class ResourceHelper {

    private ResourceHelper() {
    }

    public static ResourceLocation getResource(String name) {
        return new ResourceLocation("modernhardcorequesting", "textures/gui/" + name + ".png");
    }

    public static void bindResource(ResourceLocation resource) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
    }

}
