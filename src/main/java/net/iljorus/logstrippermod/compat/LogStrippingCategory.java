package net.iljorus.logstrippermod.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.iljorus.logstrippermod.LogStripperMod;
import net.iljorus.logstrippermod.block.ModBlocks;
import net.iljorus.logstrippermod.block.entity.LogStripperBlockEntity;
import net.iljorus.logstrippermod.config.BaseConfig;
import net.iljorus.logstrippermod.gui.GuiConstants;
import net.iljorus.logstrippermod.gui.LogStripperScreen;
import net.iljorus.logstrippermod.recipe.LogStrippingRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LogStrippingCategory implements IRecipeCategory<LogStrippingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "log_stripping");
    public static final RecipeType<LogStrippingRecipe> LOG_STRIPPING_TYPE = new RecipeType<>(UID, LogStrippingRecipe.class);


    private final IDrawable background;
    private final IDrawable icon;
    private Component name;
    private IDrawableStatic progressBackground;
    private IDrawableAnimated progress;
    private NonNullList<IDrawableStatic> slots = NonNullList.create();


    public LogStrippingCategory(IGuiHelper helper) {
        //this.background = helper.createDrawable(BASE, 0, 0, 176, 83);
        this.background = helper.drawableBuilder(LogStripperScreen.BASE_NO_AXE, 26, 11, 140, 62)
                .addPadding(0, 0, 16, 8)
                .build();
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MACHINE_LOG_STRIPPER.get()));
        this.name = Component.translatable("recipe.logstrippermod.log_stripping_name");
        this.progressBackground = helper.createDrawable(LogStripperScreen.PROGRESS_ICON, 0, 0, 16, 16);
        this.progress = helper.createAnimatedDrawable(helper.createDrawable(LogStripperScreen.PROGRESS_ICON, 16, 0, 16, 16),
                BaseConfig.COMMON.DURATION.get() * 2,
                IDrawableAnimated.StartDirection.LEFT,
                false);
        this.slots.add(helper.createDrawable(LogStripperScreen.SLOT, 0, 0, 18, 18));
        this.slots.add(helper.createDrawable(LogStripperScreen.SLOT, 0, 0, 18, 18));
    }

    @Override
    public @NotNull RecipeType<LogStrippingRecipe> getRecipeType() {
        return LOG_STRIPPING_TYPE;
    }

    @Override
    public int getWidth() {
        return this.background.getWidth();
    }

    @Override
    public int getHeight() {
        return this.background.getHeight();
    }

    @Override
    public @NotNull Component getTitle() {
        return this.name;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LogStrippingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, GuiConstants.INPUT_SLOT_X - 10, GuiConstants.INPUT_SLOT_Y - 11).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, GuiConstants.OUTPUT_SLOT_X - 10, GuiConstants.OUTPUT_SLOT_Y - 11).addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(@NotNull LogStrippingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        progressBackground.draw(guiGraphics, GuiConstants.PROGRESS_ICON_X - 10, GuiConstants.PROGRESS_ICON_Y - 11);
        progress.draw(guiGraphics, GuiConstants.PROGRESS_ICON_X - 10, GuiConstants.PROGRESS_ICON_Y - 11);
        slots.get(LogStripperBlockEntity.INPUT_SLOT).draw(guiGraphics, GuiConstants.INPUT_SLOT_X - 11, GuiConstants.INPUT_SLOT_Y - 12);
        slots.get(LogStripperBlockEntity.OUTPUT_SLOT).draw(guiGraphics, GuiConstants.OUTPUT_SLOT_X - 11, GuiConstants.OUTPUT_SLOT_Y - 12);
    }
}
