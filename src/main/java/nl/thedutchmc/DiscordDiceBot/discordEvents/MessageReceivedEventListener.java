package nl.thedutchmc.DiscordDiceBot.discordEvents;

import java.util.concurrent.ThreadLocalRandom;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.thedutchmc.DiscordDiceBot.DiscordDiceBot;

public class MessageReceivedEventListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(!event.getChannel().equals(DiscordDiceBot.JDA.diceChannel)) return;
		
		String message = event.getMessage().getContentDisplay();
		
		if(!message.startsWith(DiscordDiceBot.CONFIG.prefix)) return;
		
		message = message.replace(DiscordDiceBot.CONFIG.prefix, "").toLowerCase();
				
		//Role the dice
		if(message.startsWith("dice")) {
			String[] parts = message.split(" ");
			
			int n = 0;
			if(parts.length == 1) {
				n = ThreadLocalRandom.current().nextInt(1, DiscordDiceBot.CONFIG.diceSize + 1);
			} else {
				if(isInt(parts[1])) {
					if(Integer.valueOf(parts[1]) > 0) {
						n = ThreadLocalRandom.current().nextInt(1, Integer.valueOf(parts[1]) + 1);
					} else {
						event.getChannel().sendMessage("Dice size may not be negative or zero!").queue();
					}
				} else {
					event.getChannel().sendMessage("The given value needs to be an Integer!").queue();
				}
			}

			event.getChannel().sendMessage("The dice has fallen on " + n).queue();
		}
		
		//set default dice size
		if(message.startsWith("setdice")) {
			String[] parts = message.split(" ");
			if(parts.length < 2) {
				event.getChannel().sendMessage("You need to specify a dice size!").queue();
				return;
			}
			
			if(!isInt(parts[1])) {
				event.getChannel().sendMessage("The given value needs to be an Integer!").queue();
				return;
			}
			
			int diceSize = Integer.valueOf(parts[1]);
			
			if(diceSize <= 0) {
				event.getChannel().sendMessage("Dice size may not be negative or zero!").queue();
				return;
			}
			
			DiscordDiceBot.CONFIG.diceSize = diceSize;
			DiscordDiceBot.CONFIG.updateConfig();
			
			event.getChannel().sendMessage("Set dice size to " + DiscordDiceBot.CONFIG.diceSize).queue();
		}
		
		// help
		if(message.startsWith("help")) {
			final String p = DiscordDiceBot.CONFIG.prefix;
			event.getChannel().sendMessage(">>> Dice Bot Help\n"
					+ "\n"
					+ p + "help: This page\n"
					+ p + "dice [size]: Roll a dice with the default size, or provide a size directly\n"
					+ p + "setdice <size>: Set the default dice size").queue();
		}
	}
	
	private boolean isInt(String str) {
		return str.matches("-?\\d+");
	}
}
