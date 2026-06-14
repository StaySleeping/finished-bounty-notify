# Finished Bounty Notify

A RuneLite plugin that sends a notification when you finish collecting all parts for a sailing bounty task.

Licensed under the [MIT License](LICENSE).

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

- JDK 11+
- An OSRS account with access to sailing bounty tasks for testing

### Run locally

```bash
./gradlew run
```

This launches RuneLite in developer mode with the plugin loaded.

### Build a plugin jar

```bash
./gradlew shadowJar
```

The jar is written to `build/libs/`.

## Install without publishing to Plugin Hub

1. Build the shadow jar (above).
2. In RuneLite, open the wrench icon → Plugin Hub → the settings/gear icon → **Install plugin from disk**.
3. Select the built jar.

## Publish to Plugin Hub (optional)

1. Push this repo to GitHub.
2. Open a PR to [runelite/plugin-hub](https://github.com/runelite/plugin-hub) adding your repo to the manifest.
3. See the [plugin-hub README](https://github.com/runelite/plugin-hub) for the full submission process.

## Configuration

- **Notify on completion** — toggle notifications on/off
- **Notification mode** — each task, or all tasks for the same monster
- **Include task name** — include the bounty task or monster name in the notification message

Notifications respect RuneLite's global notification settings (sounds, tray notifications, flash, etc.).
