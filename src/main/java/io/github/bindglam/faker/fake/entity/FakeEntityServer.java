package io.github.bindglam.faker.fake.entity;

import io.github.bindglam.faker.Faker;
import io.github.bindglam.faker.fake.FakeServer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class FakeEntityServer implements FakeServer<FakeEntity> {
    private final Map<Integer, FakeEntity> entities = new LinkedHashMap<>();

    public FakeEntityServer(){
    }

    @Override
    public Collection<FakeEntity> getAll() {
        return entities.values();
    }

    @Override
    public void add(FakeEntity entity){
        entity.setServer(this);

        entities.put(entity.getEntityId(), entity);

        entity.spawnAll();
    }

    @Override
    public void remove(FakeEntity entity) {
        entity.removeAll();

        entities.remove(entity.entityId);
    }

    @Override
    public void dispose() {
        entities.values().forEach(FakeEntity::removeAll);
        entities.clear();
    }

    public FakeRealEntity toFakeRealEntity(Entity bukkitEntity) {
        for (FakeEntity fakeEntity : getAll()) {
            if(fakeEntity instanceof FakeRealEntity fakeRealEntity && fakeRealEntity.getRealEntity().getUniqueId().equals(bukkitEntity.getUniqueId())) {
                return fakeRealEntity;
            }
        }
        FakeRealEntity fakeRealEntity = new FakeRealEntity(bukkitEntity);
        add(fakeRealEntity);
        return fakeRealEntity;
    }

    public static boolean isFakeEntity(int entityId) {
        for (Map<String, FakeServer<?>> map : Faker.getInstance().getServerManager().getServers().values()) {
            for (FakeServer<?> server : map.values()) {
                if(server instanceof FakeEntityServer entityServer) {
                    if(entityServer.entities.containsKey(entityId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static @Nullable FakeEntity getFakeEntity(int entityId) {
        for (Map<String, FakeServer<?>> map : Faker.getInstance().getServerManager().getServers().values()) {
            for (FakeServer<?> server : map.values()) {
                if(server instanceof FakeEntityServer entityServer) {
                    if(entityServer.entities.containsKey(entityId)) {
                        return entityServer.entities.get(entityId);
                    }
                }
            }
        }
        return null;
    }

    public static Collection<FakeEntity> getFakeEntities() {
        List<FakeEntity> fakeEntities = new ArrayList<>();

        Faker.getInstance().getServerManager().getServers().values().forEach((map) -> map.values().forEach((server) -> {
            if(!(server instanceof FakeEntityServer entityServer)) return;

            fakeEntities.addAll(entityServer.entities.values());
        }));

        return fakeEntities;
    }
}
