# Adding Faction Names to Player Chat Messages

This guide explains how to display a player's faction name in chat using Improved Factions, PlaceholderAPI (PAPI), and a
chat formatting plugin.

## Requirements

1. [Improved Factions](https://modrinth.com/plugin/improved-factions)
2. PlaceholderAPI (PAPI) ([Download from SpigotMC](https://www.spigotmc.org/resources/placeholderapi.6245/))
3. A chat formatting plugin (e.g., EssentialsX or LPC Chat Formatter):
    - EssentialsX users require
      ChatInjector ([Download from SpigotMC](https://www.spigotmc.org/resources/chatinjector-1-13.81201/)) to use
      placeholders.

## Steps

### 1. Install the Plugins

- Download and add **Improved Factions**, **PAPI**, and a chat formatter (e.g., EssentialsX or LPC) to your server’s
  `plugins` folder.
- Start your server to generate configuration files.

### 2. Add PlaceholderAPI Placeholders

- Improved Factions supports placeholders like `%faction_name%`, `%faction_rank%`, and others.
- The placeholders should be automatically registered by improved factions - Check the server log, as Improved Factions
  will print a message saying that placeholders have been enabled.

### 3. Configure Chat Formatting

#### EssentialsX (With ChatInjector)

> Essentials doesnt support placeholders out of the box, so a separate injector has to be used. The chosen injector
> uses {} instead of %

1. Locate and open `Essentials/config.yml`.
2. Find the `format:` key.
3. Update it using `{}` for placeholders (specific to EssentialsX + ChatInjector):
   ```yaml
   format: "{DISPLAYNAME} [{faction_name}] >> {MESSAGE}"
   ```
4. Restart the server.

#### LPC Chat Formatter

1. Install LPC ([Download from SpigotMC](https://www.spigotmc.org/resources/lpc-chat-formatter-1-7-10-1-20.68965/)).
2. Update LPC’s configuration file to use `%` placeholders directly:
   ```yaml
   chat:
     format: "%player% [%faction_name%] >> %message%"
   ```
3. Save and restart the server.

## 4. Handling Factionless Players

By default, placeholders like `%faction_name%` may display nothing for factionless players. To fix this:

- Use the configuration option `default-placeholder` for placeholders in the Improved Factions config.
- Example:
   ```yaml
   default-placeholders:
    name: "No Faction"
   ```

## 5. Add Colors

- Add color codes to your format using `&` (or `§`).
- Example (green faction names):
   ```yaml
   format: "{displayname} [&a{faction_name}&r] >> {message}"
   ```

## 6. Test Your Setup

- Join a faction in-game (`/f create <name>`) and check the chat formatting.
- For factionless players, confirm the default value appears.

## **Troubleshooting**

- **Placeholder not showing:** Verify PAPI is installed and placeholders are loaded (`/papi parse me %faction_name%`).
- **Factionless issue:** Ensure `default-values` are set in the configuration file.
- **Essentials config missing:** If `format:` is not found, switch to a dedicated chat plugin like LPC.

---

For further assistance, visit the [Improved Factions Discord](https://discord.com/invite/VmSbFNZejz) or report issues
on [GitHub](https://github.com/ToberoCat/ImprovedFactions).