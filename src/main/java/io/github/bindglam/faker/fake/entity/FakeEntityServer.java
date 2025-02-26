package io.github.bindglam.faker.fake.entity;

import io.github.bindglam.faker.fake.FakeServer;
import org.bukkit.entity.Player;

import java.util.*;

public final class FakeEntityServer implements FakeServer<FakeEntity> {
    private final Map<Integer, FakeEntity> entities = new HashMap<>();
    private final List<UUID> viewers = new ArrayList<>();

    public FakeEntityServer(){
    }

    @Override
    public Collection<FakeEntity> getAll() {
        return entities.values();
    }

    @Override
    public void add(FakeEntity entity){
        entities.put(entity.getEntityId(), entity);
    }

    @Override
    public void dispose() {
    }

    @Override
    public Type getType() {
        return Type.ENTITY;
    }

    public void addViewer(Player player){
        viewers.add(player.getUniqueId());
    }

    public void removeViewer(Player player){
        viewers.remove(player.getUniqueId());
    }

    public void removeViewer(UUID uuid){
        viewers.remove(uuid);
    }

    public List<UUID> getViewers() {
        return new ArrayList<>(viewers);
    }
}
