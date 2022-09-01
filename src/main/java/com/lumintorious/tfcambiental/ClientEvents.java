package com.lumintorious.tfcambiental;

import com.lumintorious.tfcambiental.curios.ClothesCurioRendeder;
import com.lumintorious.tfcambiental.item.TFCAmbientalItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class ClientEvents {
    public static void init(IEventBus bus) {
        bus.addListener(ClientEvents::setup);
    }

    private static void setup(FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(TFCAmbientalItems.STRAW_HAT.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.LEATHER_APRON.get(), ClothesCurioRendeder::new);

        CuriosRendererRegistry.register(TFCAmbientalItems.WOOL_HAT.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.WOOL_SWEATER.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.WOOL_PANTS.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.WOOL_BOOTS.get(), ClothesCurioRendeder::new);

        CuriosRendererRegistry.register(TFCAmbientalItems.SILK_COWL.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.SILK_SHIRT.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.SILK_PANTS.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.SILK_SHOES.get(), ClothesCurioRendeder::new);

        CuriosRendererRegistry.register(TFCAmbientalItems.BURLAP_COWL.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.BURLAP_SHIRT.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.BURLAP_PANTS.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.BURLAP_SHOES.get(), ClothesCurioRendeder::new);

        CuriosRendererRegistry.register(TFCAmbientalItems.LEATHER_HAT.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.LEATHER_TUNIC.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.LEATHER_PANTS.get(), ClothesCurioRendeder::new);
        CuriosRendererRegistry.register(TFCAmbientalItems.LEATHER_BOOTS.get(), ClothesCurioRendeder::new);
    }
}
