package org.techern.minecraft.ModernHardcoreQuesting;

import org.techern.minecraft.ModernHardcoreQuesting.blocks.ModBlocks;
import org.techern.minecraft.ModernHardcoreQuesting.commands.CommandHandler;
import org.techern.minecraft.ModernHardcoreQuesting.config.ConfigHandler;
import org.techern.minecraft.ModernHardcoreQuesting.event.PlayerDeathEventListener;
import org.techern.minecraft.ModernHardcoreQuesting.event.PlayerTracker;
import org.techern.minecraft.ModernHardcoreQuesting.event.WorldEventListener;
import org.techern.minecraft.ModernHardcoreQuesting.items.ModItems;
import org.techern.minecraft.ModernHardcoreQuesting.network.NetworkManager;
import org.techern.minecraft.ModernHardcoreQuesting.proxies.CommonProxy;
import org.techern.minecraft.ModernHardcoreQuesting.quests.QuestLine;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION, guiFactory = "org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.HQMModGuiFactory")
public class HardcoreQuesting {

    @Instance(ModInformation.ID)
    public static HardcoreQuesting instance;

    @SidedProxy(clientSide = "org.techern.minecraft.ModernHardcoreQuesting.proxies.ClientProxy", serverSide = "org.techern.minecraft.ModernHardcoreQuesting.proxies.CommonProxy")
    public static CommonProxy proxy;
    public static CreativeTabs HQMTab = new HQMTab();

    public static String path;

    public static File configDir;

    public static Side loadingSide;

    private static EntityPlayer commandUser;

    public static EntityPlayer getPlayer() {
        return commandUser;
    }

    public static void setPlayer(EntityPlayer player) {
        commandUser = player;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        loadingSide = event.getSide();
        new org.techern.minecraft.ModernHardcoreQuesting.event.EventHandler();

        path = event.getModConfigurationDirectory().getAbsolutePath() + File.separator + ModInformation.CONFIG_LOC_NAME.toLowerCase() + File.separator;
        configDir = new File(path);
        ConfigHandler.initModConfig(path);
        ConfigHandler.initEditConfig(path);
        QuestLine.init(path);

        proxy.init();
        proxy.initSounds(path);

        ModBlocks.init();
        ModBlocks.registerTileEntities();

        ModItems.init();
        proxy.initRenderers();
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(instance);

        new WorldEventListener();
        new PlayerDeathEventListener();
        new PlayerTracker();

        NetworkManager.init();

        ModItems.registerRecipes();
        ModBlocks.registerRecipes();

        FMLInterModComms.sendMessage("Waila", "register", "org.techern.minecraft.ModernHardcoreQuesting.waila.Provider.callbackRegister");
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(CommandHandler.instance);
    }

    @EventHandler
    public void missingMappings(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {
            ResourceLocation loc = mapping.resourceLocation;
            if (loc.getResourceDomain().equals("HardcoreQuesting")) {
                if (mapping.type.equals(GameRegistry.Type.BLOCK)) {
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("modernhardcorequesting", loc.getResourcePath()));
                    if (block != null) {
                        mapping.remap(block);
                    }
                } else {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("modernhardcorequesting", loc.getResourcePath()));
                    if (item != null) {
                        mapping.remap(item);
                    }
                }
            }
            if (mapping.resourceLocation.getResourcePath().toLowerCase().equals("hqminvaliditem")) {
                mapping.remap(ModItems.invalidItem);
            }
        }
    }
}
