package com.lumintorious.tfcambiental.item;

import com.lumintorious.tfcambiental.modifier.TempModifier;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class StrawClothesMaterial implements ArmorMaterial, TemperatureAlteringMaterial {
    public static final StrawClothesMaterial MATERIAL = new StrawClothesMaterial();

    @Override
    public int getDurabilityForSlot(EquipmentSlot pSlot) {
        return 100;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot pSlot) {
        return 0;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return SoundEvents.GRASS_BREAK;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.of(new ItemStack(TFCItems.STRAW.get(), 1));
    }

    @Override
    public @NotNull String getName() {
        return "tfcambiental:straw";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }

    @Override
    public TempModifier getTempModifier(ItemStack stack) {
        return new TempModifier(stack.getItem().getRegistryName().toString(), 0, -0.1f);
    }
}
