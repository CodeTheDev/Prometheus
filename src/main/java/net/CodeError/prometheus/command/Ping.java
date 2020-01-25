package net.CodeError.prometheus.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ping extends ListenerAdapter {
	
	public static final String NAME = "p!ping";
	public static final String DESCRIPTION = "Outputs realtime ping statistics of bot to Discord API.";
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {

			return;

		}

		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		MessageChannel channel = event.getChannel(); // Get channel where command was sent.
		JDA prometheus = event.getJDA(); // Get JDA bot instance.

		// If command text is entered, execute command.
		if (msgContent.equals("p!ping")) {
			
			long commandID = msg.getIdLong();
			channel.deleteMessageById(commandID).queue();
			
			channel.sendMessage(":left_right_arrow: **|** " + event.getAuthor().getAsMention() + " *Time to Discord API:* " + " **" + prometheus.getGatewayPing() + "ms**").queue(); // Mention author and print appropriate response.

		}

	}

}
