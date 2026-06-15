# Finished Bounty Notify

A RuneLite plugin that sends a notification when you finish collecting all parts for a sailing bounty task.

Licensed under the [BSD 2-Clause License](LICENSE).

## How it works

The plugin watches port task varbits (same approach as [Port Tasks](https://github.com/nucleon/port-tasks)). When the remaining bounty part count for a slot reaches zero, it can notify you.

Required item counts are resolved in this order:

1. **Captain's log / notice board / task info UI** — parsed when you open those interfaces
2. **Game client data** — port task DB table loaded on login
3. **OSRS Wiki fallback** — baked-in quantities from [Bounty tasks](https://oldschool.runescape.wiki/w/Bounty_tasks)

Regenerate the wiki fallback after Jagex changes task requirements:

```bash
python3 scripts/generate-wiki-fallback.py
```

## Notification modes

- **Each task** — notify when any single accepted bounty task is finished collecting
- **All tasks for monster** — if you have multiple bounty tasks for the same monster (e.g. two great white shark tasks for different parts), notify only once all of them are finished collecting. A single veiled kraken task still notifies when that one task is done.

## Development

This project is based on the [RuneLite example plugin](https://github.com/runelite/example-plugin).

### Prerequisites

- JDK 11 ([Eclipse Temurin](https://adoptium.net/) recommended; see [RuneLite wiki](https://github.com/runelite/runelite/wiki/Building-with-IntelliJ-IDEA))
- An OSRS account with access to sailing bounty tasks for testing

### Run locally

Per the [plugin-hub development guide](https://github.com/runelite/plugin-hub#createing-new-plugins), run the plugin with the Gradle `run` task:

```bash
./gradlew run
```

This launches RuneLite in developer mode with the plugin loaded.

#### Jagex accounts

If your account uses a [Jagex Account](https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts), set up saved credentials once before running `./gradlew run`:

1. Use RuneLite launcher **2.6.3 or newer**.
2. Open the launcher config:
   - **Windows:** Start menu → `RuneLite (configure)`
   - **macOS:** `/Applications/RuneLite.app/Contents/MacOS/RuneLite --configure`
   - **Linux:** run your launcher with `--configure`
3. In **Client arguments**, add `--insecure-write-credentials` and click **Save**.
4. Launch RuneLite from the **Jagex Launcher** once. This writes `~/.runelite/credentials.properties`.
5. Run `./gradlew run`. The dev client uses those saved credentials automatically.

Do not share `credentials.properties`. When you are done testing, delete `~/.runelite/credentials.properties`. To invalidate the credentials remotely, use **End sessions** in your account settings on [runescape.com](https://www.runescape.com).

### Test in your normal RuneLite client (optional)

To load the plugin in your installed RuneLite instead of the dev client, side-load it:

1. Build the plugin jar:

```bash
./gradlew jar
```

2. Copy `build/libs/finished-bounty-notify.jar` to `~/.runelite/sideloaded-plugins/`.
3. Add `--developer-mode` to your RuneLite launcher **Client arguments** (same configure window as above).
4. Start RuneLite and enable **Finished Bounty Notify** in the plugin configuration panel.

RuneLite only loads jars from `sideloaded-plugins` when `--developer-mode` is set.

## Publish to Plugin Hub (optional)

1. Push this repo to GitHub.
2. Open a PR to [runelite/plugin-hub](https://github.com/runelite/plugin-hub) adding your repo to the manifest.
3. See the [plugin-hub README](https://github.com/runelite/plugin-hub) for the full submission process.

## Configuration

- **Notify on completion** — toggle notifications on/off
- **Notification mode** — each task, or all tasks for the same monster

Notification messages include the bounty task name (each-task mode) or monster name (all-tasks-for-monster mode).

Notifications respect RuneLite's global notification settings (sounds, tray notifications, flash, etc.).
