package net.iljorus.logstrippermod;

import net.iljorus.logstrippermod.block.ModBlocks;
import net.iljorus.logstrippermod.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LogStripperMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> LOG_STRIPPER_TAB = CREATIVE_MODE_TABS.register("log_stripper_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModBlocks.MACHINE_LOG_STRIPPER.get()))
                    .title(Component.translatable("creativetab.log_stripper_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.MACHINE_LOG_STRIPPER.get());
                        pOutput.accept(ModItems.DIGITAL_AXE_HEAD.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
