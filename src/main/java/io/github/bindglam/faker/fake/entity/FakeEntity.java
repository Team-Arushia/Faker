package io.github.bindglam.faker.fake.entity;


import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.npc.NPC;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
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
    protected final Location location;
    protected final EntityType type;
    protected final List<EntityData> metadata = new ArrayList<>();
    protected final int entityId = SpigotReflectionUtil.generateEntityId();
    protected final List<UUID> blacklist = new ArrayList<>();

    public FakeEntity(org.bukkit.entity.EntityType bukkitType, org.bukkit.Location bukkitLoc) {
        this.type = SpigotConversionUtil.fromBukkitEntityType(bukkitType);
        this.location = SpigotConversionUtil.fromBukkitLocation(bukkitLoc);
    }

    public void spawn(Player player){
        if(blacklist.stream().map(UUID::toString).toString().contains(player.getUniqueId().toString()))
            return;

        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(entityId, UUID.randomUUID(), type, location, 0f, 0, null);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnPacket);

        update(player);
    }

    public void update(Player player){
        WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(entityId, metadata);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, metadataPacket);
    }

    public void remove(Player player){
        WrapperPlayServerDestroyEntities removePacket = new WrapperPlayServerDestroyEntities(entityId);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, removePacket);
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
}
