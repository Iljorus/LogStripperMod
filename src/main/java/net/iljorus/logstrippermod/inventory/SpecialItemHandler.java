package net.iljorus.logstrippermod.inventory;

import net.iljorus.logstrippermod.block.entity.LogStripperBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class SpecialItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
    protected ItemStack stack;

    public SpecialItemHandler() {
        stack = ItemStack.EMPTY;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Slot", LogStripperBlockEntity.AXE_SLOT);
        stack.save(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        setStackInSlot(ItemStack.of(nbt));
    }

    public void setStackInSlot(@NotNull ItemStack stack) {
        setStackInSlot(0, stack);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    public @NotNull ItemStack getStack() {
        return getStackInSlot(0);
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return stack;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (!isItemValid(stack)) {
            return stack;
        }

        ItemStack existing = stack;
        int limit = Math.min(getSlotLimit(0), existing.getMaxStackSize());

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                stack = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        }
        ItemStack existing = stack;
        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                stack = ItemStack.EMPTY;
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                stack.setCount(existing.getCount() - toExtract);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return stack.getMaxStackSize();
    }

    public boolean isItemValid(@NotNull ItemStack stack) {
        return isItemValid(0, stack);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.getItem().canPerformAction(Items.OAK_LOG.getDefaultInstance(), ToolActions.AXE_STRIP);
    }
}
