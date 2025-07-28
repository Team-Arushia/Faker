package com.bindglam.faker;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.Plugin;

public final class Faker {
    private Faker() {
    }

    public static void load(Plugin plugin) {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(plugin));
        PacketEvents.getAPI().load();
    }

    public static void enable() {
        PacketEvents.getAPI().init();
    }

    public static void disable() {
        PacketEvents.getAPI().terminate();
    }
}
