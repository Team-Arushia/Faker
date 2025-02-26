package io.github.bindglam.faker.fake;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FakeServerManager {
    private final Map<String, Map<FakeServer.Type, FakeServer<?>>> servers = new HashMap<>(); // key는 plugin name

    public void register(JavaPlugin plugin, FakeServer<?> server) {
        if(!servers.containsKey(plugin.getName()))
            servers.put(plugin.getName(), new EnumMap<>(FakeServer.Type.class));
        servers.get(plugin.getName()).put(server.getType(), server);
    }

    public void unregisterAll(JavaPlugin plugin){
        if(!servers.containsKey(plugin.getName()))
            return;
        for(FakeServer<?> server : servers.get(plugin.getName()).values())
            server.dispose();
        servers.remove(plugin.getName());
    }

    public void get(JavaPlugin plugin, FakeServer.Type type) {
        servers.get(plugin.getName()).put(server.getType(), server);
    }
}
