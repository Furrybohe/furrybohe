package com.bulefire.furrybohe.recipe;

import com.bulefire.furrybohe.register.FurryBoHeRecipeSerializerRegister;
import com.bulefire.furrybohe.register.FurryBoHeRecipesRegister;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FurRecipe implements Recipe<Container> {
    public static final int DEFAULT_COTTON_COUNT = 2;
    public static final int DEFAULT_WOOL_COUNT = 1;
    
    private final ResourceLocation id;
    private final Ingredient leather;
    private final Ingredient cotton;
    private final Ingredient wool;
    private final Ingredient dye;
    private final int cottonCount;
    private final int woolCount;
    private final ItemStack result;
    
    public FurRecipe(ResourceLocation id, Ingredient leather, Ingredient cotton, Ingredient wool, Ingredient dye,
                     int cottonCount, int woolCount, ItemStack result) {
        this.id = id;
        this.leather = leather;
        this.cotton = cotton;
        this.wool = wool;
        this.dye = dye;
        this.cottonCount = Math.max(1, cottonCount);
        this.woolCount = Math.max(1, woolCount);
        this.result = result;
    }
    
    public int fillerCost(@NotNull ItemStack filler) {
        if (filler.isEmpty()) return 0;
        if (cotton.test(filler)) return cottonCount;
        if (wool.test(filler)) return woolCount;
        return 0;
    }
    
    @Override
    public boolean matches(@NotNull Container in, @NotNull Level level) {
        ItemStack leatherStack = in.getItem(0);
        ItemStack filler = in.getItem(1);
        ItemStack dyeStack = in.getItem(2);
        
        if (!leather.test(leatherStack) || leatherStack.getCount() < 1) return false;
        if (!dye.test(dyeStack) || dyeStack.getCount() < 1) return false;
        
        int cost = fillerCost(filler);
        return cost > 0 && filler.getCount() >= cost;
    }
    
    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess ra) {
        return result.copy();
    }
    
    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }
    
    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess ra) {
        return result;
    }
    
    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(leather);
        list.add(cotton);
        list.add(wool);
        list.add(dye);
        return list;
    }
    
    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }
    
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return FurryBoHeRecipeSerializerRegister.FUR_RECIPE_SERIALIZER.get();
    }
    
    @Override
    public @NotNull RecipeType<?> getType() {
        return FurryBoHeRecipesRegister.FUR_RECIPE_TYPE_REGISTER.get();
    }
    
    public Ingredient getLeather() {
        return leather;
    }
    
    public Ingredient getCotton() {
        return cotton;
    }
    
    public Ingredient getWool() {
        return wool;
    }
    
    public Ingredient getDye() {
        return dye;
    }
    
    public int getCottonCount() {
        return cottonCount;
    }
    
    public int getWoolCount() {
        return woolCount;
    }
    
    public static class Serializer implements RecipeSerializer<FurRecipe> {
        @Override
        public @NotNull FurRecipe fromJson(@NotNull ResourceLocation rl, @NotNull JsonObject json) {
            Ingredient leather = Ingredient.fromJson(json.get("leather"));
            Ingredient cotton = Ingredient.fromJson(json.get("cotton"));
            Ingredient wool = Ingredient.fromJson(json.get("wool"));
            Ingredient dye = Ingredient.fromJson(json.get("dye"));
            int cottonCount = GsonHelper.getAsInt(json, "cotton_count", DEFAULT_COTTON_COUNT);
            int woolCount = GsonHelper.getAsInt(json, "wool_count", DEFAULT_WOOL_COUNT);
            ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
            return new FurRecipe(rl, leather, cotton, wool, dye, cottonCount, woolCount, result);
        }
        
        @Override
        public @Nullable FurRecipe fromNetwork(@NotNull ResourceLocation rl, @NotNull FriendlyByteBuf buf) {
            Ingredient leather = Ingredient.fromNetwork(buf);
            Ingredient cotton = Ingredient.fromNetwork(buf);
            Ingredient wool = Ingredient.fromNetwork(buf);
            Ingredient dye = Ingredient.fromNetwork(buf);
            int cottonCount = buf.readVarInt();
            int woolCount = buf.readVarInt();
            ItemStack result = buf.readItem();
            return new FurRecipe(rl, leather, cotton, wool, dye, cottonCount, woolCount, result);
        }
        
        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull FurRecipe r) {
            r.leather.toNetwork(buf);
            r.cotton.toNetwork(buf);
            r.wool.toNetwork(buf);
            r.dye.toNetwork(buf);
            buf.writeVarInt(r.cottonCount);
            buf.writeVarInt(r.woolCount);
            buf.writeItem(r.result);
        }
    }
}
