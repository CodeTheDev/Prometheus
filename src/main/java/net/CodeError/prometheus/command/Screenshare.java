package net.CodeError.prometheus.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Screenshare extends ListenerAdapter {
	
	public static final String NAME = "p!screenshare";
	public static final String DESCRIPTION = "Generates a screenshare link for the voice channel you're currently in.";
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {

			return;

		}

		Guild guild = event.getGuild(); // Define Guild variable to get current server.
		Member executor = event.getMember(); // Define an instance of Member to get command executor.
		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		TextChannel channel = event.getChannel(); // Get current channel command is executed in.
		
		// If command text is entered, execute command.
		if (msgContent.startsWith("p!screenshare")) {
			
			long commandID = msg.getIdLong(); // Get ID of command sent.
			channel.deleteMessageById(commandID).queue(); // Delete command message.
			
			// If executor is not in a voice channel, send error message and return nothing.
			if (executor.getVoiceState().inVoiceChannel() == false) {
				
				channel.sendMessageFormat(":x: **|** You need to be in **%s** for me to generate a link for **%s**! %s", executor.getVoiceState().getChannel().getName(), executor.getVoiceState().getChannel().getName(), executor.getAsMention()).queue();
				return;
				
			}
			
			String guildID = guild.getId(); // Get Guild ID type String.
			String channelID = executor.getVoiceState().getChannel().getId(); // Get Channel ID type String.
			
			channel.sendMessageFormat("%s Here is your Screenshare Link for **%s** | https://www.discordapp.com/channels/%s/%s", executor.getAsMention(), executor.getVoiceState().getChannel().getName(), guildID, channelID).queue(); // Send message containing link.
			
		}
		
	}

}
