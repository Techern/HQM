package org.techern.minecraft.ModernHardcoreQuesting.client.sounds;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.util.Set;

public class LoreResourcePack extends AbstractResourcePack {

    private static final Set domains = Sets.newHashSet("modernhardcorequesting");

    public LoreResourcePack(File folder) {
        super(folder);
    }

    @Override
    public InputStream getInputStream(ResourceLocation resource) throws IOException {
        return this.getInputStreamByName(resource.getResourcePath().replace("sounds/", ""));
    }

    @Override
    public boolean resourceExists(ResourceLocation resource) {
        return hasResourceName(resource.getResourcePath().replace("sounds/", ""));
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, "lore.ogg")));
    }

    @Override
    protected boolean hasResourceName(String name) {
        return name.contains("lore") && name.endsWith(".ogg") && new File(this.resourcePackFile, "lore.ogg").isFile();
    }

    @Override
    public Set getResourceDomains() {
        return domains;
    }
}