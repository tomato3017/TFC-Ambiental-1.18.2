package com.lumintorious.tfcambiental.item;

import com.lumintorious.tfcambiental.TFCAmbiental;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TFCAmbientalItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TFCAmbiental.MOD_ID);

    public static final RegistryObject<Item> WOOL_HAT = ITEMS.register(
        "wool_hat",
        () -> new ArmorItem(WoolClothesMaterial.MATERIAL, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
    public static final RegistryObject<Item> WOOL_SWEATER = ITEMS.register(
            "wool_sweater",
            () -> new ArmorItem(WoolClothesMaterial.MATERIAL, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
    public static final RegistryObject<Item> WOOL_PANTS = ITEMS.register(
            "wool_pants",
            () -> new ArmorItem(WoolClothesMaterial.MATERIAL, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
    public static final RegistryObject<Item> WOOL_BOOTS = ITEMS.register(
            "wool_boots",
            () -> new ArmorItem(WoolClothesMaterial.MATERIAL, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );

    public static final RegistryObject<Item> SILK_COWL = ITEMS.register(
            "silk_cowl",
            () -> new ArmorItem(SilkClothesMaterial.MATERIAL, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
    public static final RegistryObject<Item> SILK_SHIRT = ITEMS.register(
            "silk_shirt",
            () -> new ArmorItem(SilkClothesMaterial.MATERIAL, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
    public static final RegistryObject<Item> SILK_PANTS = ITEMS.register(
            "silk_pants",
            () -> new ArmorItem(SilkClothesMaterial.MATERIAL, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
    public static final RegistryObject<Item> SILK_SHOES = ITEMS.register(
            "silk_shoes",
            () -> new ArmorItem(SilkClothesMaterial.MATERIAL, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );

    public static final RegistryObject<Item> BURLAP_COWL = ITEMS.register(
            "burlap_cowl",
            () -> new ArmorItem(BurlapClothesMaterial.MATERIAL, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
    public static final RegistryObject<Item> BURLAP_SHIRT = ITEMS.register(
            "burlap_shirt",
            () -> new ArmorItem(BurlapClothesMaterial.MATERIAL, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
    public static final RegistryObject<Item> BURLAP_PANTS = ITEMS.register(
            "burlap_pants",
            () -> new ArmorItem(BurlapClothesMaterial.MATERIAL, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
    public static final RegistryObject<Item> BURLAP_SHOES = ITEMS.register(
            "burlap_shoes",
            () -> new ArmorItem(BurlapClothesMaterial.MATERIAL, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))
    );
}
