package com.lumintorious.tfcambiental;

import com.lumintorious.tfcambiental.capability.TemperaturePacket;
import com.lumintorious.tfcambiental.item.TFCAmbientalItems;
import com.lumintorious.tfcambiental.item.TemperatureAlteringMaterial;
import com.lumintorious.tfcambiental.modifier.TempModifier;
import com.mojang.logging.LogUtils;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("tfcambiental")
public class TFCAmbiental
{
    public static final String MOD_ID = "tfcambiental";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String VERSION = Integer.toString(1);
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(Helpers.identifier("tfcambiental"), () -> VERSION, VERSION::equals, VERSION::equals);

    public TFCAmbiental()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.addListener(this::addTooltips);
        if(FMLEnvironment.dist == Dist.CLIENT) {
            TFCAmbientalGuiRenderer.load();
        }

        TFCAmbientalConfig.init();
        register(
                0,
                TemperaturePacket.class,
                TemperaturePacket::encode,
                TemperaturePacket::new,
                TemperaturePacket::handle
        );
        TFCAmbientalEventHandler.init();
        TFCAmbientalItems.ITEMS.register(eventBus);
    }

    private String formatAttribute(float attribute) {
        if(attribute != 0) {
            if(attribute > 0) {
                return attribute % 1 == 0 ? "+" + ((int) attribute) : "+" + attribute;
            } else {
                return attribute % 1 == 0 ? "" + ((int) attribute) : "" + attribute;
            }
        } else {
            return "";
        }
    }

    private void addTooltips(ItemTooltipEvent event) {
        if(!Objects.requireNonNull(event.getPlayer()).level.isClientSide()) return;
        float warmth = 0;
        float insulation = 0;
        if(event.getItemStack().getItem() instanceof ArmorItem armorItem) {
            if(armorItem.getMaterial() instanceof TemperatureAlteringMaterial tempMaterial) {
                TempModifier modifier = tempMaterial.getTempModifier(event.getItemStack());
                warmth = (modifier.getChange());
                insulation = (modifier.getPotency() / 0.1f);

            } else if(armorItem.getMaterial() == ArmorMaterials.LEATHER) {
                warmth = 2;
                insulation = -1;
            } else {
                warmth = 2;
            }
            if(armorItem.getEquipmentSlot(event.getItemStack()) == EquipmentSlot.HEAD) {
                event.getToolTip().add(
                        Helpers.translatable(
                                "tfcambiental.tooltip.sun_protection"
                        ).withStyle(ChatFormatting.DARK_GRAY)
                );
            }
        }
        warmth = ((float) Math.floor(warmth / 0.25f)) * 0.25f;
        insulation = ((float) Math.floor(insulation / 0.25f)) * 0.25f;
        insulation = -insulation;

        if(warmth != 0) {
            event.getToolTip().add(
                Helpers.translatable(
                    "tfcambiental.tooltip.warmth",
                    formatAttribute(warmth)
                ).withStyle(ChatFormatting.BLUE)
            );
        }
        if(insulation != 0) {
            event.getToolTip().add(
                Helpers.translatable(
                    "tfcambiental.tooltip.insulation",
                    formatAttribute(insulation)
                ).withStyle(ChatFormatting.BLUE)
            );
        }

    }

//    private void clientSetup(final FMLClientSetupEvent event) {
//        event.get
//    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

    private static <T> void register(int id, Class<T> cls, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, NetworkEvent.Context> handler)
    {
        CHANNEL.registerMessage(id, cls, encoder, decoder, (packet, context) -> {
            context.get().setPacketHandled(true);
            handler.accept(packet, context.get());
        });
    }

    private static <T> void register(int id, Class<T> cls, Supplier<T> factory, BiConsumer<T, NetworkEvent.Context> handler)
    {
        CHANNEL.registerMessage(id, cls, (packet, buffer) -> {}, buffer -> factory.get(), (packet, context) -> {
            context.get().setPacketHandled(true);
            handler.accept(packet, context.get());
        });
    }
}
