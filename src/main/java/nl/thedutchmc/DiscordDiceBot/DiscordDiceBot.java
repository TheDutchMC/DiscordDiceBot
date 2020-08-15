package nl.thedutchmc.DiscordDiceBot;

import org.bukkit.plugin.java.JavaPlugin;

public class DiscordDiceBot extends JavaPlugin {

	public static DiscordDiceBot INSTANCE;
	
	public static final ConfigurationHandler CONFIG = new ConfigurationHandler();
	public static final JdaHandler JDA = new JdaHandler();
		
	@Override
	public void onEnable() {
		INSTANCE = this;
		
		logInfo("Welcome to DiscordDiceBot version " + this.getDescription().getVersion() + " by TheDutchMC");
				
		logInfo("Loading config...");
		CONFIG.loadConfig();
		
		logInfo("Connecting to Discord...");
		JDA.setupJda();
		
		logInfo("Startup complete!");
		
		JDA.diceChannel.sendMessage("**Bot online**").queue();
	}
	
	@Override
	public void onDisable() {
		JDA.diceChannel.sendMessage("**Bot offline**").queue();
		
		final int WAIT = 3;
		int waitLeft = WAIT;
		
		logInfo("Shutting down JDA in " + WAIT + " seconds.");
		
		while(waitLeft > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			waitLeft--;
		}
		
		logInfo("Shutting down JDA");
		
		try {
			JDA.shutdownJda();
		} catch(Exception e) {}
		
		logInfo("Thank you for using DiscordDiceBot by TheDutchMC");
	}
	
	public static void logInfo(String log) {
		INSTANCE.getLogger().info(log);
	}
	
	public static void logWarn(String log) {
		INSTANCE.getLogger().warning(log);
	}
}
