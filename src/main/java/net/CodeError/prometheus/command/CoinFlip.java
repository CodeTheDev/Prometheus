package net.CodeError.prometheus.command;

import java.util.Random;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CoinFlip extends ListenerAdapter {
	
	public static final String NAME = "p!coinflip";
	public static final String DESCRIPTION = "Flips a coin. Heads or Tails.";
	
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
		if (msgContent.equals("p!coinflip")) {
			
			long commandID = msg.getIdLong();
			channel.deleteMessageById(commandID).queue();
			
			int flip = random.nextInt(2); // Generate random number between 0 and 1.
			
			// If flip equals 0, heads,
			if (flip == 0) {
				
				channel.sendMessage(":dvd: **|** " + event.getAuthor().getAsMention() + " *flipped* " + " **heads**").queue();
				
			}
			
			// If flip equals 1, tails.
			else if (flip == 1) {
				
				channel.sendMessage(":dvd: **|** " + event.getAuthor().getAsMention() + " *flipped* " + " **tails**").queue();
				
			}
			
		}
		
	}
	
}
