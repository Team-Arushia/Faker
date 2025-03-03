package io.github.bindglam.faker.fake.entity;


import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.npc.NPC;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.bindglam.faker.Faker;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class FakeEntity {
    protected Location location;
    protected World world;
    protected final EntityType type;
    protected final Map<Integer, EntityData> metadata = new ConcurrentHashMap<>();
    protected final int entityId = SpigotReflectionUtil.generateEntityId();
    protected final List<UUID> blacklist = new ArrayList<>();

    public FakeEntity(org.bukkit.entity.EntityType bukkitType, org.bukkit.Location bukkitLoc) {
        this.type = SpigotConversionUtil.fromBukkitEntityType(bukkitType);
        this.location = SpigotConversionUtil.fromBukkitLocation(bukkitLoc);
        this.world = bukkitLoc.getWorld();
    }

    public void spawn(Player player){
        if(!player.getWorld().getName().equals(world.getName())) return;
        if(blacklist.stream().map(UUID::toString).toString().contains(player.getUniqueId().toString()))
            return;

        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(entityId, UUID.randomUUID(), type, location, 0f, 0, null);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnPacket);

        update(player);
    }

    public void spawnAll(){
        Bukkit.getOnlinePlayers().forEach(this::spawn);
    }

    public void update(Player player){
        if(!player.getWorld().getName().equals(world.getName())) return;
        if(blacklist.stream().map(UUID::toString).toString().contains(player.getUniqueId().toString()))
            return;

        WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(entityId, metadata.values().stream().toList());

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, metadataPacket);

        WrapperPlayServerEntityTeleport teleportPacket = new WrapperPlayServerEntityTeleport(entityId, location, false);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, teleportPacket);
    }

    public void updateAll(){
        Bukkit.getScheduler().runTask(Faker.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(this::update));
    }

    public void remove(Player player){
        if(!player.getWorld().getName().equals(world.getName())) return;
        if(blacklist.stream().map(UUID::toString).toString().contains(player.getUniqueId().toString()))
            return;

        WrapperPlayServerDestroyEntities removePacket = new WrapperPlayServerDestroyEntities(entityId);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, removePacket);
    }

    public void removeAll(){
        Bukkit.getScheduler().runTask(Faker.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(this::remove));
    }

    public org.bukkit.Location getLocation() {
        return SpigotConversionUtil.toBukkitLocation(world, location);
    }

    public void setLocation(org.bukkit.Location location) {
        this.location = SpigotConversionUtil.fromBukkitLocation(location);
        this.world = location.getWorld();

        updateAll();
    }

    @ApiStatus.Experimental
    public void ride(@Nullable Entity entity) {
        FakeEntityServer.rideFakeEntityOn(this, entity);
    }

    public org.bukkit.entity.EntityType getType() {
        return SpigotConversionUtil.toBukkitEntityType(type);
    }

    public Collection<EntityData> getMetadata() {
        return metadata.values();
    }

    public int getEntityId() {
        return entityId;
    }
}
