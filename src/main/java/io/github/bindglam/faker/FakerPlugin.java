package io.github.bindglam.faker;

import io.github.bindglam.faker.fake.FakeServerManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FakerPlugin extends JavaPlugin {
    private static FakerPlugin instance;

    private final FakeServerManager serverManager = new FakeServerManager();

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public FakeServerManager getServerManager() {
        return serverManager;
    }

    public static @NotNull FakerPlugin getInstance() {
        return instance;
    }
}
