package io.github.bindglam.faker.fake.entity;

import io.github.bindglam.faker.fake.FakeServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public final class FakeEntityServer implements FakeServer<FakeEntity> {
    private final Map<Integer, FakeEntity> entities = new HashMap<>();

    public FakeEntityServer(){
    }

    @Override
    public Collection<FakeEntity> getAll() {
        return entities.values();
    }

    @Override
    public void add(FakeEntity entity){
        entities.put(entity.getEntityId(), entity);

        entity.spawnAll();
    }

    @Override
    public void dispose() {
        entities.values().forEach(FakeEntity::removeAll);
        entities.clear();
    }
}
