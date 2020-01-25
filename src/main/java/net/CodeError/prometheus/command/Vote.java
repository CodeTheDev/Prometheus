package net.CodeError.prometheus.command;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

public class Vote extends ListenerAdapter {

	public static final String NAME = "p!vote <question> answer1/answer2[/answer3...]";
	public static final String DESCRIPTION = "Creates a vote with a questiom and a maximum of 10 possible answers.";

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

		// If command text is entered, execute command.
		if (msgContent.startsWith("p!vote") || msgContent.startsWith("p!poll")) {
			
			long commandID = msg.getIdLong(); // Get ID of command sent.
			channel.deleteMessageById(commandID).queue(); // Delete command message.

			Role voteRole = null; // Create new Role and store empty (null) value to initialize.

			// Filter through executor's roles and see if executor possesses a role with the name "Vote Creator". If executor possesses
			// this role, store Role in Role variable and break for loop. If executor does not possess this role, keep value set to empty (null).
			for (int i = 0; i < executor.getRoles().size(); i++) {

				if (executor.getRoles().get(i).getName().equalsIgnoreCase("Vote Creator")) {

					voteRole = executor.getRoles().get(i); // Store Role with name "Vote Creator" in Role variable.
					break; // Break loop.

				}

			}
			
			// If command executor has permission or "Vote Creator" role, execute command.
			if ((!executor.hasPermission(Permission.ADMINISTRATOR) && executor.getRoles().contains(voteRole)) || executor.hasPermission(Permission.ADMINISTRATOR) || executor.getRoles().contains(voteRole)) {

				try {

					String[] argsAnswers = msgContent.split("/"); // Split answers using "/" in command.

					String question = msgContent.substring(msgContent.indexOf('<') + 1, msgContent.indexOf('>')); // Get text in between question block. Ex. <hello> = hello

					String[] answers = new String[argsAnswers.length]; // Create String array for valid answers.

					// Due to some formatting issues with the first answer while splitting the arguments, create a for loop to transfer
					// answers to new String array. If i equals 0, modify the first element in the array to be formatted correctly and stored,
					// else transfer element from old array to new array.
					for (int i = 0; i < argsAnswers.length; i++) {

						if (i == 0) {

							answers[i] = msgContent.substring(msgContent.indexOf('>') + 1, msgContent.indexOf("/"));

						}

						else {

							answers[i] = argsAnswers[i];

						}

					}

					// If the user has specified more than 10 answers, send error message and return nothing.
					if (answers.length > 10) {

						channel.sendMessage(":x: **|** *You cannot have more than 10 possible answers in your vote!*").queue();
						return;

					}
					
					// Else if user has specified less than 2 answers, send error message and return nothing.
					else if (answers.length < 2) {
						
						channel.sendMessage(":x: **|** *You must have 2 or more possible answers in your vote!*").queue();
						return;
						
					}

					// Else continue executing command.
					else {

						RestAction<Message> raVote = channel.sendMessage(createVote(executor, question, answers)); // Create new RestAction that takes type Message to store vote message embed to send.
						Message vote = raVote.complete(); // Send vote message embed.

						long voteMsgID = vote.getIdLong(); // Get ID of vote message embed.

						String[] reactions = {"U+0031 U+20E3", "U+0032 U+20E3", "U+0033 U+20E3", 
											  "U+0034 U+20E3", "U+0035 U+20E3", "U+0036 U+20E3", 
											  "U+0037 U+20E3", "U+0038 U+20E3", "U+0039 U+20E3", 
											  "U+1F51F"}; // Create new String array to store Unicode Codepoint formatted emoticons 1 to 10.

						// Create for loop to react to vote message embed with the appropriate amount of reactions. Get the length of the
						// answers array and react to vote message embed using Unicode Codepoint formatted emoticons starting at 1.
						for (int j = 0; j < answers.length; j++) {

							channel.addReactionById(voteMsgID, reactions[j]).complete();

						}

					}

				}

				// Catch all other exceptions.
				catch(Exception e) {

					channel.sendMessage(":warning: **|** *Invalid arguments!* Correct Syntax: **p!vote <question> answer1/answer2**").queue();

				}

			}

			// Else if user does not have permission or the "Vote Creator" role, send error message and return nothing.
			else if (!executor.getRoles().contains(voteRole) && !executor.hasPermission(Permission.ADMINISTRATOR)) {

				channel.sendMessage(":x: **|** *Sorry, you do not have the* `Vote Creator` *or* `Administrator` *permission node!* " + executor.getAsMention()).queue();
				return;

			}

		}

	}

	// Method to create a vote using parameters from user. Private to limit use to exclusively this class.
	private MessageEmbed createVote(Member executor, String question, String[] answers) {

		EmbedBuilder eb = new EmbedBuilder(); // Create new instance of EmbedBuilder to build RichEmbed.
		String[] reactions = {":one:", ":two:", ":three:", ":four:", 
							  ":five:", ":six:", ":seven:", ":eight:", 
							  ":nine:", ":keycap_ten:"}; // Create new string array to store text-formatted emoticons 1 to 10.

		eb.setColor(new Color(255, 0, 220)); // Set color of embed to purple-pink.
		eb.setAuthor(executor.getUser().getName() + "'s Poll", null, executor.getUser().getAvatarUrl()); // Create header for vote using executor's name and profile photo.

		eb.addField("Question:", question, false); // Create new field for question and take input from variable question.

		// Create for loop to add answer Fields from answers array variable. If j equals 0, title field with "Answers:", else no title.
		for (int j = 0; j < answers.length; j++) {

			if (j == 0) {

				eb.addField("Answers:", reactions[j] + " " + answers[j], false);

			}

			else {

				eb.addField("", reactions[j] + " " + answers[j], false);

			}

		}

		eb.addField("", "React with " + reactions[0] + " - " + reactions[answers.length - 1], false); // Create new field at the bottom of the embed telling users what emoticons to react with.

		return eb.build(); // Return built rich embed.

	}

}
