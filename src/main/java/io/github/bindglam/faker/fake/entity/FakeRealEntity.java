package io.github.bindglam.faker.fake.entity;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FakeRealEntity extends FakeEntity {
    private final Entity bukkitEntity;

    public FakeRealEntity(Entity bukkitEntity) {
        super(bukkitEntity.getType(), bukkitEntity.getLocation());

        this.bukkitEntity = bukkitEntity;
    }

    @Override
    protected void spawn(User user) {
        update(user);
    }

    @Override
    protected void update(User user) {
        WrapperPlayServerSetPassengers passengersPacket = new WrapperPlayServerSetPassengers(bukkitEntity.getEntityId(), getPassengers().stream().mapToInt(Integer::intValue).toArray());
        user.sendPacket(passengersPacket);
    }

    @Override
    protected void remove(User user) {
    }

    public Entity getRealEntity() {
        return bukkitEntity;
    }

    @Override
    public void addPassenger(@NotNull FakeEntity entity) {
        passengers.add(entity.getEntityId());
    }

    @Override
    public void addPassenger(@NotNull Entity entity) {
        bukkitEntity.addPassenger(entity);
    }

    public List<Integer> getPassengers() {
        List<Integer> result = new ArrayList<>(passengers);
        result.addAll(bukkitEntity.getPassengers().stream().map(Entity::getEntityId).toList());
        return result;
    }

    @Override
    public int getEntityId() {
        return bukkitEntity.getEntityId();
    }
}
