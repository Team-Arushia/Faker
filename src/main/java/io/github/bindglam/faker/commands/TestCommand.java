package io.github.bindglam.faker.commands;

import io.github.bindglam.faker.Faker;
import io.github.bindglam.faker.fake.FakeServer;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import io.github.bindglam.faker.fake.entity.display.FakeItemDisplayEntity;
import io.github.bindglam.faker.fake.entity.display.FakeTextDisplayEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;

        FakeEntityServer server = Faker.getInstance().getServerManager().get(Faker.getInstance(), "test");

        if(args[0].equalsIgnoreCase("textdisplay")){
            FakeTextDisplayEntity entity = new FakeTextDisplayEntity(player.getLocation());
            entity.setText(Component.text("test"));
            entity.setOpacity((byte) 100);
            entity.setBillboard(Display.Billboard.HORIZONTAL);
            entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));

            server.add(entity);
        } else if(args[0].equalsIgnoreCase("itemdisplay")){
            FakeItemDisplayEntity entity = new FakeItemDisplayEntity(player.getLocation());
            entity.setItemStack(new ItemStack(Material.DIAMOND_SWORD));
            entity.setBillboard(Display.Billboard.VERTICAL);
            entity.setShadowRadius(1f);
            entity.setShadowStrength(1f);
            entity.setShadowRadius(0.5f);
            entity.setTransformation(new Transformation(
                    new Vector3f(),
                    new AxisAngle4f((float) Math.toRadians(45.0), 1f, 1f, 1f),
                    new Vector3f(1f, 2f, 1f),
                    new AxisAngle4f()
            ));

            entity.setRidingEntity(player);

            server.add(entity);
        }
        return false;
    }
}
