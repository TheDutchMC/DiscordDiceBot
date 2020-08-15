package nl.thedutchmc.DiscordDiceBot;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationHandler {

	public String discordToken, discordChannel, prefix;
	public int diceSize;
	
	private File file;
	private FileConfiguration config;
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public void loadConfig() {
		file = new File(DiscordDiceBot.INSTANCE.getDataFolder(), "config.yml");
		
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			DiscordDiceBot.INSTANCE.saveResource("config.yml", false);
		}
		
		config = new YamlConfiguration();
		
		try {
			config.load(file);
			readConfig();
		} catch (InvalidConfigurationException e) {
			DiscordDiceBot.logWarn("Invalid config.yml!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readConfig() {
		this.discordToken = this.getConfig().getString("discordToken");
		this.discordChannel = this.getConfig().getString("discordChannel");
		this.prefix = this.getConfig().getString("prefix");
		this.diceSize = this.getConfig().getInt("diceSize");
	}
	
	public void updateConfig() {
		this.getConfig().set("diceSize", diceSize);
		
		try {
			this.getConfig().save(file);
		} catch (IOException e) {
			DiscordDiceBot.logWarn("There was an issue saving the config!");
			e.printStackTrace();
		}
	}
}
