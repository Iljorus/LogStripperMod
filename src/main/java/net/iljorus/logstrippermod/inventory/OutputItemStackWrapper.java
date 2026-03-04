package net.iljorus.logstrippermod.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/*
 * ItemStackHandler wrapper that allows for normal item manipulation except insertion from external sources
 * */

public class OutputItemStackWrapper extends ItemStackHandler {
    private final ItemStackHandler internalHandler;

    public OutputItemStackWrapper(ItemStackHandler internalHandler) {
        super();
        this.internalHandler = internalHandler;
    }

    @Override
    public void setSize(int size) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        internalHandler.setStackInSlot(slot, stack);
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
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return internalHandler.extractItem(slot, amount, simulate);
    }
}
