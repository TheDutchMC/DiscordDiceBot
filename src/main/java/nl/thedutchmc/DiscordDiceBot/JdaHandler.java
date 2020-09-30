package nl.thedutchmc.DiscordDiceBot;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import nl.thedutchmc.DiscordDiceBot.discordEvents.MessageReceivedEventListener;

public class JdaHandler {

	private JDA jda;
	
	public TextChannel diceChannel;
	
	public void setupJda() {
				
		List<GatewayIntent> intents = new ArrayList<>();
		intents.add(GatewayIntent.GUILD_MESSAGES);
		
		try {
			jda = JDABuilder.createDefault(DiscordDiceBot.CONFIG.discordToken)
					.setActivity(Activity.playing("Rolling the dices"))
					.enableIntents(intents)
					.build();
			
			jda.awaitReady();
			
		} catch (LoginException e) {
			DiscordDiceBot.logWarn("There was an error logging in to Discord. Is your token correct?");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			
		}
		
		jda.addEventListener(new MessageReceivedEventListener());
		
		diceChannel = jda.getTextChannelById(DiscordDiceBot.CONFIG.discordChannel);
		
		if(diceChannel == null) {
			DiscordDiceBot.logWarn("The given Discord channel is invalid!");
			return;
		}
	}
	
	public void shutdownJda() throws Exception {
		jda.shutdownNow();
	}
}
