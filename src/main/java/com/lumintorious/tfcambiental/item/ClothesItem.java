package com.lumintorious.tfcambiental.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

public class ClothesItem extends Item implements Vanishable {
    private final ArmorMaterial material;
    private final EquipmentSlot slot;

    public ClothesItem(ArmorMaterial material, EquipmentSlot slot, Properties pProperties) {
        super(pProperties);
        this.material = material;
        this.slot = slot;

    }

    public ArmorMaterial getMaterial() {
        return material;
    }

    public EquipmentSlot getEquivalentSlot() {
        return this.slot;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return super.getDamage(stack);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return material.getDurabilityForSlot(getEquivalentSlot());
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
}
