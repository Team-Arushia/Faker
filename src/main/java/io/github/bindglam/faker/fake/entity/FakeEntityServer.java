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

    private static final Set<Integer> ENTITIES_REQUIRING_PASSENGERS_PACKET = new HashSet<>();

    static {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Faker.getInstance(), () -> {
            ENTITIES_REQUIRING_PASSENGERS_PACKET.forEach((id) -> {
                List<Integer> passengers = new ArrayList<>();

                Entity entity = SpigotConversionUtil.getEntityById(null, id);
                if(entity != null) {
                    passengers.addAll(entity.getPassengers().stream().map(Entity::getEntityId).toList());
                } else if(!isFakeEntity(id)) {
                    return;
                }
                passengers.addAll(getFakeEntities().stream().filter((fakeEntity) -> Objects.equals(fakeEntity.getRidingEntityId(), id)).map(FakeEntity::getEntityId).toList());

                WrapperPlayServerSetPassengers passengersPacket = new WrapperPlayServerSetPassengers(id, passengers.stream().mapToInt(Integer::intValue).toArray());
                PacketEvents.getAPI().getProtocolManager().getUsers().forEach((user) -> user.sendPacket(passengersPacket));
            });

            ENTITIES_REQUIRING_PASSENGERS_PACKET.clear();
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

    public static void addPassengersPacketQueue(Integer entityId){
        ENTITIES_REQUIRING_PASSENGERS_PACKET.add(entityId);
    }
}
