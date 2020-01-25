package net.CodeError.prometheus.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Calculator extends ListenerAdapter {

	public static final String NAME = "p!calc <num> <operator> <num>";
	public static final String DESCRIPTION = "Peforms mathematical calculations such as addition, subtraction, multiplication, etc.";

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		// If command is sent by another bot, return nothing.
		if (event.getAuthor().isBot()) {

			return;

		}

		Message msg = event.getMessage(); // Define message event variable as "msg".
		String msgContent = msg.getContentRaw(); // Define content of message variable. Make sure to get raw message without formatting.
		MessageChannel channel = event.getChannel(); // Get channel where command was sent.

		// If command text is entered, execute command.
		if (msgContent.startsWith("p!calc")) {
			
			long commandID = msg.getIdLong();
			channel.deleteMessageById(commandID).queue();

			String[] args = msgContent.split(" "); // Split arguments into individual segments.

			try {

				double num1 = Integer.parseInt(args[1]); // Store first int in variable.
				String operation = args[2]; // Store operator in variable.
				double num2 = Integer.parseInt(args[3]); // Store second int in variable.
				
				// Addition
				if (operation.equals("+")) {

					double sum = num1 + num2;

					channel.sendMessage(":white_check_mark: **|** " + event.getAuthor().getAsMention() + "*The sum of your calculation is:* **" + " " + sum + "**").queue();

				}
				
				// Subtraction
				else if (operation.equals("-")) {

					double sum = num1 - num2;

					channel.sendMessage(":white_check_mark: **|** " + event.getAuthor().getAsMention() + "*The sum of your calculation is:* **" + " " + sum + "**").queue();

				}
				
				// Multiplication
				else if (operation.equals("*")) {

					double sum = num1 * num2;

					channel.sendMessage(":white_check_mark: **|** " + event.getAuthor().getAsMention() + "*The sum of your calculation is:* **" + " " + sum + "**").queue();

				}
				
				// Division
				else if (operation.equals("/")) {

					double sum = num1 / num2;

					channel.sendMessage(":white_check_mark: **|** " + event.getAuthor().getAsMention() + "*The sum of your calculation is:* **" + " " + sum + "**").queue();

				}
				
				// Exponents
				else if (operation.equals("^")) {

					double sum = Math.pow(num1, num2);

					channel.sendMessage(":white_check_mark: **|** " + event.getAuthor().getAsMention() + "*The sum of your calculation is:* **" + " " + sum + "**").queue();

				}

			}
			
			// If operator is not valid or integers are not integers, catch exceptions.
			catch(Exception e) {

				channel.sendMessage(":warning: **|** *Invalid Arguments! Please input 2 integers and a valid operator for calculations!*").queue();

			}

		}

	}

}
