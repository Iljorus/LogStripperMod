package net.iljorus.logstrippermod.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/*
 * ItemStackHandler wrapper that allows for normal item manipulation except insertion of an axe
 * */

public class InputItemStackWrapper extends ItemStackHandler {
    public ItemStackHandler internalHandler;

    public InputItemStackWrapper(ItemStackHandler internalHandler) {
        super();
        this.internalHandler = internalHandler;
    }

    @Override
    public void setSize(int size) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (!stack.getItem().canPerformAction(Items.OAK_LOG.getDefaultInstance(), ToolActions.AXE_STRIP)) {
            internalHandler.setStackInSlot(slot, stack);
        }
    }

    @Override
    public int getSlots() {
        return internalHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return internalHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!stack.getItem().canPerformAction(Items.OAK_LOG.getDefaultInstance(), ToolActions.AXE_STRIP)) {
            return internalHandler.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return internalHandler.extractItem(slot, amount, simulate);
    }
}
