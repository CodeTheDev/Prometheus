package net.CodeError.prometheus.audio.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class Join extends ListenerAdapter {

	public static final String NAME = "p!join";
	public static final String DESCRIPTION = "Joins the bot to any voice channel where the bot has connect and speak permissions.";
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {

			return;

		}

		Member executor = event.getMember(); // Define an instance of Member to get command executor.
		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		TextChannel channel = event.getChannel(); // Get current channel command is executed in.
		AudioManager audio = event.getGuild().getAudioManager(); // Create new instance of AudioManager.

		// If command text is entered, execute command.
		if (msgContent.equalsIgnoreCase("p!join")) {
			
			Role dj = null; // Create new Role and store empty (null) value to initialize.

			// Filter through executor's roles and see if executor possesses a role with the name "DJ". If executor possesses
			// this role, store Role in Role variable and break for loop. If executor does not possess this role, keep value set to empty (null).
			for (int i = 0; i < executor.getRoles().size(); i++) {

				if (executor.getRoles().get(i).getName().equalsIgnoreCase("DJ")) {

					dj = executor.getRoles().get(i); // Store Role with name "DJ" in Role variable.
					break; // Break loop.

				}

			}
			
			// If command executor has permission or "DJ" role, execute command.
			if ((!executor.hasPermission(Permission.ADMINISTRATOR) && executor.getRoles().contains(dj)) || executor.hasPermission(Permission.ADMINISTRATOR) || executor.getRoles().contains(dj)) {
				
				Member prometheus = event.getGuild().getSelfMember(); // Create new variable to store information about bot's user.
				GuildVoiceState executorVC = event.getMember().getVoiceState(); // Create new variable to store voice state of executor.
				VoiceChannel VC = executorVC.getChannel(); // Create new variable to store voice channel that executor is currently connected to.

				// If bpt is already connected to a channel, send error message and return nothing.
				if (audio.isConnected()) {

					channel.sendMessageFormat(":x: **|** I'm already in **%s** %s", VC.getName(), executor.getAsMention()).queue();
					return;

				}

				// If executor is not in a voice channel, send error message and return nothing.
				if (!executorVC.inVoiceChannel()) {

					channel.sendMessageFormat(":x: **|** You need to be in **%s** for me to join **%s**! %s", VC.getName(), VC.getName(), executor.getAsMention()).queue();
					return;

				}
				
				// If bot does not have permission to Connect and Speak in executor's voice channel, send error message and return nothing.
				if (!prometheus.hasPermission(VC, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK)) {

					channel.sendMessageFormat(":x: **|** I am missing permissions for **%s**", VC.getName()).queue();
					return;

				}

				audio.openAudioConnection(VC); // Open audio connection.

				channel.sendMessageFormat("Joined **%s**", VC.getName()).queue();

			}

			// Else if user does not have permission or the "DJ" role, send error message and return nothing.
			else if (!executor.getRoles().contains(dj) && !executor.hasPermission(Permission.ADMINISTRATOR)) {

				channel.sendMessage(":x: **|** *Sorry, you do not have the* `DJ Role` or `Administrator` *permission node!* " + executor.getAsMention()).queue();
				return;

			}

		}

	}

}
