package com.lumintorious.tfcambiental.curios;

import com.google.common.collect.Maps;
import com.lumintorious.tfcambiental.item.ClothesItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class CurioClothesLayer extends HumanoidArmorLayer<Player, HumanoidModel<Player>, HumanoidModel<Player>> {
    public final HumanoidModel<Player> innerModel;
    public final HumanoidModel<Player> outerModel;
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();

    public CurioClothesLayer(RenderLayerParent<Player, HumanoidModel<Player>> p_117075_, HumanoidModel<Player> innerModel, HumanoidModel<Player> outerModel) {
        super(p_117075_, innerModel, outerModel);
        this.innerModel = innerModel;
        this.outerModel = outerModel;
    }

    public void render(
        ItemStack itemStack,
        @NotNull PoseStack pMatrixStack,
        @NotNull MultiBufferSource pBuffer,
        int pPackedLight,
        @NotNull Player player,
        float pLimbSwing,
        float pLimbSwingAmount,
        float pPartialTicks,
        float pAgeInTicks,
        float pNetHeadYaw,
        float pHeadPitch
    ) {
        this.renderArmorPiece2(itemStack, pMatrixStack, pBuffer, player, EquipmentSlot.CHEST, pPackedLight, this.getArmorModel(EquipmentSlot.CHEST));
        this.renderArmorPiece2(itemStack, pMatrixStack, pBuffer, player, EquipmentSlot.LEGS, pPackedLight, this.getArmorModel(EquipmentSlot.LEGS));
        this.renderArmorPiece2(itemStack, pMatrixStack, pBuffer, player, EquipmentSlot.FEET, pPackedLight, this.getArmorModel(EquipmentSlot.FEET));
        this.renderArmorPiece2(itemStack, pMatrixStack, pBuffer, player, EquipmentSlot.HEAD, pPackedLight, this.getArmorModel(EquipmentSlot.HEAD));
    }

    private HumanoidModel<Player> getArmorModel(EquipmentSlot p_117079_) {
        return (HumanoidModel<Player>)(this.usesInnerModel(p_117079_) ? this.innerModel : this.outerModel);
    }

    private boolean usesInnerModel(EquipmentSlot pSlot) {
        return pSlot == EquipmentSlot.LEGS;
    }


    private void renderArmorPiece2(ItemStack itemstack, PoseStack p_117119_, MultiBufferSource p_117120_, Player p_117121_, EquipmentSlot p_117122_, int p_117123_, HumanoidModel<Player> p_117124_) {
        if (itemstack.getItem() instanceof ClothesItem clothesItem) {
            if (clothesItem.getEquivalentSlot() == p_117122_) {
                this.getParentModel().copyPropertiesTo(p_117124_);
                this.setPartVisibility(p_117124_, p_117122_);
                net.minecraft.client.model.Model model = getArmorModelHook(p_117121_, itemstack, p_117122_, p_117124_);
                boolean flag = this.usesInnerModel(p_117122_);
                boolean flag1 = itemstack.hasFoil();
                this.renderModel(p_117119_, p_117120_, p_117123_, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource2(p_117121_, itemstack, p_117122_, null));
            }
        }
    }


    public ResourceLocation getArmorResource2(net.minecraft.world.entity.Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        ClothesItem item = (ClothesItem)stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format(java.util.Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (usesInnerModel(slot) ? 2 : 1), type == null ? "" : String.format(java.util.Locale.ROOT, "_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
        }

        return resourcelocation;
    }

    public void renderModel(PoseStack p_117107_, MultiBufferSource p_117108_, int p_117109_, boolean p_117111_, net.minecraft.client.model.Model p_117112_, float p_117114_, float p_117115_, float p_117116_, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(p_117108_, RenderType.armorCutoutNoCull(armorResource), false, p_117111_);
        p_117112_.renderToBuffer(p_117107_, vertexconsumer, p_117109_, OverlayTexture.NO_OVERLAY, p_117114_, p_117115_, p_117116_, 1.0F);
    }
}
