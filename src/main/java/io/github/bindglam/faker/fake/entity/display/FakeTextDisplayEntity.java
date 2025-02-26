package io.github.bindglam.faker.fake.entity.display;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Color;
import org.bukkit.entity.EntityType;

public class FakeTextDisplayEntity extends FakeDisplayEntity {
    public FakeTextDisplayEntity(FakeEntityServer server) {
        super(EntityType.TEXT_DISPLAY, server);
    }

    public void setText(Component component){
        metadata.add(new EntityData(23, EntityDataTypes.ADV_COMPONENT, component));
    }

    public void setLineWidth(int width){
        metadata.add(new EntityData(24, EntityDataTypes.INT, width));
    }

    public void setBackgroundColor(Color color){
        throw new NotImplementedException();
    }

    public void setOpacity(byte value){
        metadata.add(new EntityData(26, EntityDataTypes.BYTE, value));
    }
}
