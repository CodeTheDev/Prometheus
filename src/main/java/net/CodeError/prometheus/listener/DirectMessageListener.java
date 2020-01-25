package net.CodeError.prometheus.listener;

import net.CodeError.prometheus.util.Logger;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DirectMessageListener extends ListenerAdapter {
	
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		
		System.out.printf(Logger.CONSOLE_PREFIX + Logger.CONSOLE_SUBPREFIX_DM + "[%s]: %s\n", event.getAuthor().getName(), event.getMessage().getContentDisplay());
		
	}

}
