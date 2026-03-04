package net.iljorus.logstrippermod.block.entity;

import net.iljorus.logstrippermod.LogStripperMod;
import net.iljorus.logstrippermod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/*
 * Handles the registry of BlockEntities
 * */

public class ModBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LogStripperMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    public static final RegistryObject<BlockEntityType<LogStripperBlockEntity>> MACHINE_LOG_STRIPPER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("machine_log_stripper_block_entity",
                    () -> BlockEntityType.Builder.of(LogStripperBlockEntity::new,
                            ModBlocks.MACHINE_LOG_STRIPPER.get()).build(null));
}
