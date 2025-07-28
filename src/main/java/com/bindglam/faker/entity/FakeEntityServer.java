package com.bindglam.faker.entity;

import com.bindglam.faker.FakeServer;
import org.bukkit.entity.Entity;

import java.util.*;

public final class FakeEntityServer implements FakeServer<FakeEntity> {
    private final Map<Integer, FakeEntity> entities = new LinkedHashMap<>();

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
}
