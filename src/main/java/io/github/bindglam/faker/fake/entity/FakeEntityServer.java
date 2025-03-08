package io.github.bindglam.faker.fake.entity;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import io.github.bindglam.faker.Faker;
import io.github.bindglam.faker.fake.FakeServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class FakeEntityServer implements FakeServer<FakeEntity> {
    private final Map<Integer, FakeEntity> entities = new HashMap<>();

    private static final Map<UUID, List<Integer>> PASSENGERS = new HashMap<>();

    static {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Faker.getInstance(), () -> {
            for(int i = PASSENGERS.size() - 1; i >= 0; i--){
                UUID uuid = PASSENGERS.keySet().stream().toList().get(i);
                List<Integer> entityIds = PASSENGERS.get(uuid);

                Entity entity = Bukkit.getEntity(uuid);
                if(entity == null) continue;

                List<Integer> passengers = new ArrayList<>(entityIds);
                passengers.addAll(entity.getPassengers().stream().map(Entity::getEntityId).toList());

                WrapperPlayServerSetPassengers passengersPacket = new WrapperPlayServerSetPassengers(entity.getEntityId(), passengers.stream().mapToInt(Integer::intValue).toArray());
                PacketEvents.getAPI().getProtocolManager().getUsers().forEach((user) -> user.writePacket(passengersPacket));
            }
        }, 0L, 1L);
    }

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
    public void remove(FakeEntity entity) {
        entity.removeAll();

        entities.remove(entity.entityId);
    }

    @Override
    public void dispose() {
        entities.values().forEach(FakeEntity::removeAll);
        entities.clear();
    }

    public static void rideFakeEntityOn(FakeEntity fakeEntity, @Nullable Entity entity){
        if(entity == null){
            PASSENGERS.forEach((uuid, ids) -> ids.forEach((id) -> {
                if(fakeEntity.entityId == id)
                    ids.remove(id);
            }));
        } else {
            if (!PASSENGERS.containsKey(entity.getUniqueId()))
                PASSENGERS.put(entity.getUniqueId(), new ArrayList<>());
            PASSENGERS.get(entity.getUniqueId()).add(fakeEntity.entityId);
        }
    }
}
