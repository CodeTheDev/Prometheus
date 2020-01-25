package net.CodeError.prometheus.audio.command;

import net.CodeError.prometheus.audio.util.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class Play extends ListenerAdapter {

	public static final String NAME = "p!play <URL>";
	public static final String DESCRIPTION = "Queues/Plays any audio/video from a valid URL source, and unpauses the bot if currently paused.";

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
		AudioManager audio = event.getGuild().getAudioManager(); // Create new instance of AudioManager.

		// If command text is entered, execute command.
		if (msgContent.startsWith("p!play")) {
			
			Role dj = null; // Create new Role and store empty (null) value to initialize.

			// Filter through executor's roles and see if executor possesses a role with the name "DJ". If executor possesses
			// this role, store Role in Role variable and break for loop. If executor does not possess this role, keep value set to empty (null).
			for (int i = 0; i < executor.getRoles().size(); i++) {

				if (executor.getRoles().get(i).getName().equalsIgnoreCase("DJ")) {

					dj = executor.getRoles().get(i); // Store Role with name "DJ" in Role variable.
					break; // Break loop.

				}

			}

			String[] args = msgContent.split(" "); // Split message at each space into separate arguments.

			// If command executor has permission or "DJ" role, execute command.
			if ((!executor.hasPermission(Permission.ADMINISTRATOR) && executor.getRoles().contains(dj)) || executor.hasPermission(Permission.ADMINISTRATOR) || executor.getRoles().contains(dj)) {

				PlayerManager manager = PlayerManager.getInstance(); // Create new instance of PlayerManager.

				VoiceChannel VC = event.getMember().getVoiceState().getChannel(); // Create new variable to store voice channel that executor is currently connected to.

				// If the bot is not connected to an audio channel, send error message and return nothing.
				if (!audio.isConnected()) {

					audio.openAudioConnection(VC);

				}

				// If the bot is in a voice channel where the executor is not present, send error message and return nothing.
				if (!VC.getMembers().contains(executor)) {

					channel.sendMessageFormat(":x: **|** Do not try and remotely queue tracks. %s", executor.getAsMention()).queue();
					return;

				}

				// If the bot is paused, un-pause the bot.
				if (manager.getGuildMusicManager(guild).player.isPaused()) {

					manager.getGuildMusicManager(guild).player.setPaused(false);
					return;

				}

				// If volume does not equal 100 (default), keep volume at present level and load track.
				if (manager.getGuildMusicManager(guild).player.getVolume() != 100) {

					manager.load(channel, args[1]);

				}

				// Else load track and set volume to 100 (default).
				else {

					manager.load(channel, args[1]);
					manager.getGuildMusicManager(guild).player.setVolume(100);

				}

			}

			// Else if user does not have permission or the "DJ" role, send error message and return nothing.
			else if (!executor.getRoles().contains(dj) && !executor.hasPermission(Permission.ADMINISTRATOR)) {

				channel.sendMessage(":x: **|** *Sorry, you do not have the* `DJ Role` *or* `Administrator` *permission node!* " + executor.getAsMention()).queue();
				return;

			}

		}

	}

}
