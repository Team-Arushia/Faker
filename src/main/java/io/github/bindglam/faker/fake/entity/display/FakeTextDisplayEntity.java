package io.github.bindglam.faker.fake.entity.display;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class FakeTextDisplayEntity extends FakeDisplayEntity {
    public FakeTextDisplayEntity(Location location) {
        super(EntityType.TEXT_DISPLAY, location);
    }

    public void setText(Component component){
        setMetadata(new EntityData(23, EntityDataTypes.ADV_COMPONENT, component));
    }

    public void setLineWidth(int width){
        setMetadata(new EntityData(24, EntityDataTypes.INT, width));
    }

    public void setBackgroundColor(Color color){
        setMetadata(new EntityData(25, EntityDataTypes.INT, color.asARGB()));
    }

    public void setOpacity(byte value){
        setMetadata(new EntityData(26, EntityDataTypes.BYTE, value));
    }
}
