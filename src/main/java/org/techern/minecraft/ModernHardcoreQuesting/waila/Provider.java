package org.techern.minecraft.ModernHardcoreQuesting.waila;


import org.techern.minecraft.ModernHardcoreQuesting.blocks.BlockDelivery;
import org.techern.minecraft.ModernHardcoreQuesting.blocks.BlockPortal;
import org.techern.minecraft.ModernHardcoreQuesting.blocks.ModBlocks;
import org.techern.minecraft.ModernHardcoreQuesting.quests.task.QuestTask;
import org.techern.minecraft.ModernHardcoreQuesting.tileentity.PortalType;
import org.techern.minecraft.ModernHardcoreQuesting.tileentity.TileEntityBarrel;
import org.techern.minecraft.ModernHardcoreQuesting.tileentity.TileEntityPortal;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class Provider implements IWailaDataProvider {

    private static final String MOD_NAME = "HQM";
    private static final String IS_REMOTE_AVAILABLE = MOD_NAME + ".showQDS";

    public static void callbackRegister(IWailaRegistrar registrar) {
        Provider instance = new Provider();
        registrar.registerStackProvider(instance, BlockPortal.class);
        registrar.registerBodyProvider(instance, BlockDelivery.class);
        registrar.registerNBTProvider(instance, BlockDelivery.class);
        registrar.addConfigRemote(MOD_NAME, IS_REMOTE_AVAILABLE, "Show QDS data");
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() == ModBlocks.itemPortal) {
            TileEntity te = accessor.getTileEntity();
            if (te instanceof TileEntityPortal) {
                TileEntityPortal portal = (TileEntityPortal) te;
                if (portal.hasTexture(getPlayer())) {
                    if (portal.getType().isPreset()) {
                        return new ItemStack(ModBlocks.itemPortal, 1, portal.getType() == PortalType.TECH ? 1 : 2);
                    } else {
                        return portal.getStack();
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public List<String> getWailaHead(ItemStack stack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack stack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (config.getConfig(IS_REMOTE_AVAILABLE)) {
            if (!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(accessor.getBlock())) {
                TileEntity te = accessor.getTileEntity();
                if (te != null) {
                    if (te instanceof TileEntityBarrel) {
                        TileEntityBarrel qds = (TileEntityBarrel) te;

                        qds.readFromNBT(accessor.getNBTData());

                        QuestTask task = qds.getCurrentTask();
                        if (task != null && te.getBlockMetadata() == 1) {
                            currenttip.add(qds.getPlayer());
                            currenttip.add(task.getParent().getName());
                            currenttip.add(task.getDescription());
                            currenttip.add((int) (task.getCompletedRatio(qds.getPlayer()) * 100) + "% completed");
                        }
                    }
                }
            }
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack stack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return tag;
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().player;
    }
}
