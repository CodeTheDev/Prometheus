package net.CodeError.prometheus.command;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Kick extends ListenerAdapter {
	
	public static final String NAME = "p!kick <@User>";
	public static final String DESCRIPTION = "Allows user with Kick permissions to kick users below them on the rank hierarchy.";
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {

			return;

		}

		Guild guild = event.getGuild(); // Define Guild variable to get current server.
		Member executor = event.getMember(); // Define an instance of Member to get command executor.
		List<User> targetUsersList = event.getMessage().getMentionedUsers(); // Create list of users mentioned in command.
		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		MessageChannel channel = event.getChannel(); // Get current channel command is executed in.

		// If command text is entered, execute command.
		if (msgContent.startsWith("p!kick")) {
			
			long commandID = msg.getIdLong();
			channel.deleteMessageById(commandID).queue();
			
			// If command executor has permission, execute command.
			if (executor.hasPermission(Permission.KICK_MEMBERS)) {
				
				// If targetUsersList is empty return invalid arguments message.
				if (targetUsersList.isEmpty()) {

					channel.sendMessage(":warning: **|** *Invalid Arguments! Please mention a user to kick!*").queue();
					
					return;

				}
				
				// When targetUsersList is not empty, get users as members from corresponding guild and kick. Catch errors using lambda.
				for (User user : targetUsersList) {
					Member targetUser  = guild.getMember(user);

					guild.kick(targetUser).queue(
							success -> channel.sendMessage(":white_check_mark: **|** *Kicked* " + targetUser.getAsMention()).queue(),
							error -> {

								if (error instanceof PermissionException) {

									channel.sendMessage(":x: **|** *Could not kick* " + targetUser.getAsMention() + " *due to your permissions level.*").queue();

								}
								
								if (error instanceof HierarchyException) {
									
									channel.sendMessage(":x: **|** *Could not kick* " + targetUser.getAsMention() + " *due to role hierarchy.*").queue();
									
								}

								else {

									channel.sendMessage(":x: **|** *Could not kick* " + targetUser.getAsMention() + " *due to unknown exception.").queue();

								} 

							});
				}

			}
			
			// If executor does not have permission, send error message and return nothing.
			else if (!executor.hasPermission(Permission.KICK_MEMBERS)) {

				channel.sendMessage(":x: **|** *Sorry, you do not have permission to execute this command!*").queue();

				return;

			}

		}

	}

}
