<h1 align="center">💿 pikap</h1>

CLI YouTube player without ads. Only audio stream from YouTube by [Lavaplayer](https://github.com/sedmelluq/lavaplayer).
<br/>
🕰️ <b>Planned</b> update for support the SoundCloud, Twitch and local audio files. 

#### Current Version: beta-0.2

### Objectives & Goals
1. Minimal system usage (~150MB Memory)
2. Fluent experience
3. Scalable
4. Easy to use
5. Many planned features

### Current Features
1. YouTube search, selection at search result, YouTube Playlist playback, and queue management.
2. Queue system: adding to queue, navigating within the queue and going back.
3. Ability to view upcoming songs page by page, configurable via config.
4. Song repeat mode. Ability to add songs to favorites. Play using favorite song selection or play all favorites as a playlist.
5. Adding and removing songs from the favorites list.
6. Show previously played *songs* and options to replay, add to queue, or delete them.
7. Show previously played *playlists* and options to replay, add to queue, or delete them.
8. Equalizer (some issues present but will be resolved).
9. Commands to enable/disable DiscordRPC.
10. Monitoring and garbage collectioning.


<h2 align="center">📸 Screenshots</h2>

<details>

<summary>Click here for screenshots</summary>
<h2><b>Those images from alpha version!</b></h2>
<img src="images/1.png" />
</br>
<img src="images/2.png" />

</details>

<h2 align="center">⚙️ How To Build And Run</h3>

I will also share the compiled files when <b>pikap</b> goes into beta version.

Requirements are: Maven3, JDK >= 11
```
git clone https://github.com/koply/pikap
cd pikap
mvn install
sh run.sh
```

<h2 align="center">🗒️ Task List</h3>

- [ ] Download option.
- [x] Equalizer for player
- [ ] Support for SoundCloud, Twitch and local audio files.
- [ ] Local web page for remote control the player
- [x] Database initialization and integration
- [x] Storing information of past songs with SQLite
- [ ] Endless play mode with next suggested content from the song's YouTube page
- [x] Discord RPC
- [x] More efficient input-output handler (maybe tui?)
- [x] Colorized console handler with Chalk
- [x] Enchance the play command
- [x] Config commands
- [ ] Config management commands
- [x] Favourite commands and system
- [x] Playlist commands
- [x] Repeat command
- [ ] Shuffle command
- [x] Previous/back command
- [x] Queue/list command
- [ ] Optional: Output device selector
- [x] Optional: Advanced search command
