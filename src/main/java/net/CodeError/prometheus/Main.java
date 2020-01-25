package net.CodeError.prometheus;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.CodeError.prometheus.audio.command.Disconnect;
import net.CodeError.prometheus.audio.command.Join;
import net.CodeError.prometheus.audio.command.Pause;
import net.CodeError.prometheus.audio.command.Play;
import net.CodeError.prometheus.audio.command.Skip;
import net.CodeError.prometheus.audio.command.Stop;
import net.CodeError.prometheus.audio.command.Volume;
import net.CodeError.prometheus.command.Ban;
import net.CodeError.prometheus.command.BruhMoment;
import net.CodeError.prometheus.command.Calculator;
import net.CodeError.prometheus.command.Clear;
import net.CodeError.prometheus.command.CoinFlip;
import net.CodeError.prometheus.command.DiceRoll;
import net.CodeError.prometheus.command.Help;
import net.CodeError.prometheus.command.Kick;
import net.CodeError.prometheus.command.Ping;
import net.CodeError.prometheus.command.Screenshare;
import net.CodeError.prometheus.command.Sudo;
import net.CodeError.prometheus.command.Vote;
import net.CodeError.prometheus.listener.DirectMessageListener;
import net.CodeError.prometheus.util.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class Main implements EventListener {

	public static void main(String[] args) throws LoginException, InterruptedException {

		System.out.println("=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=");
		System.out.println("Prometheus Bot v2.0 - Console Interface");
		System.out.println("Created By: CodeError#0001");
		System.out.println("https://www.codeslab.ca/prometheus/");
		System.out.println("=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=");

		System.out.println();

		System.out.print("Please enter bot access token: ");

		Scanner input = new Scanner(System.in); // Create input scanner.
		String token = input.nextLine(); // Grab input from scanner and store in String variable for token.

		System.out.println();

		try {

			// Define JDA instance.
			JDA prometheus = new JDABuilder(token)
					
					// Add Commands
					.addEventListeners(new Main(), new DirectMessageListener(), new Ping(), new Kick(), new Ban(), new Calculator(), new DiceRoll(), new CoinFlip(), new BruhMoment(), new Sudo(), new Clear(), new Help(), new Vote(), new Disconnect(), new Join(), new Pause(), new Play(), new Skip(), new Stop(), new Volume(), new Screenshare())
					
					// Set bot status.
					.setActivity(Activity.watching("the server. | p!help"))
					
					// Build JDA instance.
					.build();

			// Wait for bot to finish pre-check operations and start.
			prometheus.awaitReady();

		}

		// If bot token is invalid, catch exception.
		catch (Exception e) {

			System.out.println();

			System.out.println(e.getMessage());
			System.out.println("Press enter to terminate...");
			input.nextLine();

		}

		input.close(); // Close input scanner.

	}
	
	@Override
	public void onEvent(GenericEvent event) {

		// When bot instance is ready, output message in console.
		if (event instanceof ReadyEvent) {

			System.out.println(Logger.CONSOLE_PREFIX + Logger.CONSOLE_SUBPREFIX_INFO + "Bot Online, API Ready!");

		}

	}

}