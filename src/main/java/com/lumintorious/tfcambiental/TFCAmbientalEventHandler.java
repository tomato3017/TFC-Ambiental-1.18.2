package com.lumintorious.tfcambiental;

import com.lumintorious.tfcambiental.capability.TemperatureCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class TFCAmbientalEventHandler {
    public static void init() {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addGenericListener(Entity.class, TFCAmbientalEventHandler::attachEntityCapabilities);
        bus.addListener(TFCAmbientalEventHandler::onPlayerUpdate);
    }

    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player player)
        {
            TemperatureCapability capability = new TemperatureCapability();
            capability.setPlayer(player);
            event.addCapability(TemperatureCapability.KEY, capability);
        }
    }

    public static void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        if(!event.player.level.isClientSide()) {
            event.player.getCapability(TemperatureCapability.CAPABILITY).ifPresent(TemperatureCapability::update);
        }
    }
}
