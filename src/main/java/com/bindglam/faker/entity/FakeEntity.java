package com.bindglam.faker.entity;


import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class FakeEntity {
    protected Location location, lastLocation;
    protected World world;
    protected UUID uuid = UUID.randomUUID();
    protected int entityId = SpigotReflectionUtil.generateEntityId();
    protected final EntityType type;
    protected final List<EntityData<?>> metadata = new ArrayList<>();
    protected final List<UUID> blacklist = new ArrayList<>();
    protected final List<Integer> passengers = new ArrayList<>();

    public FakeEntity(org.bukkit.entity.EntityType bukkitType, org.bukkit.Location bukkitLoc) {
        this.type = SpigotConversionUtil.fromBukkitEntityType(bukkitType);
        this.location = SpigotConversionUtil.fromBukkitLocation(bukkitLoc);
        this.lastLocation = location;
        this.world = bukkitLoc.getWorld();
    }

    protected void spawn(User user) {
        if(user.getUUID() == null)
            return;
        if(blacklist.stream().map(UUID::toString).toString().contains(user.getUUID().toString()))
            return;

        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(entityId, uuid, type, location, 0f, 0, null);
        user.sendPacket(spawnPacket);

        update(user);
    }

    public void spawn(Player player){
        if(!player.getWorld().getName().equals(world.getName())) return;

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if(user == null)
            return;

        spawn(user);
    }

    public void spawnAll(){
        PacketEvents.getAPI().getProtocolManager().getUsers().forEach(this::spawn);
    }

    protected void update(User user){
        if(user.getUUID() == null)
            return;
        if(blacklist.stream().map(UUID::toString).toString().contains(user.getUUID().toString()))
            return;

        if(!metadata.isEmpty()) {
            WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(entityId, new ArrayList<>(metadata));
            user.sendPacket(metadataPacket);
            metadata.clear();
        }

        if(location != lastLocation) {
            WrapperPlayServerEntityTeleport teleportPacket = new WrapperPlayServerEntityTeleport(entityId, location, false);
            user.sendPacket(teleportPacket);
            lastLocation = location;
        }

        if(!passengers.isEmpty()) {
            WrapperPlayServerSetPassengers passengersPacket = new WrapperPlayServerSetPassengers(entityId, passengers.stream().mapToInt(Integer::intValue).toArray());
            user.sendPacket(passengersPacket);
        }
    }

    public void update(Player player){
        if(!player.getWorld().getName().equals(world.getName())) {
            remove(player);
            return;
        }

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if(user == null)
            return;

        update(user);
    }

    public void updateAll(){
        PacketEvents.getAPI().getProtocolManager().getUsers().forEach(this::update);
    }

    protected void remove(User user){
        if(user.getUUID() == null)
            return;
        if(blacklist.stream().map(UUID::toString).toString().contains(user.getUUID().toString()))
            return;

        WrapperPlayServerDestroyEntities removePacket = new WrapperPlayServerDestroyEntities(entityId);

        user.sendPacket(removePacket);
    }

    public void remove(Player player){
        if(!player.getWorld().getName().equals(world.getName())) return;

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if(user == null)
            return;

        remove(user);
    }

    public void removeAll(){
        PacketEvents.getAPI().getProtocolManager().getUsers().forEach(this::remove);
    }

    public org.bukkit.Location getLocation() {
        return SpigotConversionUtil.toBukkitLocation(world, location);
    }

    public void setLocation(org.bukkit.Location location) {
        this.location = SpigotConversionUtil.fromBukkitLocation(location);
        this.world = location.getWorld();

        updateAll();
    }

    public void addPassenger(@NotNull FakeEntity entity) {
        passengers.add(entity.getEntityId());
    }

    public void addPassenger(@NotNull Entity entity) {
        passengers.add(entity.getEntityId());
    }

    public void clearPassengers() {
        passengers.clear();
    }

    public org.bukkit.entity.EntityType getType() {
        return SpigotConversionUtil.toBukkitEntityType(type);
    }

    public Collection<EntityData<?>> getMetadata() {
        return metadata;
    }

    public void setMetadata(EntityData<?> data) {
        metadata.add(data);
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getUniqueId() {
        return uuid;
    }
}
