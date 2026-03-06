package net.iljorus.logstrippermod.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class InventoryHelper {
    public static boolean pullFromAdjacent(BlockEntity origin, IItemHandler originHandler, int amount, Direction side) {
        Level level = origin.getLevel();
        BlockPos adjacentPos = origin.getBlockPos().relative(side);
        Direction adjacentFace = side.getOpposite();

        assert level != null;
        BlockEntity adjacent = level.getBlockEntity(adjacentPos);
        if (adjacent == null) {
            return false;
        }

        LazyOptional<IItemHandler> capability = adjacent.getCapability(ForgeCapabilities.ITEM_HANDLER, adjacentFace);
        IItemHandler adjacentHandler = capability.orElse(null);
        if (adjacentHandler == null) {
            return false;
        }
        int slotCount = adjacentHandler.getSlots();
        for (int i = 0; i < slotCount; i++) {
            ItemStack grabbed = adjacentHandler.extractItem(i, amount, true);
            if (grabbed.isEmpty()) {
                continue;
            }
            ItemStack returned = originHandler.insertItem(0, grabbed, true);
            if (returned.getCount() != grabbed.getCount()) {
                originHandler.insertItem(0, adjacentHandler.extractItem(i, amount, false), false);
                return true;
            }
        }
        return false;
    }

    public static boolean pushToAdjacent(BlockEntity origin, IItemHandler originHandler, int amount, Direction side) {
        ItemStack originStack = originHandler.getStackInSlot(0).copy();
        originStack.setCount(Math.min(amount, originStack.getCount()));
        if (originStack.isEmpty()) {
            return false;
        }

        Level level = origin.getLevel();
        BlockPos adjacentPos = origin.getBlockPos().relative(side);
        Direction adjacentFace = side.getOpposite();

        assert level != null;
        BlockEntity adjacent = level.getBlockEntity(adjacentPos);
        if (adjacent == null) {
            return false;
        }

        LazyOptional<IItemHandler> capability = adjacent.getCapability(ForgeCapabilities.ITEM_HANDLER, adjacentFace);
        IItemHandler adjacentHandler = capability.orElse(null);
        if (adjacentHandler == null) {
            return false;
        }

        int slotCount = adjacentHandler.getSlots();
        for (int i = 0; i < slotCount; i++) {
            ItemStack returned = adjacentHandler.insertItem(i, originStack, true);
            if (returned.getCount() < originStack.getCount()) {
                adjacentHandler.insertItem(i, originHandler.extractItem(0, Math.min(amount, originStack.getCount()), false), false);
                return true;
            }
        }
        return false;
    }
}
