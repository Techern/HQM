package org.techern.minecraft.ModernHardcoreQuesting.proxies;

import org.techern.minecraft.ModernHardcoreQuesting.blocks.ModBlocks;
import org.techern.minecraft.ModernHardcoreQuesting.client.sounds.Sounds;
import org.techern.minecraft.ModernHardcoreQuesting.items.ModItems;
import org.techern.minecraft.ModernHardcoreQuesting.quests.Quest;
import org.techern.minecraft.ModernHardcoreQuesting.quests.QuestTicker;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;


public class ClientProxy extends CommonProxy {

    @Override
    public void initSounds(String path) {
        //init all the sounds
        Sounds.initSounds();
    }

    @Override
    public void initRenderers() {
        //init the rendering stuff

        //MinecraftForge.EVENT_BUS.register(new GUIOverlay(Minecraft.getMinecraft()));
        //MinecraftForge.EVENT_BUS.register(new BlockHighlightRemover());
        ModItems.initRender();
        ModBlocks.initRender();
    }

    @Override
    public void init() {
        Quest.serverTicker = new QuestTicker(false);
        Quest.clientTicker = new QuestTicker(true);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public EntityPlayer getPlayer(MessageContext ctx) {
        return ctx.side == Side.CLIENT ? Minecraft.getMinecraft().player : super.getPlayer(ctx);
    }
}
