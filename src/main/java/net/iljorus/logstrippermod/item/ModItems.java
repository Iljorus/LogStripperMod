package net.iljorus.logstrippermod.item;

import net.iljorus.logstrippermod.LogStripperMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LogStripperMod.MOD_ID);

    public static final RegistryObject<Item> DIGITAL_AXE_HEAD = ITEMS.register("digital_axe_head",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
