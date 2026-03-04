package net.iljorus.logstrippermod.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SpecialSlotItemHandler extends SlotItemHandler {
    public SpecialSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if(stack.isEmpty()){
            return false;
        }
        return stack.getItem().canPerformAction(Items.OAK_LOG.getDefaultInstance(), ToolActions.AXE_STRIP);
    }

    @Override
    public int getMaxStackSize(){
        return 1;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack){
        return 1;
    }
}
