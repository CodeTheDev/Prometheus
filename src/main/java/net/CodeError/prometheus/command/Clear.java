package net.CodeError.prometheus.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

public class Clear extends ListenerAdapter {

	public static final String NAME = "p!clear <num>";
	public static final String DESCRIPTION = "Allows user with Manage Messages permission to delete messages.";

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {

			return;

		}

		Member executor = event.getMember(); // Define an instance of Member to get command executor.
		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		TextChannel channel = event.getChannel(); // Get channel where command was sent.

		// If command text is entered, execute command.
		if (msgContent.startsWith("p!clear")) {

			long commandID = msg.getIdLong();
			channel.deleteMessageById(commandID).complete();

			String[] args = msgContent.split(" "); // Split arguments into individual segments.

			// If command executor has permission, execute command.
			if (executor.hasPermission(Permission.MESSAGE_MANAGE)) {

				try {

					MessageHistory history = new MessageHistory(channel); // Create new instance of MessageHistory in channel.
					List<Message> msgs = new ArrayList<>(); // Create new List to store type Message.

					int delete = Integer.parseInt(args[1]); // Create new integer to store amount of messages to delete.

					// If messages to delete is less than 2, send error message and return nothing.
					if (delete < 2) {

						channel.sendMessage(":x: **|** *You must delete at least 2 messages!*").queue();
						return;

					}

					// If messages to delete is more than 100, send error message and return nothing.
					else if (delete > 100) {

						channel.sendMessage(":x: **|** *You cannot delete more than 100 messages!*").queue();
						return;

					}

					// If messages to delete is between 2 and 100, store messages to delete in List, delete messages and send success message. Delete success message after 5 seconds.
					else if (delete >= 2 && delete <= 100) {

						msgs = history.retrievePast(Integer.parseInt(args[1])).complete(); // Store messages in List.
						channel.deleteMessages(msgs).complete(); // Delete messages.
						int deleted = msgs.size(); // Get amount of deleted messages.

						RestAction<Message> raSuccess = channel.sendMessage(":white_check_mark: **|** *Deleted* `" + deleted + "` *messages.*"); // Create RestAction to store success message.
						Message success = raSuccess.complete(); // Send success message.

						long successMsgID = success.getIdLong(); // Get ID of success message.

						channel.deleteMessageById(successMsgID).queueAfter(5, TimeUnit.SECONDS); // Delete success message after 5 seconds.

					}

					// If messages to delete is empty, send error message and return nothing.
					else {

						channel.sendMessage(":warning: **|** Please specify amount of messages to delete!").queue();
						return;

					}

				}

				// Catch various exceptions that could occur.
				catch(Exception e) {
					
					channel.sendMessage(":warning: **|** *Invalid Arguments! You may have used non-numbers or there are less than 2 messages in the current channel.*").queue();

				}

			}

			// If executor does not have permission, send error message and return nothing.
			else if (!executor.hasPermission(Permission.MESSAGE_MANAGE)) {

				channel.sendMessage(":x: **|** *Sorry, you do not have permission to execute this command!*").queue();

				return;

			}

		}

	}

}
