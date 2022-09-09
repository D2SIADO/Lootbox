package me.d2siado.dev.Utils.files;

import java.io.File;

import me.d2siado.dev.LootBox;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class DataFile extends YamlConfiguration
{
    private static DataFile config;
    private final Plugin plugin;
    private final File Config;
    private Plugin main() {
        return LootBox.getInstance();
    }
    public void save() {
        try {
            super.save(this.Config);
        } catch (Exception ignored) {}
    }
    public void saveDefault() {
        this.plugin.saveResource("data.yml", false);
    }
    public DataFile() {
        this.plugin = this.main();
        this.Config = new File(this.plugin.getDataFolder(), "data.yml");
        this.saveDefault();
        this.reload();
    }
    public void saveAll() {
        this.save();
        this.reload();
    }
    public static DataFile getConfig() {
        if (config == null) {
            config = new DataFile();
        }
        return config;
    }
    public void reload() {
        try {
            super.load(this.Config);
        } catch (Exception ignored) {}
    }
}