package org.techern.minecraft.ModernHardcoreQuesting;

import org.techern.minecraft.ModernHardcoreQuesting.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HQMTab extends CreativeTabs {

    public HQMTab() {
        super("mhq");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        // the Icon of the creative Tab
        return new ItemStack(ModItems.book, 1, 0);
    }

    @Override
    public ItemStack getTabIconItem() {
        return getIconItemStack();
    }
}
