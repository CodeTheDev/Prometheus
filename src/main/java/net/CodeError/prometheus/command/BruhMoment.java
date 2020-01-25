package net.CodeError.prometheus.command;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BruhMoment extends ListenerAdapter {

	public static final String NAME = "p!bruh";
	public static final String DESCRIPTION = "For use when a Bruh Moment is observed.";

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {

			return;

		}

		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		MessageChannel channel = event.getChannel(); // Get channel where command was sent.
		EmbedBuilder eb = new EmbedBuilder(); // Create new instance of EmbedBuilder to create new Rich Embed.

		// If command text is entered, execute command.
		if (msgContent.equals("p!bruh")) {

			long commandID = msg.getIdLong(); // Get ID of command sent.
			channel.deleteMessageById(commandID).queue(); // Delete command message.

			eb.setColor(new Color(255, 0, 220)); // Set color of embed to purple-pink.
			eb.setImage("https://www.codeslab.ca/prometheus/assets/certified_bruh_moment.jpg"); // Embed image.
			
			channel.sendMessage(":warning: ***A BRUH MOMENT™️ HAS BEEN DETECTED*** :warning:").queue(); // Send message.
			channel.sendMessage(eb.build()).queue(); // Send embed.

		}

	}

}
