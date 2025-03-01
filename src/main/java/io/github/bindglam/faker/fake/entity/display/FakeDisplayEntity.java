package io.github.bindglam.faker.fake.entity.display;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3f;
import io.github.bindglam.faker.fake.entity.FakeEntity;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;

public abstract class FakeDisplayEntity extends FakeEntity {
    public FakeDisplayEntity(EntityType bukkitType, Location location) {
        super(bukkitType, location);
    }

    public void setTransformation(Transformation transformation){
        metadata.add(new EntityData(11, EntityDataTypes.VECTOR3F, new Vector3f(transformation.getTranslation().x, transformation.getTranslation().y, transformation.getTranslation().z)));
        metadata.add(new EntityData(12, EntityDataTypes.VECTOR3F, new Vector3f(transformation.getScale().x, transformation.getScale().y, transformation.getScale().z)));
        metadata.add(new EntityData(13, EntityDataTypes.QUATERNION, new Quaternion4f(transformation.getRightRotation().x, transformation.getRightRotation().y, transformation.getRightRotation().z, transformation.getRightRotation().w)));
        metadata.add(new EntityData(14, EntityDataTypes.QUATERNION, new Quaternion4f(transformation.getLeftRotation().x, transformation.getLeftRotation().y, transformation.getLeftRotation().z, transformation.getLeftRotation().w)));
    }

    public void setBillboard(Display.Billboard type){
        metadata.add(new EntityData(15, EntityDataTypes.BYTE, (byte) type.ordinal()));
    }

    public void setViewRange(float range){
        metadata.add(new EntityData(17, EntityDataTypes.FLOAT, range));
    }

    public void setShadowRadius(float radius){
        metadata.add(new EntityData(18, EntityDataTypes.FLOAT, radius));
    }

    public void setShadowStrength(float strength){
        metadata.add(new EntityData(19, EntityDataTypes.FLOAT, strength));
    }

    public void setInterpolationDelay(int delay){
        metadata.add(new EntityData(8, EntityDataTypes.INT, delay));
    }

    public void setTransformationInterpolationDuration(int duration){
        metadata.add(new EntityData(9, EntityDataTypes.INT, duration));
    }

    public void setTeleportInterpolationDuration(int duration){
        metadata.add(new EntityData(10, EntityDataTypes.INT, duration));
    }
}
