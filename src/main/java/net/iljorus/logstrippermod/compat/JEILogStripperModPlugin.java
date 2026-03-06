package net.iljorus.logstrippermod.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.iljorus.logstrippermod.LogStripperMod;
import net.iljorus.logstrippermod.block.ModBlocks;
import net.iljorus.logstrippermod.gui.GuiConstants;
import net.iljorus.logstrippermod.gui.LogStripperScreen;
import net.iljorus.logstrippermod.recipe.LogStrippingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEILogStripperModPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new LogStrippingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<LogStrippingRecipe> logStrippingRecipes = recipeManager.getAllRecipesFor(LogStrippingRecipe.Type.INSTANCE);
        registration.addRecipes(LogStrippingCategory.LOG_STRIPPING_TYPE, logStrippingRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(LogStripperScreen.class,
                GuiConstants.PROGRESS_ICON_X + 1,
                GuiConstants.PROGRESS_ICON_Y + 2,
                14,
                14,
                LogStrippingCategory.LOG_STRIPPING_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MACHINE_LOG_STRIPPER.get()), LogStrippingCategory.LOG_STRIPPING_TYPE);
    }
}
