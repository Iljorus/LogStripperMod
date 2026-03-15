package net.iljorus.logstrippermod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.iljorus.logstrippermod.LogStripperMod;
import net.iljorus.logstrippermod.block.entity.LogStripperBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LogStrippingRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;

    public LogStrippingRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }
        return inputItems.get(0).test(pContainer.getItem(LogStripperBlockEntity.INPUT_SLOT_INDEX));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer pContainer, @NotNull RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    //getId
    @Override
    public @NotNull ResourceLocation m_6423_() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<LogStrippingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "log_stripping";
    }

    public static class Serializer implements RecipeSerializer<LogStrippingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        //Deprecation only affects 1.21
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "log_stripping");

        //fromJson, pRecipeId, pSerializedRecipe
        @Override
        public @NotNull LogStrippingRecipe m_6729_(@NotNull ResourceLocation p_44103_, @NotNull JsonObject p_44104_) {
            //ItemStackFromJson
            ItemStack output = ShapedRecipe.m_151274_(GsonHelper.getAsJsonObject(p_44104_, "result"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(p_44104_, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                //fromJson
                inputs.set(i, Ingredient.m_43917_(ingredients.get(i)));
            }

            return new LogStrippingRecipe(inputs, output, p_44103_);
        }

        //pRecipeId
        @Override
        public @Nullable LogStrippingRecipe fromNetwork(@NotNull ResourceLocation p_44105_, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            inputs.replaceAll(index -> Ingredient.fromNetwork(pBuffer));

            ItemStack output = pBuffer.readItem();
            return new LogStrippingRecipe(inputs, output, p_44105_);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, LogStrippingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
