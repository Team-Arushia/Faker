package com.bindglam.faker.entity.display;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;

public class FakeTextDisplayEntity extends FakeDisplayEntity {
    private static final byte HAS_SHADOW_FLAG = 0x01; // 0b0001
    private static final byte IS_SEE_THROUGH_FLAG = 0x02; //0b0010
    private static final byte USE_DEFAULT_BACKGROUND_COLOR_FLAG = 0x04; // 0b0100
    private static final byte ALIGNMENT_FLAG = 0x08; // 0b1000

    public FakeTextDisplayEntity(Location location) {
        super(EntityType.TEXT_DISPLAY, location);
    }

    public void setText(Component component){
        setMetadata(new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, component));
    }

    public void setLineWidth(int width){
        setMetadata(new EntityData<>(24, EntityDataTypes.INT, width));
    }

    public void setBackgroundColor(Color color){
        setMetadata(new EntityData<>(25, EntityDataTypes.INT, color.asARGB()));
    }

    public void setOpacity(byte value){
        setMetadata(new EntityData<>(26, EntityDataTypes.BYTE, value));
    }

    public void setProperties(boolean hasShadow, boolean isSeeThrough, boolean useDefaultBackgroundColor, TextDisplay.TextAlignment alignment){
        byte currentFlags = 0x00;

        if(hasShadow)
            currentFlags |= HAS_SHADOW_FLAG;
        else
            currentFlags &= ~HAS_SHADOW_FLAG;

        if(isSeeThrough)
            currentFlags |= IS_SEE_THROUGH_FLAG;
        else
            currentFlags &= ~IS_SEE_THROUGH_FLAG;

        if(useDefaultBackgroundColor)
            currentFlags |= USE_DEFAULT_BACKGROUND_COLOR_FLAG;
        else
            currentFlags &= ~USE_DEFAULT_BACKGROUND_COLOR_FLAG;

        // TODO : Alignment

        setMetadata(new EntityData<>(27, EntityDataTypes.BYTE, currentFlags));
    }
}
