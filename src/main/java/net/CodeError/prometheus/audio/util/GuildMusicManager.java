package net.CodeError.prometheus.audio.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
	
	// This class was not made by me. Included with LavaPlayer.

	public final AudioPlayer player;

	public final TrackScheduler scheduler;

	public GuildMusicManager(AudioPlayerManager manager) {
		
		player = manager.createPlayer();
		scheduler = new TrackScheduler(player);
		player.addListener(scheduler);
		
	}

	public AudioPlayerSendHandler getSendHandler() {
		
		return new AudioPlayerSendHandler(player);
		
	}
	
}
