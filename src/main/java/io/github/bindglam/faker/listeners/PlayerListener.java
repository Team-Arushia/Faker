package io.github.bindglam.faker.listeners;

import io.github.bindglam.faker.Faker;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        Faker.getInstance().getServerManager().getServers().values().forEach((map) -> map.values().forEach((server) -> {
            if(server instanceof FakeEntityServer entityServer) {
                entityServer.getAll().forEach((entity) -> entity.spawn(player));
            }
        }));
    }
}
