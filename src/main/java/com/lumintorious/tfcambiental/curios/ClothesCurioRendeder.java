package com.lumintorious.tfcambiental.curios;

import com.google.common.collect.Maps;
import com.lumintorious.tfcambiental.item.ClothesItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.Map;

public class ClothesCurioRendeder implements ICurioRenderer {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    @Override
    @SuppressWarnings("unchecked")
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
        ItemStack stack,
        SlotContext slotContext,
        PoseStack poseStack,
        RenderLayerParent<T, M> renderLayerParent,
        MultiBufferSource renderTypeBuffer,
        int light,
        float limbSwing,
        float limbSwingAmount,
        float partialTicks,
        float ageInTicks,
        float netHeadYaw,
        float headPitch) {
        if(slotContext.entity() instanceof Player && stack.getItem() instanceof ClothesItem clothesItem) {
            if(slotContext.entity().getItemBySlot(clothesItem.getEquivalentSlot()).isEmpty()) {
                boolean isSlim = false;
                HumanoidModel<T> model = new HumanoidModel<T>(
                    Minecraft.getInstance().getEntityModels().bakeLayer(
                        isSlim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR
                    )
                );

                CurioClothesLayer layer = new CurioClothesLayer(
                    (RenderLayerParent<Player, HumanoidModel<Player>>) renderLayerParent,
                    (HumanoidModel<Player>) model,
                    (HumanoidModel<Player>) model
                );

                poseStack.pushPose();

                layer.render(stack, poseStack, renderTypeBuffer, light, (Player)slotContext.entity(), limbSwing, limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);

                poseStack.popPose();
            }
        }

    }
}

