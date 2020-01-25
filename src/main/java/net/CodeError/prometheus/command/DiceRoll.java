package net.CodeError.prometheus.command;

import java.util.Random;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiceRoll extends ListenerAdapter {
	
	public static final String NAME = "p!dice";
	public static final String DESCRIPTION = "Rolls a 6-sided dice.";
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {
			
			return;
			
		}
		
		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		MessageChannel channel = event.getChannel(); // Get channel where command was sent.
		Random random = new Random(); // Define random variable as instance of Random class.
		
		// If command text is entered, execute command.
		if (msgContent.equals("p!dice")) {
			
			long commandID = msg.getIdLong();
			channel.deleteMessageById(commandID).queue();
			
			int num = random.nextInt((6 - 1) + 1) + 1; // Generate a random number between 1 and 6.
			
			channel.sendMessage(":game_die: **|** *You rolled a:* **" + " " + num + "**").queue(); // Send message with randomly generated number.
			
		}
		
	}

}
