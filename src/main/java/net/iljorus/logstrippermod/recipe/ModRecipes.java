package net.iljorus.logstrippermod.recipe;

import net.iljorus.logstrippermod.LogStripperMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LogStripperMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }

    public static final RegistryObject<RecipeSerializer<LogStrippingRecipe>> LOG_STRIPPING_SERIALIZER =
            SERIALIZERS.register("log_stripping", () -> LogStrippingRecipe.Serializer.INSTANCE);
}
