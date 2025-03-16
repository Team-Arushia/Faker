package io.github.bindglam.faker.fake.entity;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import io.github.bindglam.faker.Faker;
import io.github.bindglam.faker.fake.FakeServer;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class FakeEntityServer implements FakeServer<FakeEntity> {
    private final Map<Integer, FakeEntity> entities = new HashMap<>();

    private static final Map<Integer, List<Integer>> PASSENGERS = new HashMap<>();

    static {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Faker.getInstance(), () -> {
            List<Integer> keyList = PASSENGERS.keySet().stream().toList();
            for(int i = PASSENGERS.size() - 1; i >= 0; i--){
                Integer entityId = keyList.get(i);
                List<Integer> entityIds = PASSENGERS.get(entityId);

                if(entityIds.isEmpty()){
                    PASSENGERS.remove(entityId);
                    continue;
                }

                List<Integer> passengers = new ArrayList<>(entityIds);

                Entity entity = SpigotConversionUtil.getEntityById(null, entityId);
                if(entity != null && entity.isValid()) {
                    passengers.addAll(entity.getPassengers().stream().map(Entity::getEntityId).toList());
                } else if (!isFakeEntity(entityId)) {
                    PASSENGERS.remove(entityId);
                    continue;
                }

                WrapperPlayServerSetPassengers passengersPacket = new WrapperPlayServerSetPassengers(entityId, passengers.stream().mapToInt(Integer::intValue).toArray());
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

        PASSENGERS.values().forEach((entityIds) -> entityIds.removeIf((id) -> entity.entityId == id));
    }

    @Override
    public void dispose() {
        entities.values().forEach(FakeEntity::removeAll);
        entities.clear();
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

    public static void rideFakeEntityOn(FakeEntity fakeEntity, @Nullable Entity entity){
        if(entity == null){
            PASSENGERS.forEach((uuid, ids) -> ids.forEach((id) -> {
                if(fakeEntity.entityId == id)
                    ids.remove(id);
            }));
        } else {
            if (!PASSENGERS.containsKey(entity.getEntityId()))
                PASSENGERS.put(entity.getEntityId(), new ArrayList<>());
            PASSENGERS.get(entity.getEntityId()).add(fakeEntity.entityId);
        }
    }

    public static void rideFakeEntityOn(FakeEntity fakeEntity, @Nullable FakeEntity entity){
        if(entity == null){
            PASSENGERS.forEach((uuid, ids) -> ids.forEach((id) -> {
                if(fakeEntity.entityId == id)
                    ids.remove(id);
            }));
        } else {
            if (!PASSENGERS.containsKey(entity.getEntityId()))
                PASSENGERS.put(entity.getEntityId(), new ArrayList<>());
            PASSENGERS.get(entity.getEntityId()).add(fakeEntity.entityId);
        }
    }
}
