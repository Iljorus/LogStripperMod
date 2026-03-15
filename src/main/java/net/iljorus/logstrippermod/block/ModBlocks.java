package net.iljorus.logstrippermod.block;

import net.iljorus.logstrippermod.LogStripperMod;
import net.iljorus.logstrippermod.block.custom.LogStripperBlock;
import net.iljorus.logstrippermod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/*
 * Handles the registry of normal blocks
 * */

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, LogStripperMod.MOD_ID);

    public static final RegistryObject<Block> MACHINE_LOG_STRIPPER = registerAsBlockAndItem("machine_log_stripper_block",
            () -> new LogStripperBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().noOcclusion().destroyTime(2.5F)));


    private static <T extends Block> RegistryObject<T> registerAsBlockAndItem(String name, Supplier<T> supplier) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties())); //MACHINE_LOG_STRIPPER_ITEM =
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
