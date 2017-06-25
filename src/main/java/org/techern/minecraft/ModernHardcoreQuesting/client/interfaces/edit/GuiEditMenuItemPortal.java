package org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.edit;

import org.techern.minecraft.ModernHardcoreQuesting.client.interfaces.GuiBase;
import org.techern.minecraft.ModernHardcoreQuesting.quests.ItemPrecision;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiEditMenuItemPortal extends GuiEditMenuItem {

    private GuiEditMenuPortal parent;

    public GuiEditMenuItemPortal(GuiBase gui, GuiEditMenuPortal parent, EntityPlayer player, ItemStack stack) {
        super(gui, player, !stack.isEmpty() ? stack.copy() : null, 0, Type.PORTAL, 1, ItemPrecision.PRECISE);

        this.parent = parent;
    }

    @Override
    public void save(GuiBase gui) {
        if (selected instanceof ElementItem && selected.getFluidStack() != null) {
            parent.setItem((ItemStack) selected.getFluidStack());
        }
    }

    @Override
    public void close(GuiBase gui) {
        gui.setEditMenu(parent);
    }
}
