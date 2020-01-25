package net.CodeError.prometheus.audio.util;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {
	
	// This class is a custom PlayerManager heavily modified by me. Supports all types of audio links (YouTube, SoundCloud, mp4s, etc.).
	// Utilizes LavaPlayer library with thread synchronization capabilities between APIs (JDA, LavaPlayer).

	private static PlayerManager INSTANCE; // Create new instance of PlayerManager to be utilized by other classes.
	private final AudioPlayerManager playerManager; // Create new instance of LavaPlayer provided AudioPlayerManager.
	private final Map<Long, GuildMusicManager> musicManagers; // Create new HashMap to store all GuildMusicManager instances.

	private PlayerManager() {

		this.musicManagers = new HashMap<>();
		this.playerManager = new DefaultAudioPlayerManager();

		AudioSourceManagers.registerRemoteSources(playerManager); // Register remote sources for player.
		AudioSourceManagers.registerLocalSource(playerManager); // Register local sources for player.

	}

	// Method to get current GuildMusicManager. Enables guild specific audio functions. Synchronized to prevent audio errors.
	public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {

		long guildID = guild.getIdLong(); // Get guild ID.
		GuildMusicManager musicManager = musicManagers.get(guildID); // Create new instance of GuildMusicManager which stores GuildMusicManager for guildID.

		// If musicManager is null, fallback to playerManager to manually add guild to GuildMusicManager list.
		if (musicManager == null) {

			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guildID, musicManager);

		}

		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler()); // Get and initialize AudioPlayerSendHandler for guild.

		return musicManager; // Return new GuildMusicManager.

	}

	// Method for parsing/loading tracks into playerManager per guild.
	public void load(TextChannel channel, String URL) {

		GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild()); // Create new instance of GuildMusicManager.

		// Create responses for loading items into playerManager.
		playerManager.loadItemOrdered(musicManager, URL, new AudioLoadResultHandler() {

			// Method for responding to track successfully loaded.
			@Override
			public void trackLoaded(AudioTrack track) {

				// If no track is playing, send "Now Playing" message instead of "Added to Queue" message.
				if (musicManager.player.getPlayingTrack() == null) {

					channel.sendMessageFormat(":arrow_forward: **|** Now Playing ***%s*** by **%s**", track.getInfo().title, track.getInfo().author).queue();
					play(musicManager, track); // Call play method.

				}

				// Else send "Added to Queue" message.
				else {

					channel.sendMessageFormat(":play_pause: **|** Added ***%s*** by **%s** to the queue.", track.getInfo().title, track.getInfo().author).queue();
					play(musicManager, track); // Call play method.

				}

			}

			// Method for responding to playlist successfully loaded.
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {

				AudioTrack track = null; // Set AudioTrack to null (empty).

				for (int i = 0; i < playlist.getTracks().size(); i++) {

					track = playlist.getTracks().get(i); // Get track from playlist.
					
					// If no track is playing, send "Now Playing" message instead of "Added to Queue" message.
					if (musicManager.player.getPlayingTrack() == null) {

						channel.sendMessageFormat(":arrow_forward: **|** Now Playing ***%s*** by **%s** from [**%s**]", track.getInfo().title, track.getInfo().author, playlist.getName()).queue();
						play(musicManager, track); // Call play method.

					}

					// Else call play method.
					else {
						
						play(musicManager, track);

					}

				}
				
				channel.sendMessageFormat(":play_pause: **|** Added ***%s*** more songs to the queue.", playlist.getTracks().size() - 1).queue(); // Send message with how many songs have been added to queue.

			}

			// Method for responding to no track found.
			@Override
			public void noMatches() {

				channel.sendMessageFormat(":x: **|** Invalid URL: **%s**", URL).queue(); // Send error message.

			}

			// Method for responding to miscellaneous error. (Video not available in country, video taken down, etc.).
			@Override
			public void loadFailed(FriendlyException exception) {

				channel.sendMessageFormat(":x: **|** Could not load: **%s**", exception.getMessage()).queue(); // Send error message.

			}

		});

	}

	// Method for playing songs through player.
	private void play(GuildMusicManager musicManager, AudioTrack track) {

		// If no track is playing, play track specified immediately.
		if (musicManager.player.getPlayingTrack() == null) {

			musicManager.player.playTrack(track);

		}

		// Else queue specified track.
		else {

			musicManager.scheduler.queue(track);

		}

	}

	// Method to allow other classes in the program to properly access an instance of PlayerManager. Synchronized.
	public static synchronized PlayerManager getInstance() {

		// If INSTANCE variable is null (empty), set INSTANCE variable equal to a new instance of PlayerManager.
		if (INSTANCE == null) {

			INSTANCE = new PlayerManager();

		}

		return INSTANCE; // Return INSTANCE variable.

	}

}
