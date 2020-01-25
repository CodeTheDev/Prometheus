package net.CodeError.prometheus.command;

import java.awt.Color;

import net.CodeError.prometheus.audio.command.Disconnect;
import net.CodeError.prometheus.audio.command.Join;
import net.CodeError.prometheus.audio.command.Pause;
import net.CodeError.prometheus.audio.command.Play;
import net.CodeError.prometheus.audio.command.Skip;
import net.CodeError.prometheus.audio.command.Stop;
import net.CodeError.prometheus.audio.command.Volume;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Help extends ListenerAdapter {
	
	public static final String NAME = "p!help";
	public static final String DESCRIPTION = "Provides a help menu that lists commands, command syntaxes and what specific commands do.";
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {
			
			return;
			
		}
		
		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		MessageChannel channel = event.getChannel(); // Get channel where command was sent.
		EmbedBuilder embed = new EmbedBuilder(); // Define new instance of EmbedBuilder for building Rich Embeds.
		
		// If command text is entered, execute command.
		if (msgContent.equals("p!help")) {
			
			long commandID = msg.getIdLong();
			channel.deleteMessageById(commandID).queue();
			
			// Create a RichEmbed with a title, color, description and fields using variables from all command classes.
			
			embed.setTitle("Prometheus Help Menu");
			embed.setColor(new Color(255, 0, 220));
			embed.setDescription("Lists all bot commands, command syntaxes, and a brief description of what functions they perform.");
			
			embed.addField("**" + Ping.NAME + "**", "*" + Ping.DESCRIPTION + "*", false);
			embed.addField("**" + Calculator.NAME + "**", "*" + Calculator.DESCRIPTION + "*", false);
			embed.addField("**" + CoinFlip.NAME + "**", "*" + CoinFlip.DESCRIPTION + "*", false);
			embed.addField("**" + DiceRoll.NAME + "**", "*" + DiceRoll.DESCRIPTION + "*", false);
			embed.addField("**" + BruhMoment.NAME + "**", "*" + BruhMoment.DESCRIPTION + "*", false);
			embed.addField("**" + Screenshare.NAME + "**", "*" + Screenshare.DESCRIPTION + "*", false);
			embed.addField("**" + Sudo.NAME + "**", "*" + Sudo.DESCRIPTION + "*", false);
			embed.addField("**" + Kick.NAME + "**", "*" + Kick.DESCRIPTION + "*", false);
			embed.addField("**" + Ban.NAME + "**", "*" + Ban.DESCRIPTION + "*", false);
			embed.addField("**" + Clear.NAME + "**", "*" + Clear.DESCRIPTION + "*", false);
			embed.addField("**" + Vote.NAME + "**", "*" + Vote.DESCRIPTION + "*", false);
			embed.addField("**" + Join.NAME + "**", "*" + Join.DESCRIPTION + "*", false);
			embed.addField("**" + Disconnect.NAME + "**", "*" + Disconnect.DESCRIPTION + "*", false);
			embed.addField("**" + Pause.NAME + "**", "*" + Pause.DESCRIPTION + "*", false);
			embed.addField("**" + Play.NAME + "**", "*" + Play.DESCRIPTION + "*", false);
			embed.addField("**" + Skip.NAME + "**", "*" + Skip.DESCRIPTION + "*", false);
			embed.addField("**" + Stop.NAME + "**", "*" + Stop.DESCRIPTION + "*", false);
			embed.addField("**" + Volume.NAME + "**", "*" + Volume.DESCRIPTION + "*", false);
			embed.addField("**" + Help.NAME + "**", "*" + Help.DESCRIPTION + "*", false);
			embed.addBlankField(false);
			
			embed.setAuthor("Prometheus", null, "https://www.codeslab.ca/prometheus/assets/hawk.png");
			
			embed.setFooter("Copyright © 2019-2020 CodeError", "https://www.codeslab.ca/prometheus/assets/missing_texture.png");
			
			embed.setThumbnail("https://www.codeslab.ca/prometheus/assets/hawk.png");
			
			channel.sendMessage(embed.build()).queue(); // Build RichEmbed and send.
			
		}
		
	}
	
}
