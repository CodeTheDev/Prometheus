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

public class Ban extends ListenerAdapter {
	
	public static final String NAME = "p!ban <@User>";
	public static final String DESCRIPTION = "Allows user with Ban permissions to ban users belown them on the rank hierarchy.";
	
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
		if (msgContent.startsWith("p!ban")) {
			
			long commandID = msg.getIdLong(); // Get ID of command sent.
			channel.deleteMessageById(commandID).queue(); // Delete command message.
			
			// If command executor has permission, execute command.
			if (executor.hasPermission(Permission.BAN_MEMBERS)) {
				
				// If targetUsersList is empty return invalid arguments message.
				if (targetUsersList.isEmpty()) {

					channel.sendMessage(":warning: **|** *Invalid Arguments! Please mention a user to ban!*").queue();

				}
				
				// When targetUsersList is not empty, get users as members from corresponding guild and ban. Catch errors using lambda.
				for (User user : targetUsersList) {
					Member targetUser  = guild.getMember(user);

					guild.ban(targetUser, 0).queue(
							success -> channel.sendMessage(":white_check_mark: **|** *Banned* " + targetUser.getAsMention()).queue(),
							error -> {

								if (error instanceof PermissionException) {

									channel.sendMessage(":x: **|** *Could not ban* " + targetUser.getAsMention() + " *due to unhandled permission exception.*").queue();

								}
								
								else if (error instanceof HierarchyException) {
									
									channel.sendMessage(":x: **|** *Could not ban* " + targetUser.getAsMention() + " *due to hierarchy exception.*").queue();
									
								}

								else {

									channel.sendMessage(":x: **|** *Could not ban* " + targetUser.getAsMention() + " *due to unknown exception.").queue();

								}

							});
				}

			}
			
			// If executor does not have permission, send error message and return nothing.
			else if (!executor.hasPermission(Permission.BAN_MEMBERS)) {

				channel.sendMessage(":x: **|** *Sorry, you do not have permission to execute this command!*").queue();

				return;

			}

		}

	}

}
