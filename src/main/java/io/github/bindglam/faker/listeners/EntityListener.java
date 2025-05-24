package io.github.bindglam.faker.listeners;

import io.github.bindglam.faker.fake.FakeServer;
import io.github.bindglam.faker.fake.entity.FakeEntity;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import io.github.bindglam.faker.fake.entity.FakeRealEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRemoveEvent;

public class EntityListener implements Listener {
    @EventHandler
    public void onRemove(EntityRemoveEvent event) {
        Entity entity = event.getEntity();

        FakeRealEntity fakeRealEntity = (FakeRealEntity) FakeEntityServer.getFakeEntity(entity.getEntityId());

        if(fakeRealEntity != null) {
            FakeServer<FakeEntity> server = fakeRealEntity.getServer();

            if(server != null) {
                server.remove(fakeRealEntity);
            }
        }
    }
}
