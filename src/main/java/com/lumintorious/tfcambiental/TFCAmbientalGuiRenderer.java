package com.lumintorious.tfcambiental;

import com.lumintorious.tfcambiental.capability.TemperatureCapability;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class TFCAmbientalGuiRenderer {
    public static final IIngameOverlay TEMPERATURE_OVERLAY = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.BOSS_HEALTH_ELEMENT, "Temperature", TFCAmbientalGuiRenderer::render);
    public static final ResourceLocation COLD_VIGNETTE = new ResourceLocation("tfcambiental:textures/gui/cold_vignette.png");
    public static final ResourceLocation HOT_VIGNETTE = new ResourceLocation("tfcambiental:textures/gui/hot_vignette.png");
    public static final ResourceLocation MINUS = new ResourceLocation("tfcambiental:textures/gui/lower.png");
    public static final ResourceLocation PLUS = new ResourceLocation("tfcambiental:textures/gui/higher.png");
    public static final ResourceLocation MINUSER = new ResourceLocation("tfcambiental:textures/gui/lowerer.png");
    public static final ResourceLocation PLUSER = new ResourceLocation("tfcambiental:textures/gui/higherer.png");

    public static void load() {
        OverlayRegistry.enableOverlay(TEMPERATURE_OVERLAY, true);
    }

    private static TemperatureCapability defaultCap = new TemperatureCapability();

    public static void render(ForgeIngameGui gui, PoseStack stack, float partialTicks, int widthh, int heightt) {
        Minecraft mc = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;
        if(player.isCreative() || !player.isAlive() || player.isSpectator()) {
            return;
        }
        TemperatureCapability tempSystem = player.getCapability(TemperatureCapability.CAPABILITY, null).orElse(defaultCap);
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        float redCol = 0f;
        float greenCol = 0f;
        float blueCol = 0f;

        float offsetY = 0f;
        float offsetX = 0f;
        float offsetYArrow = 0f;
        BlockPos pos = new BlockPos(player.getPosition(0));
        BlockPos pos2 = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
//        if(player.isRi()) {
//            offsetY = -10f;
//            offsetYArrow = -10f;
//            offsetX = 0;
//        }
        BlockState state = player.getLevel().getBlockState(pos2);
        Block block = state.getBlock();
//        if(block == FluidsTFC.HOT_WATER.get().getBlock() ||
//                block == FluidsTFC.SALT_WATER.get().getBlock() ||
//                block == FluidsTFC.FRESH_WATER.get().getBlock()) {
//            offsetY = -10f;
//            offsetX = 0;
//        }

        drawTemperatureVignettes(width, height, player);
        float temperature = 1f;
        int healthRowHeight = mc.getWindow().getGuiScaledHeight();
        int armorRowHeight = healthRowHeight - 51;
        int mid = mc.getWindow().getGuiScaledWidth() / 2;
        temperature = tempSystem.getTemperature();
        RenderSystem.enableBlend();
        float AVERAGE = TFCAmbientalConfig.COMMON.averageTemperature.get().floatValue();
        float HOT_THRESHOLD = TFCAmbientalConfig.COMMON.hotThreshold.get().floatValue();
        float COOL_THRESHOLD = TFCAmbientalConfig.COMMON.coolThreshold.get().floatValue();
        if(temperature > AVERAGE) {
            float hotRange = HOT_THRESHOLD - AVERAGE + 2;
            float red = Math.max(0, Math.min(1, (temperature - AVERAGE) / hotRange));
            redCol = 1F;
            greenCol = 1.0F - red / 2.4F;
            blueCol = 1.0F - red / 1.6F;
        }else {
            float coolRange = AVERAGE - COOL_THRESHOLD - 2;
            float blue = Math.max(0, Math.min(1, (AVERAGE - temperature) / coolRange));
            redCol = 1.0F - blue / 1.6F;
            greenCol = 1.0F - blue / 2.4F;
            blueCol = 1.0F;
        }
        RenderSystem.setShaderColor(redCol, greenCol, blueCol, 0.9F);
        RenderSystem.enableBlend();
        java.awt.Color c = new java.awt.Color(redCol, greenCol, blueCol, 1.0F);
        RenderSystem.setShaderColor(redCol, greenCol, blueCol, 0.9F);
//        GL11.glEnable(GL11.GL_BLEND);
        float speed = tempSystem.getChangeSpeed();
        float change = tempSystem.getChange();
        if(change > 0) {
            if(change > TemperatureCapability.HIGH_CHANGE) {
                drawTexturedModalRect(gui, stack, mid - 8, armorRowHeight - 4 + offsetYArrow, 16, 16, PLUSER);
            }else {
                drawTexturedModalRect(gui, stack, mid - 8, armorRowHeight - 4 + offsetYArrow, 16, 16, PLUS);
            }
        }else {
            if(change < -TemperatureCapability.HIGH_CHANGE) {
                drawTexturedModalRect(gui, stack, mid - 8, armorRowHeight - 4 + offsetYArrow, 16, 16, MINUSER);
            }else {
                drawTexturedModalRect(gui, stack, mid - 8, armorRowHeight - 4 + offsetYArrow, 16, 16, MINUS);
            }
        }
        if((player.isCrouching() || TFCAmbientalConfig.CLIENT.alwaysShowTemp.get()  /* || !TFCAmbientalConfig.CLIENT.sneakyDetails */) && tempSystem instanceof TemperatureCapability) {
            TemperatureCapability sys = (TemperatureCapability)tempSystem;
            float targetFormatted = sys.getTarget();
            float tempFormatted = sys.getTemperature();
            float changeFormatted = sys.getChange();
//            if(true /*!TFCAmbientalConfig.CLIENT.celsius*/) {
//                targetFormatted = targetFormatted * (9 / 5) + 32;
//                tempFormatted = tempFormatted * (9 / 5) + 32;
//                changeFormatted = changeFormatted * (9 / 5);
//            }
//            FontRe
            Font f = gui.getFont();
            String tempStr = String.format("%.1f\u00BA -> %.1f\u00BA", temperature, targetFormatted);
            String changeStr = String.format("%.3f\u00BA/s", change);
            f.drawShadow(stack, tempStr, mid + 50 - f.width(tempStr) / 2F + offsetX, armorRowHeight + 1 + offsetY, c.getRGB());
//            f.drawShadow(stack, changeStr, mid - 50 - f.width(changeStr) / 2F, armorRowHeight + 1F, c.getRGB());

        }
        RenderSystem.setShaderColor(1f, 1f, 1f, 0.9F);
        RenderSystem.disableBlend();
    }

    private static void drawTexturedModalRect(ForgeIngameGui gui, PoseStack stack, float x, float y, float width, float height, ResourceLocation loc)
    {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(loc);
        RenderSystem.setShaderTexture(0, loc);

        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.depthMask(false);
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(x, y + height, -90.0D).uv(0.0f, 1.0f).endVertex();
        buffer.vertex(x + width, y +height, -90.0D).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(x +width, y, -90.0D).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(x, y, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }


    private static void drawTemperatureVignettes(int width, int height, Player player)
    {
        ResourceLocation vignetteLocation = null;
        float temperature = 1f;
        TemperatureCapability tempSystem = player.getCapability(TemperatureCapability.CAPABILITY, null).orElse(defaultCap);
        temperature = tempSystem.getTemperature();

        float BURN_THRESHOLD = TFCAmbientalConfig.COMMON.burnThreshold.get().floatValue();
        float FREEZE_THRESHOLD = TFCAmbientalConfig.COMMON.freezeThreshold.get().floatValue();

        float opacity = 1f;
        if(temperature > BURN_THRESHOLD - 2.5f) {
            vignetteLocation = HOT_VIGNETTE;
            opacity = Math.min(0.80f ,(temperature - (BURN_THRESHOLD - 2.5f)) / 18);
        }else if(temperature < FREEZE_THRESHOLD + 2.5f) {
            vignetteLocation = COLD_VIGNETTE;
            opacity = Math.min(0.80f ,((FREEZE_THRESHOLD + 2.5f) - temperature) / 18);
        }

            if (vignetteLocation != null)
            {
                RenderSystem.setShaderTexture(0, vignetteLocation);

                RenderSystem.enableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder buffer = tessellator.getBuilder();
                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                buffer.vertex(0.0D, height, -90.0D).uv(0.0F, 1.0F).endVertex();
                buffer.vertex(width, height, -90.0D).uv(1.0F, 1.0F).endVertex();
                buffer.vertex(width, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
                buffer.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
                tessellator.end();

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

    }
}
