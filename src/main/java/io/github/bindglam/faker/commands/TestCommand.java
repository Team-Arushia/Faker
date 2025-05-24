package io.github.bindglam.faker.commands;

import io.github.bindglam.faker.Faker;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import io.github.bindglam.faker.fake.entity.FakeRealEntity;
import io.github.bindglam.faker.fake.entity.display.FakeItemDisplayEntity;
import io.github.bindglam.faker.fake.entity.display.FakeTextDisplayEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.Objects;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;

        FakeEntityServer server = Objects.requireNonNull(Faker.getInstance().getServerManager().get(Faker.getInstance(), "test"));

        if(args[0].equalsIgnoreCase("textdisplay")){
            FakeTextDisplayEntity entity = new FakeTextDisplayEntity(player.getLocation());
            entity.setText(Component.text("test"));
            entity.setOpacity((byte) 100);
            entity.setBillboard(Display.Billboard.HORIZONTAL);
            entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));

            Bukkit.getAsyncScheduler().runNow(Faker.getInstance(), (task) -> server.add(entity));
        } else if(args[0].equalsIgnoreCase("itemdisplay")){
            FakeItemDisplayEntity itemDisplay = new FakeItemDisplayEntity(player.getLocation());
            itemDisplay.setItemStack(new ItemStack(Material.DIAMOND_SWORD));
            itemDisplay.setBillboard(Display.Billboard.VERTICAL);
            itemDisplay.setShadowRadius(1f);
            itemDisplay.setShadowStrength(1f);
            itemDisplay.setShadowRadius(0.5f);
            itemDisplay.setTransformation(new Transformation(
                    new Vector3f(),
                    new AxisAngle4f((float) Math.toRadians(45.0), 1f, 1f, 1f),
                    new Vector3f(1f, 2f, 1f),
                    new AxisAngle4f()
            ));

            FakeTextDisplayEntity textDisplay = new FakeTextDisplayEntity(player.getLocation());
            textDisplay.setText(Component.text("오늘 내 세상이 어쩌구저쩌구...\n날 어쩌구저쩌구..."));
            textDisplay.setTransformation(new Transformation(
                    new Vector3f(),
                    new AxisAngle4f((float) Math.toRadians(45.0), 1f, 1f, 1f),
                    new Vector3f(1f, 4f, 1f),
                    new AxisAngle4f()
            ));

            server.add(itemDisplay);
            server.add(textDisplay);

            Cow cow = player.getWorld().spawn(player.getLocation(), Cow.class);
            cow.setPersistent(true);

            FakeRealEntity fakePlayer = server.toFakeRealEntity(cow);
            fakePlayer.addPassenger(itemDisplay);
            fakePlayer.addPassenger(textDisplay);
            fakePlayer.updateAll();

            /*Bukkit.getScheduler().scheduleSyncRepeatingTask(Faker.getInstance(), () -> {
                itemDisplay.setTransformation(new Transformation(
                        new Vector3f((float) (player.getLocation().getDirection().getX()*2f), (float) (player.getLocation().getDirection().getY()*2f), (float) (player.getLocation().getDirection().getZ()*2f)),
                        new AxisAngle4f(0f, 0f, 0f, 0f),
                        new Vector3f(1f, 1f, 1f),
                        new AxisAngle4f(0f, 0f, 0f, 0f)
                ));
                itemDisplay.updateAll();
            }, 0L, 1L);*/
        }
        return false;
    }
}
