package net.CodeError.prometheus.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Sudo extends ListenerAdapter {

	public static final String NAME = "p!sudo <message>";
	public static final String DESCRIPTION = "Allows user with Administrator permissions to make the bot say things or post links.";

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {

			return;

		}

		Member executor = event.getMember(); // Define an instance of Member to get command executor.
		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		MessageChannel channel = event.getChannel(); // Get channel where command was sent.
		
		// If command text is entered, execute command.
		if (msgContent.startsWith("p!sudo")) {
			
			long commandID = msg.getIdLong();
			channel.deleteMessageById(commandID).queue();
			
			String[] args = msgContent.split(" "); // Split arguments into individual segments.
			
			// If command executor has permission, execute command.
			if (executor.hasPermission(Permission.ADMINISTRATOR)) {

				try {

					String msgArgs = ""; // Create variable to store message args.
					MessageBuilder msgBuilder = new MessageBuilder(); // Create new MessageBuilder instance to build response.
					
					// When i is less than the length of the args array, store part of args in msgArgs and append to msgBuilder,
					// then add 1 and repeat until i is equal to the length of the args array.
					for (int i = 1; i < args.length; i++) {

						msgArgs = args[i];
						msgBuilder.append(msgArgs + " ");

					}

					Message sudo = msgBuilder.build(); // Create a new message that stores the built message from msgBuilder.

					channel.sendMessage(sudo).queue(); // Send message.

				}
				
				// If args array is empty, catch exception.
				catch(Exception e) {

					channel.sendMessage(":warning: **|** *Invalid Arguments! Please specify a message to send!*").queue();

				}

			}
			
			// If executor does not have permission, send error message and return nothing.
			else if (!executor.hasPermission(Permission.ADMINISTRATOR)) {

				channel.sendMessage(":x: **|** *Sorry, you do not have permission to execute this command!* " + executor.getAsMention()).queue();

				return;

			}

		}

	}

}
