package io.github.bindglam.faker.listeners;

import io.github.bindglam.faker.Faker;
import io.github.bindglam.faker.fake.FakeServer;
import io.github.bindglam.faker.fake.entity.FakeEntity;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import io.github.bindglam.faker.fake.entity.FakeRealEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        FakeRealEntity fakeRealEntity = (FakeRealEntity) FakeEntityServer.getFakeEntity(player.getEntityId());

        if(fakeRealEntity != null) {
            FakeServer<FakeEntity> server = fakeRealEntity.getServer();

            if(server != null) {
                server.remove(fakeRealEntity);
            }
        }
    }
}
