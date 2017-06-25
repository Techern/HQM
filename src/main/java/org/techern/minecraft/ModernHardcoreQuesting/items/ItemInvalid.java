package org.techern.minecraft.ModernHardcoreQuesting.items;

import org.techern.minecraft.ModernHardcoreQuesting.HardcoreQuesting;
import org.techern.minecraft.ModernHardcoreQuesting.util.RegisterHelper;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Tim
 */
public class ItemInvalid extends Item {

    public ItemInvalid() {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(HardcoreQuesting.HQMTab);
        this.setRegistryName(ItemInfo.INVALID_UNLOCALIZED_NAME);
        this.setUnlocalizedName(ItemInfo.LOCALIZATION_START + ItemInfo.INVALID_UNLOCALIZED_NAME);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        RegisterHelper.registerItemRenderer(this);
    }
}
