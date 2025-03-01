package io.github.bindglam.faker.fake.entity.display;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

public class FakeItemDisplayEntity extends FakeDisplayEntity {
    public FakeItemDisplayEntity(Location location) {
        super(EntityType.ITEM_DISPLAY, location);
    }

    public void setItemStack(ItemStack itemStack){
        metadata.add(new EntityData(23, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(itemStack)));
    }

    public void setDisplayTransform(ItemDisplay.ItemDisplayTransform transform){
        metadata.add(new EntityData(24, EntityDataTypes.BYTE, (byte) transform.ordinal()));
    }
}
