package io.github.bindglam.faker.fake.entity;


import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class FakeEntity {
    protected final EntityType type;
    protected final List<EntityData> metadata = new ArrayList<>();
    protected final int entityId = SpigotReflectionUtil.generateEntityId();

    private final FakeEntityServer server;

    public FakeEntity(org.bukkit.entity.EntityType bukkitType, FakeEntityServer server) {
        this.type = SpigotConversionUtil.fromBukkitEntityType(bukkitType);
        this.server = server;
    }

    public void spawn(org.bukkit.Location bukkitLoc, List<Player> viewers){
        Location location = SpigotConversionUtil.fromBukkitLocation(bukkitLoc);

        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(entityId, UUID.randomUUID(), type, location, 0f, 0, null);
        WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(entityId, metadata);

        for(Player player : viewers) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnPacket);
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, metadataPacket);
        }
    }

    public void update(List<Player> viewers){
        WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(entityId, metadata);

        for(Player player : viewers) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, metadataPacket);
        }
    }

    public org.bukkit.entity.EntityType getType() {
        return SpigotConversionUtil.toBukkitEntityType(type);
    }

    public List<EntityData> getMetadata() {
        return metadata;
    }

    public int getEntityId() {
        return entityId;
    }

    public FakeEntityServer getServer() {
        return server;
    }
}
