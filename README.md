<h1 align="center">💿 pikap</h1>

CLI YouTube player without ads. Only audio stream from YouTube by [Lavaplayer](https://github.com/sedmelluq/lavaplayer).
<br/>
🕰️ <b>Planned</b> update for support the SoundCloud, Twitch and local audio files. 

### Features
1. Minimal system usage (~100MB Memory)
2. Fluent experience
3. Scalable
4. Easy to use
5. Many planned features

<h2 align="center">📸 Screenshots</h2>

<details>

<summary>Click here for screenshots</summary>

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
- [ ] Equalizer for player
- [ ] Support for SoundCloud, Twitch and local audio files.
- [ ] Local web page for remote control the player
- [x] Database initialization and integration
- [ ] Storing information of past songs with SQLite
- [ ] Endless play mode with next suggested content from the song's YouTube page
- [x] Discord RPC
- [x] More efficient input-output handler (maybe tui?)
- [x] Colorized console handler with Chalk
- [x] Enchance the play command
- [ ] Config commands
- [ ] Repeat and shuffle commands
- [x] Previous/back command
- [x] Queue/list command
- [ ] Optional: Output device selector
- [x] Optional: Advanced search command
