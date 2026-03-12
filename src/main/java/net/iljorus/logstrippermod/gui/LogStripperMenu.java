package net.iljorus.logstrippermod.gui;

import net.iljorus.logstrippermod.block.ModBlocks;
import net.iljorus.logstrippermod.block.entity.LogStripperBlockEntity;
import net.iljorus.logstrippermod.block.entity.config.RedstoneConfig;
import net.iljorus.logstrippermod.config.BaseConfig;
import net.iljorus.logstrippermod.inventory.slot.OutPutSlotItemHandler;
import net.iljorus.logstrippermod.inventory.slot.SpecialSlotItemHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/*
 * Called when player opens the TE-Inventory.
 * Handles Player-Inventory & TE-Inventory communication.
 * */
public class LogStripperMenu extends AbstractContainerMenu {
    public final LogStripperBlockEntity blockEntity;
    private final Level level;
    //Defined in LogStripperBlockEntity class, [0]=progress, [1]=maxProgress, [2]=redstoneConfig
    private final ContainerData data;

    public LogStripperMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(TE_INVENTORY_SLOT_COUNT));
    }

    public LogStripperMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.LOG_STRIPPER_MENU.get(), pContainerId);
        checkContainerSize(inv, BaseConfig.COMMON.AXE_SLOT.get() ? 3 : 2);
        this.blockEntity = ((LogStripperBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, LogStripperBlockEntity.INPUT_SLOT_INDEX, GuiConstants.INPUT_SLOT_X, GuiConstants.INPUT_SLOT_Y));
            this.addSlot(new OutPutSlotItemHandler(iItemHandler, LogStripperBlockEntity.OUTPUT_SLOT_INDEX, GuiConstants.OUTPUT_SLOT_X, GuiConstants.OUTPUT_SLOT_Y));
            if (BaseConfig.COMMON.AXE_SLOT.get()) {
                this.addSlot(new SpecialSlotItemHandler(iItemHandler, LogStripperBlockEntity.AXE_SLOT_INDEX, GuiConstants.AXE_SLOT_X, GuiConstants.AXE_SLOT_Y));
            }
        });
        addDataSlots(data);
    }

    @Override
    public void removed(@NotNull Player pPlayer) {
        this.blockEntity.setChanged();
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 16;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledAxeDurability() {
        int scaleSize = 42;
        ItemStack stack = this.blockEntity.getAxe();
        if (stack == ItemStack.EMPTY) {
            return 0;
        }
        int maxDurability = stack.getMaxDamage();
        int durability = maxDurability - stack.getDamageValue();

        return maxDurability != 0 ? scaleSize * durability / maxDurability : 0;
    }

    public RedstoneConfig getRedstoneConfig() {
        return RedstoneConfig.fromIntValue(this.data.get(2));
    }

    public void setRedstoneConfig(RedstoneConfig config) {
        this.data.set(2, config.getIntValue());
    }

    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots and the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    //Number of Slots in Tile Entity
    private static final int TE_INVENTORY_SLOT_COUNT = BaseConfig.COMMON.AXE_SLOT.get() ? 3 : 2;

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {  //TODO add axe support (supposed to go in slot two)
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            //Prioritizing Axe slot if moving axe-like item
            if (sourceStack.getItem().canPerformAction(Items.OAK_LOG.getDefaultInstance(), ToolActions.AXE_STRIP)) {
                if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 2, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.MACHINE_LOG_STRIPPER.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
