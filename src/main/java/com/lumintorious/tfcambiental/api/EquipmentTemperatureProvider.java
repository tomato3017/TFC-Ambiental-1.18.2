package com.lumintorious.tfcambiental.api;

import com.lumintorious.tfcambiental.TFCAmbientalConfig;
import com.lumintorious.tfcambiental.item.TemperatureAlteringMaterial;
import com.lumintorious.tfcambiental.modifier.EnvironmentalModifier;
import com.lumintorious.tfcambiental.modifier.TempModifier;
import com.lumintorious.tfcambiental.modifier.TempModifierStorage;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;

import java.util.Optional;

@FunctionalInterface
public interface EquipmentTemperatureProvider {
    Optional<TempModifier> getModifier(Player player, ItemStack stack);

    static Optional<TempModifier> handleClothes(Player player, ItemStack stack) {
        if(stack.getItem() instanceof ArmorItem armorItem) {
            if(armorItem.getMaterial() instanceof TemperatureAlteringMaterial tempMaterial) {
                return Optional.of(tempMaterial.getTempModifier(stack));
            } else if(armorItem.getMaterial() == ArmorMaterials.LEATHER) {
                return TempModifier.defined(stack.getItem().getRegistryName().toString() + "_" + armorItem.getEquipmentSlot(stack), 2.5f, 0.15f);
            } else {
                return TempModifier.defined(stack.getItem().getRegistryName().toString() + "_" + armorItem.getEquipmentSlot(stack), 2f, 0.0f);
            }
        }
        return TempModifier.none();
    }

    static Optional<TempModifier> handleSunlightCap(Player player, ItemStack stack) {
        float AVERAGE = TFCAmbientalConfig.COMMON.averageTemperature.get().floatValue();
        if(stack.getItem() instanceof ArmorItem armor) {
            if(armor.getSlot() == EquipmentSlot.HEAD) {
                if(player.level.getBrightness(LightLayer.SKY, player.getOnPos().above()) > 14) {
                    float envTemp = EnvironmentalModifier.getEnvironmentTemperatureWithTimeOfDay(player);
                    if(envTemp > AVERAGE) {
                        float diff = envTemp - AVERAGE;
                        Optional<TempModifier> helmetMod = handleClothes(player, stack);
                        if(helmetMod.isPresent()) {
                            diff = diff - helmetMod.get().getChange();
                        };
                        return TempModifier.defined("sunlight_protection", diff * -0.2f, -0.5f);
                    }
                }
            }
        }
        return TempModifier.none();
    }

    static void evaluateAll(Player player, TempModifierStorage storage) {
        Iterable<ItemStack> armorItems = player.getArmorSlots();
        for(ItemStack stack : armorItems) {
            for(var fn : AmbientalRegistry.EQUIPMENT) {
                storage.add(fn.getModifier(player, stack));
            }
        }
    }
}
