package io.github.bindglam.faker;

import io.github.bindglam.faker.commands.TestCommand;
import io.github.bindglam.faker.fake.FakeServerManager;
import io.github.bindglam.faker.fake.entity.FakeEntityServer;
import io.github.bindglam.faker.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Faker extends JavaPlugin {
    private static Faker instance;

    private final FakeServerManager serverManager = new FakeServerManager();

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getCommand("testfake").setExecutor(new TestCommand());

        serverManager.register(this, "test", new FakeEntityServer());
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public FakeServerManager getServerManager() {
        return serverManager;
    }

    public static @NotNull Faker getInstance() {
        return instance;
    }
}
