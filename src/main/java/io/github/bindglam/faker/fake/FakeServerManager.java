package io.github.bindglam.faker.fake;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FakeServerManager {
    private final Map<String, Map<String, FakeServer<?>>> servers = new HashMap<>(); // key는 plugin name

    public void register(JavaPlugin plugin, String id, FakeServer<?> server) {
        if(!servers.containsKey(plugin.getName()))
            servers.put(plugin.getName(), new HashMap<>());
        servers.get(plugin.getName()).put(id, server);
    }

    public void unregisterAll(JavaPlugin plugin){
        if(!servers.containsKey(plugin.getName()))
            return;
        servers.get(plugin.getName()).values().forEach(FakeServer::dispose);
        servers.remove(plugin.getName());
    }

    public <T extends FakeServer<?>> @Nullable T get(JavaPlugin plugin, String id) {
        return (T) servers.get(plugin.getName()).get(id);
    }

    public Map<String, Map<String, FakeServer<?>>> getServers() {
        return new HashMap<>(servers);
    }
}
