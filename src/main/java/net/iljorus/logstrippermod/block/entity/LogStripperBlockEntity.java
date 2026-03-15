package net.iljorus.logstrippermod.block.entity;

import net.iljorus.logstrippermod.config.BaseConfig;
import net.iljorus.logstrippermod.gui.LogStripperMenu;
import net.iljorus.logstrippermod.inventory.InputItemHandler;
import net.iljorus.logstrippermod.inventory.OutputItemHandler;
import net.iljorus.logstrippermod.inventory.SpecialItemHandler;
import net.iljorus.logstrippermod.recipe.LogStrippingRecipe;
import net.iljorus.logstrippermod.util.helpers.InventoryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/*
 *Handles the functionality of the machine
 * */
public class LogStripperBlockEntity extends BlockEntity implements MenuProvider {
    protected InputItemHandler inputSlot = new InputItemHandler(INPUT_SLOT_INDEX) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };
    protected OutputItemHandler outputSlot = new OutputItemHandler(OUTPUT_SLOT_INDEX) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };
    protected SpecialItemHandler axeSlot = new SpecialItemHandler(AXE_SLOT_INDEX) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;
    public static final int AXE_SLOT_INDEX = 2;
    private LazyOptional<IItemHandler> inputAndAxeLazyOptional = LazyOptional.empty();
    private LazyOptional<IItemHandler> outputLazyOptional = LazyOptional.empty();
    private LazyOptional<IItemHandler> inputAndOutputAndAxeLazyOptional = LazyOptional.empty();
    private LazyOptional<IItemHandler> inputAndOutputLazyOptional = LazyOptional.empty();
    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = BaseConfig.COMMON.DURATION.get();

    public LogStripperBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.MACHINE_LOG_STRIPPER_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> LogStripperBlockEntity.this.progress;
                    case 1 -> LogStripperBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> LogStripperBlockEntity.this.progress = pValue;
                    case 1 -> LogStripperBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                if (BaseConfig.COMMON.AXE_SLOT.get()) {
                    return inputAndOutputAndAxeLazyOptional.cast();
                } else {
                    return inputAndOutputLazyOptional.cast();
                }
            }
            if (side == Direction.UP ||
                    side == Direction.NORTH ||
                    side == Direction.EAST ||
                    side == Direction.SOUTH ||
                    side == Direction.WEST) {
                return inputAndAxeLazyOptional.cast();
            }
            if (side == Direction.DOWN) {
                return outputLazyOptional.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inputAndAxeLazyOptional = LazyOptional.of(() -> new CombinedInvWrapper(inputSlot, axeSlot));
        outputLazyOptional = LazyOptional.of(() -> outputSlot);
        inputAndOutputAndAxeLazyOptional = LazyOptional.of(() -> new CombinedInvWrapper(inputSlot, outputSlot, axeSlot));
        inputAndOutputLazyOptional = LazyOptional.of(() -> new CombinedInvWrapper(inputSlot, outputSlot));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputAndAxeLazyOptional.invalidate();
        outputLazyOptional.invalidate();
        inputAndOutputAndAxeLazyOptional.invalidate();
        inputAndOutputLazyOptional.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(3);
        inventory.setItem(INPUT_SLOT_INDEX, inputSlot.getStack());
        inventory.setItem(OUTPUT_SLOT_INDEX, outputSlot.getStack());
        inventory.setItem(AXE_SLOT_INDEX, axeSlot.getStack());

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.logstrippermod.machine_log_stripper_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new LogStripperMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        ListTag nbtTagList = new ListTag();
        nbtTagList.add(inputSlot.serializeNBT());
        nbtTagList.add(outputSlot.serializeNBT());
        nbtTagList.add(axeSlot.serializeNBT());

        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", 3);

        pTag.put("inventory", nbt);
        pTag.putInt("machine_log_stripper_block.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        ListTag tagList = pTag.getCompound("inventory").getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot == INPUT_SLOT_INDEX) {
                this.inputSlot.deserializeNBT(itemTags);
            }
            if (slot == OUTPUT_SLOT_INDEX) {
                this.outputSlot.deserializeNBT(itemTags);
            }
            if (slot == AXE_SLOT_INDEX) {
                this.axeSlot.deserializeNBT(itemTags);
            }
        }
        progress = pTag.getInt("machine_log_stripper_block.progress");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (BaseConfig.COMMON.AXE_SLOT.get() && shouldPreserveAxe()) {
            InventoryHelper.pushToAdjacent(this, this.axeSlot, 1, Direction.UP);
        }

        if (!isPowered() && hasRecipe()) {
            if (!BaseConfig.COMMON.AXE_SLOT.get() || !getAxe().isEmpty()) {
                increaseProgress();
            } else {
                decreaseProgress();
            }

            if (progressFinished()) {
                craftItem();
                resetProgress();
            }
            setChanged();
        } else {
            if (isPowered()) {
                decreaseProgress();
            } else {
                resetProgress();
            }

        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<LogStrippingRecipe> recipe = fetchRecipe();
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());
        int batch_size = recipe.get().getIngredients().get(0).getItems()[0].getCount();

        if (BaseConfig.COMMON.AXE_SLOT.get()) {
            hurtAxe();
        }

        this.inputSlot.extractItem(0, batch_size, false);
        this.outputSlot.setStackInSlot(0, new ItemStack(result.getItem(),
                this.outputSlot.getStackInSlot(0).getCount() + result.getCount()));
    }

    private boolean progressFinished() {
        return progress >= maxProgress;
    }

    private void increaseProgress() {
        progress++;
    }

    private void decreaseProgress() {
        if (progress > 0) {
            progress--;
        }
    }

    private boolean hasRecipe() {
        Optional<LogStrippingRecipe> recipe = fetchRecipe();
        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());
        return canOutputAmount(result.getCount()) && canOutputItem(result.getItem());
    }

    private boolean shouldPreserveAxe() {
        return BaseConfig.COMMON.PRESERVE_AXE.get() &&
                getAxe().getDamageValue() + BaseConfig.COMMON.DURABILITY_DEDUCTION.get() >= getAxe().getMaxDamage();
    }

    private void hurtAxe() {
        ItemStack axeCopy = getAxe().copy();
        if (axeCopy.isEmpty()) {
            return;
        }
        axeCopy.hurt(BaseConfig.COMMON.DURABILITY_DEDUCTION.get(), RandomSource.create(), null);
        axeSlot.setStackInSlot(0, axeCopy);
    }

    private Optional<LogStrippingRecipe> fetchRecipe() {
        SimpleContainer inventory = new SimpleContainer(2);
        inventory.setItem(INPUT_SLOT_INDEX, inputSlot.getStackInSlot(0));
        return this.level.getRecipeManager().getRecipeFor(LogStrippingRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canOutputItem(Item item) {
        return this.outputSlot.getStackInSlot(0).isEmpty() || this.outputSlot.getStackInSlot(0).is(item);
    }

    private boolean canOutputAmount(int count) {
        return this.outputSlot.getStackInSlot(0).getCount() + count <= this.outputSlot.getStackInSlot(0).getMaxStackSize();
    }

    public ItemStack getAxe() {
        return this.axeSlot.getStackInSlot(0);
    }

    private boolean isPowered() {
        return this.level.hasNeighborSignal(this.getBlockPos());
    }
}
