# QA (Discord export)

## Disclaimer

This document provides a collection of solutions to common issues and frequently asked questions regarding the Improved
Factions plugin. The content has been sourced from the support [Discord server](https://discord.com/invite/VmSbFNZejz),
and I’d like to extend my gratitude to everyone who helps with answering questions and troubleshooting on a daily basis.

Please note that this export may contain similar or duplicate solutions, as it hasn’t been thoroughly filtered
(essentially, it's a bulk export from the [Discord server](https://discord.com/invite/VmSbFNZejz)).

## Finding a Solution

To locate a solution, it's recommended to use your browser’s search function (usually `Ctrl + F`) and search for
keywords related to your issue. If you're unable to find what you're looking for, feel free to reach out on
the [Discord server](https://discord.com/invite/VmSbFNZejz).

## Questions and Answers

### **Error: Confirmation is required to execute this command**

**Problem:** User encountered an error when trying to delete a faction, thinking it was a permission issue.

**Solution:**

- The user was informed that the error is not related to permissions but is a safety confirmation to prevent accidental
  deletion.
- The correct command to delete a faction is `/f delete confirm`.

**Key takeaway:** When deleting factions, use the full command to confirm your intention.

---

### **How to Open the Faction Ranks Menu**

**Problem:** User asked how to open a specific faction-related menu.

**Solution:**

- The ranks menu can be accessed by installing the `guiengine` plugin and typing `/f ranks` in the chat.

**Key takeaway:** Ensure `guiengine` is installed to access the ranks menu via `/f ranks`.

---

### **Disabling Particles in the Game**

**Problem:** User wanted to disable particles.

**Solution:**

- Particles can be disabled by turning off the claim particle module in the configuration file.

**Key takeaway:** Disable particles by modifying the appropriate module in the configuration file.

---

### **Viewing Faction Information**

**Problem:** User wanted to view information about their own faction or other factions.

**Solution:**

- The following commands were provided:
    - `/f info` shows your own faction's information.
    - `/f info <faction>` shows another faction's information.
    - `/f list` lists all factions on the server.

**Key takeaway:** Use these commands to access faction information directly from the chat.

---

### **GUI Navigation Issues**

**Problem:** User was confused about the back and forward navigation in the GUI.

**Solution:**

- The back and forward buttons are designed to navigate between pages in the GUI but not to return to the previous GUI.
- If needed, the `.gui` files in the plugin can be modified to adjust the GUI behavior.

**Key takeaway:** Customize GUI behavior by modifying the `.gui` files if the default navigation is not suitable.

---

### **Text-Based Faction Information**

**Problem:** User wanted faction information in text format rather than a GUI.

**Solution:**

- The GUI can be replaced with text-based versions by disabling the GUI module entirely.

**Key takeaway:** Disabling the GUI module will allow text-based faction information instead of the graphical version.

---

### **Ally and Enemy System Development**

**Problem:** User couldn't find the ally and enemy system in the current version.

**Solution:**

- The ally and enemy system is still under development and not fully functional in version 2.3.0-dev.

**Key takeaway:** The faction's ally and enemy system is still a work in progress in the current development build.

---

### **Running the Plugin on a Server with Java 17**

**Problem:** User couldn't load a plugin due to a Java version mismatch (plugin required Java 21, but their server was
running Java 17).

**Solution:**

- The developer provided a Java 17-compatible build of the plugin to resolve the issue.

**Key takeaway:** Ensure your server’s Java version matches the plugin requirements or request a compatible version from
the developer.

---

### **Displaying Player Names Instead of Numbers**

**Problem:** User wanted to display the names of faction participants instead of just showing the number of
participants.

**Solution:**

- This feature is not available in the current version, but users can submit a feature request on GitHub for future
  versions.

**Key takeaway:** If a feature isn't available, it can be requested for future releases on the project’s GitHub page.

### **Why is the Owner Stored as BLOB in the Database?**

**Problem:** User noticed that the owner of a faction is stored as BLOB data in their MySQL database and found it
difficult to work with when displaying faction owners on their website.

**Solution:**

- The owner is stored as a UUID, not a BLOB. The issue lies in how the OR mapper handles storage across different
  databases.
- On SQLite, it stores the UUID as `var(36)`, while MySQL stores it as a `BINARY(16)` for efficiency.
- To retrieve the UUID in a readable format, use `SELECT HEX(owner) AS owner FROM table_name;`.
- After converting the UUID to a string, use the Mojang API to get the player's name and other details.

**Key takeaway:** For databases like MySQL that store UUIDs as `BINARY(16)`, use `HEX()` to convert them back to a
readable format and then fetch player information via the Mojang API.

---

### **Why Use an External SQL Database Instead of SQLite?**

**Problem:** User asked if there is a benefit to using an external SQL database over SQLite.

**Solution:**

- External SQL databases, like MySQL, are recommended if you are running multiple servers and need faction data to sync
  across them.
- For a single server, SQLite is usually sufficient.

**Key takeaway:** Use an external SQL database when managing multiple servers for data synchronization, but SQLite works
well for most single-server setups.

---

### **How Can I Prevent Faction Leaders from Customizing Ranks?**

**Problem:** User wanted to prevent faction leaders from creating custom ranks and enforce the same ranks for all
factions.

**Solution:**

- Use a permissions manager to disable rank editing in the game. This ensures the default ranks are applied universally
  and cannot be modified by faction leaders.
- The documentation for configuring permissions can be
  found [here](https://github.com/ToberoCat/ImprovedFactions/blob/main/docs/permissions.md).

**Key takeaway:** To enforce uniform ranks, disable rank customization using a permissions manager.

---

### **How Can I Limit the Number of Characters in a Faction Name?**

**Problem:** User wanted to set a limit on how many characters can be used in a faction name.

**Solution:**

- The maximum length for a faction name can be set in the configuration file. Look for `max-name-length` in the config
  and set the desired limit.

**Key takeaway:** Configure the maximum faction name length by adjusting the `max-name-length` setting in the
configuration file.

---

### **Disabling Land Claim Functionality for Factions**

**Problem:** User wanted to disable the land claim feature and replace it with an economy-based claim system.

**Solution:**

- You can disable the land claiming feature by adjusting permissions. However, disabling this feature means factions
  won't be able to claim land using the default system.
- The alternative system, like an economy-based claim, would need to be implemented separately.

**Key takeaway:** Disable land claiming through permissions, but keep in mind that this removes the ability for factions
to claim land unless an alternative system is implemented.

---

### **Using Placeholder API (PAPI) with Improved Factions**

**Problem:** User couldn’t see Improved Factions in the Placeholder API (PAPI) and assumed they had to manually add it.

**Solution:**

- Improved Factions automatically integrates with PAPI if it is detected. There's no need to manually install it like
  other plugins.
- If it's not working, restarting the server and waiting for the plugin to initialize might resolve the issue.

**Key takeaway:** Improved Factions integrates with PAPI automatically, and restarting the server can resolve detection
issues.

---

### **Handling Faction Chunk Clustering with PAPI**

**Problem:** User was experiencing delays with PAPI loading due to faction chunk clustering.

**Solution:**

- This is an issue with faction chunk clustering, not PAPI itself. Updating to the latest development build should
  provide better logging and faster processing.

**Key takeaway:** Update to the latest development build to improve performance and logging during faction chunk
clustering when using PAPI.

- **Plans for Pl3xMap Support**
  **Problem:** User asked if Pl3xMap would be supported by the plugin.

  **Solution:**
    - The developer hadn't planned it initially but mentioned it should be simple to implement, similar to Dynmap
      support, and agreed to add it in the future when time permits.

  **Key takeaway:** Pl3xMap support will be implemented, but the plugin already supports Dynmap.

---

- **How to Rename a Faction**
  **Problem:** User wanted to know if they could rename a faction.

  **Solution:**
    - The command `/f rename` allows users to rename their faction.

  **Key takeaway:** Use `/f rename` to rename your faction.

---

- **Disabling Overclaiming and Raiding**
  **Problem:** User wanted to disable all overclaiming and raiding while keeping chunk claiming.

  **Solution:**
    - Disabling the power system will turn off raids and claiming limits. However, once disabled, there is no max claim
      limit.
    - Alternatively, by disabling claim costs and player death penalties, claims can become unraidable.

  **Key takeaway:** Disabling the power system removes overclaiming and raiding but also disables claim limits.

---

- **Max Claim Limit without Power System**
  **Problem:** User wanted to set a max claim limit when the power system is disabled.

  **Solution:**
    - There's no way to set a claim limit if the power system is turned off. Claims will be unlimited.

  **Key takeaway:** No max claim limit exists if the power system is disabled.

---

- **Installing GUI Engine for Commands**
  **Problem:** User wanted to know if they needed command aliases for the GUI engine.

  **Solution:**
    - The GUI engine is optional and adds GUI interfaces for commands. Without it, commands will be used through the
      chat.

  **Key takeaway:** GUI engine is optional; without it, commands are handled via chat.

---

- **Blacklisting a World from Claims**
  **Problem:** User wanted to blacklist a specific world from being claimed.

  **Solution:**
    - The correct syntax is:
      ```
      blacklisted-worlds:
        - twilightforest
      ```

  **Key takeaway:** Use the folder name of the world in the config to blacklist it.

---

- **Economy-Based Power for Factions**
  **Problem:** User wanted to know if power could be bought with in-game currency.

  **Solution:**
    - Not natively supported, but users can use external plugins to add power with the command
      `/factions power add <powertype> <power> <faction>`.

  **Key takeaway:** Power can be added via external plugins and commands but is not natively tied to an economy.

---

- **Dynmap Accessibility for Players**
  **Problem:** User wanted to allow all players to access Dynmap.

  **Solution:**
    - Players can access the Dynmap by typing `<your server url>:<port set in the Dynmap config>` in their browser.
    - Make sure the port is forwarded correctly.

  **Key takeaway:** Dynmap can be accessed via a browser using the server URL and port number.

---

- **Offline Mode Player Detection**
  **Problem:** User reported that offline mode players were not properly detected by the plugin.

  **Solution:**
    - The plugin has issues with offline mode players. Compatibility issues may exist, and further development might be
      needed for better offline mode support.

  **Key takeaway:** Offline mode player detection is problematic, and development may be needed for full compatibility.

---

- **Improved Factions Compatibility with GeyserMC**
  **Problem:** User wanted to know if the plugin is compatible with GeyserMC.

  **Solution:**
    - Improved Factions works with GeyserMC, but the invite menu has a known bug. Bedrock-specific menus are under
      development by other developers.

  **Key takeaway:** Improved Factions works with GeyserMC, but some menu features may have issues.

---

- **PlaceholderAPI Issues**
  **Problem:** User had trouble displaying faction placeholders with PlaceholderAPI (PAPI).

  **Solution:**
    - The issue was a problem with placeholder parsing in the plugin. The developer identified the bug and planned to
      fix it in a future update.

  **Key takeaway:** Placeholder issues with PAPI will be resolved in future updates; parsing errors may exist in certain
  builds.

---

- **War Mechanics Between Factions**
  **Problem:** User wanted to know if the plugin supports war mechanics between enemy factions.

  **Solution:**
    - The plugin does not currently support declaring war. A siege occurs when a faction loses power and others can
      claim their land.

  **Key takeaway:** There is no formal war declaration yet, but sieges happen when factions lose power.

---

- **Changing Plugin Language to Spanish**
  **Problem:** User wanted to change the plugin language to Spanish but encountered issues.

  **Solution:**
    - The `.lang` files are outdated for version 2.x.x. To add Spanish, create a new file in the language folder with
      the correct language code. The plugin will use the player’s game language settings to show translated messages.

  **Key takeaway:** For version 2.x.x, create a new language file, and the plugin will adjust based on players' language
  settings.

---

- **Language File Issues**
  **Problem:** User added the Spanish language file but saw some messages were not translated.

  **Solution:**
    - The issue occurred because certain messages were missing from the Spanish translation file.

  **Key takeaway:** Ensure all required translations are present in the language file to avoid missing messages.

### **Pl3xmap Support**

**Problem:** User asked if Pl3xmap will be supported by the plugin.

**Solution:**

- The developer mentioned that while it’s not currently supported, adding support for Pl3xmap should be similar to the
  existing Dynmap implementation and can be done in the future.

**Key takeaway:** Pl3xmap support is not available yet, but it may be added in future updates similar to Dynmap.

---

### **Renaming a Faction**

**Problem:** User wanted to rename a faction.

**Solution:**

- The command `/f rename` allows you to rename a faction.

**Key takeaway:** Use the `/f rename` command to change a faction’s name.

---

### **Disabling Overclaiming and Raiding**

**Problem:** User wanted to disable all overclaiming and raiding features and use the plugin solely for chunk claiming.

**Solution:**

- Disabling the power system also disables raiding and claim limits.
- The user can turn off these features by adjusting the config settings like `base-claim-power-cost` and
  `player-death-cost`.

**Key takeaway:** To disable overclaiming and raiding, adjust the power system in the configuration settings.

---

### **Setting Max Claim Limits without Power System**

**Problem:** User wanted to set a maximum claim limit after disabling the power system.

**Solution:**

- Disabling the power system removes the claim limit. There is no native way to limit claims without the power system.

**Key takeaway:** There is no claim limit without the power system, but using the power system allows for limits.

---

### **Installing GUI Engine**

**Problem:** User asked if they need command aliases when installing the GUI engine.

**Solution:**

- The GUI engine is optional. If not installed, the plugin will use chat-based commands. Installing it switches some
  commands to GUI-based interactions.

**Key takeaway:** The GUI engine is optional and replaces some chat commands with GUI-based interactions if installed.

---

### **World Blacklisting for Claiming**

**Problem:** User wanted to blacklist a specific world from being claimed.

**Solution:**

- The correct way to blacklist a world is by adding the folder name of the world in the config under
  `blacklisted-worlds` like this:
  ```
  blacklisted-worlds:
    - twilightforest
  ```

**Key takeaway:** Use the world folder name to blacklist worlds from claiming in the configuration file.

---

### **Supporting Economies in Faction Claims**

**Problem:** User wanted to allow factions to "buy" power through an economy system.

**Solution:**

- This is not natively supported, but you can use another plugin to run commands like
  `/factions power add <powertype> <power> <faction>` to integrate it with an economy system.

**Key takeaway:** While not natively supported, you can combine other plugins to implement economy-based power
purchasing.

---

### **Disabling Overclaiming in the Power System**

**Problem:** User wanted to disable overclaiming while keeping claims unraidable.

**Solution:**

- To disable overclaiming, set `claim-power-keep` and `player-death-cost` to 0 in the config. This will prevent claims
  from being raided by ensuring factions always have enough power.

**Key takeaway:** Adjust `claim-power-keep` and `player-death-cost` to 0 to prevent raiding and ensure claims remain
secure.

---

### **Dynmap Access for Players**

**Problem:** User wanted to know how to allow players to use Dynmap.

**Solution:**

- Provide players with the server URL and port as configured in Dynmap. Example:
  ```
  <Your server URL>:<port>
  ```
- Ensure that port forwarding is enabled.

**Key takeaway:** Share the server URL and Dynmap port with players, and ensure the port is forwarded.

---

### **Offline Mode Players Not Detected**

**Problem:** User reported that offline mode players are not being detected by the plugin, preventing invites.

**Solution:**

- Offline mode detection for players may not be fully supported in the current version of the plugin. It’s a known
  issue.

**Key takeaway:** Offline mode support may have issues with player detection in the current plugin version.

---

### **GeyserMC Compatibility**

**Problem:** User wanted to know if the plugin is compatible with GeyserMC (Bedrock proxy).

**Solution:**

- The plugin is compatible with GeyserMC, but there may be bugs with menus, particularly the invite menu.

**Key takeaway:** The plugin works with GeyserMC, though menu-related bugs may occur.

---

### **Faction Placeholder Not Working**

**Problem:** User reported that faction placeholders were not displaying correctly despite having PlaceholderAPI (PAPI)
installed.

**Solution:**

- The developer acknowledged that it was a bug with the plugin's placeholder parsing and promised to fix it.

**Key takeaway:** Placeholder issues are a known bug and are expected to be fixed in future updates.

---

### **War Mechanics Between Factions**

**Problem:** User asked if the plugin includes war mechanics between factions.

**Solution:**

- The plugin doesn’t currently support declaring war, but "sieges" allow other factions to claim chunks from a faction
  with insufficient power.

**Key takeaway:** The plugin uses a siege system where factions with low power lose territory but doesn’t support formal
declarations of war.

---

### **Spanish Language Support**

**Problem:** User wanted to apply the Spanish language to the plugin.

**Solution:**

- The plugin uses the player's game language settings. To apply Spanish, create a new file in the language folder with
  the correct language code. The plugin will use these translations if the player’s game is set to Spanish.

**Key takeaway:** Players’ game language settings determine the language used by the plugin, and translations are
applied automatically based on the language files.

### **People Can Break Blocks in Safezones and Warzones**

**Problem:** Some players are able to break blocks in safezones and warzones, while others cannot. This issue persists
even across different plugin versions.

**Solution:**

- The issue could be related to plugin configurations or interference from other plugins. It was suggested to ensure the
  zone settings in the configuration are correct and to confirm that other plugins aren’t causing interference.
- One possible fix is using the latest version or switching database types (e.g., from SQLite to H2 or MySQL) to see if
  the issue persists.
- Check for the setting `allow-claiming` in the zones section of the configuration and ensure it’s set to `false` for
  safezones and warzones.

**Key takeaway:** Verify that zone configurations are correct, and check if database type or plugin interference could
be causing the issue.

---

### **Faction Menus Allowing Item Removal**

**Problem:** Players were able to remove items from faction menus, which shouldn’t be possible.

**Solution:**

- This may be a bug in the specific version of the plugin being used. Downgrading to a more stable version or updating
  to the latest one might resolve the issue.

**Key takeaway:** Test different plugin versions and report bugs for fixes if items can be removed from menus.

---

### **Players Can Join Any Faction**

**Problem:** In some plugin versions, players were able to join any faction without restriction.

**Solution:**

- This is intended behavior if the `/f join` command is used. To prevent this, adjust permissions using a plugin like
  LuckPerms to restrict who can use the `/f join` command.

**Key takeaway:** Use a permission manager like LuckPerms to restrict faction joining if necessary.

---

### **Database-Related Issues**

**Problem:** Players experienced issues with zone protections and other functionality that could be related to the
database.

**Solution:**

- Switching from SQLite to H2 or MySQL was suggested as a way to test if database performance or configuration issues
  were causing the problems.

**Key takeaway:** Test different database options (e.g., H2, MySQL) to see if they improve performance or resolve
issues.

---

### **Faction Chat Formatting with Essentials**

**Problem:** User struggled to format the chat to show faction names using Essentials and PlaceholderAPI (PAPI), with
placeholders not displaying correctly.

**Solution:**

- Essentials doesn’t natively support PAPI placeholders, so using ChatInjector was recommended to enable the
  placeholders.
- The correct format should include `%%` for placeholders, but if that doesn’t work, using ChatInjector will fix it.

**Key takeaway:** Use ChatInjector with Essentials to enable PAPI placeholders for chat formatting.

---

### **Safezone and Warzone Claiming Issues**

**Problem:** Players were able to claim or modify blocks in safezones and warzones if they weren’t in a faction, causing
issues with land protection.

**Solution:**

- Ensure that claiming is disabled in the safezone and warzone configurations. This can be controlled with the
  `allow-claiming` option in the config.
- If players are still able to claim, it could indicate a misconfiguration or a bug in the version being used.

**Key takeaway:** Double-check the `allow-claiming` setting in the zone configuration to prevent claims in safezones and
warzones.

---

### **Using ChatInjector with PlaceholderAPI**

**Problem:** Placeholders for factions were not working properly in Essentials chat formatting, breaking the display.

**Solution:**

- ChatInjector is required to enable PlaceholderAPI placeholders in Essentials chat formatting. Following the guide and
  adding ChatInjector will resolve the issue.

**Key takeaway:** Use ChatInjector to enable PAPI placeholders in Essentials chat formatting.

---

### **Updating Minecraft and Plugin Versions**

**Problem:** Issues persisted across multiple versions of the plugin, and the user was unsure which version of Minecraft
or the plugin to use.

**Solution:**

- The user was advised to update to the latest Minecraft version and use version latest version of the plugin, while
  also ensuring that the configuration file matches the updated version’s requirements.

**Key takeaway:** Keep both Minecraft and the plugin updated, and ensure the config file is updated accordingly to avoid
compatibility issues.

---

### **Plugin and Mod Compatibility**

**Problem:** User asked if the plugin protects modded blocks and whether additional plugins are needed.

**Solution:**

- The plugin protects modded blocks as long as they trigger the standard Minecraft block break and place events. Extra
  plugins, like Dynmap, GUIEngine, or WorldGuard, may enhance features but aren’t required.

**Key takeaway:** Modded blocks are protected by default, but additional plugins may offer extra features.

### **Error When Enabling ImprovedFactions**

**Problem:** The plugin fails to enable with an error message related to MySQL or SQLite.

**Solution:**

- The plugin defaults to SQLite if it cannot connect to the MySQL database. Ensure that your MySQL credentials are
  correct in the `config.yml` file.
- If you’re using a host like PebbleHost, verify the database name, username, and password. If there are issues with
  MySQL, try switching to SQLite or H2 by changing the `database` field in the configuration.

**Key takeaway:** Double-check database configurations and switch between MySQL, SQLite, or H2 if needed.

---

### **People Can Break Blocks in Safezones and Warzones**

**Problem:** Some players are able to break blocks in safezones and warzones while others cannot, even with no changes
to the configuration.

**Solution:**

- Ensure that the zone protection settings are correctly configured. The zones should be configured to prevent any block
  breaking regardless of the player's faction status.
- Check if other plugins are interfering with the zone settings or if there is a lag issue due to database
  misconfigurations.

**Key takeaway:** Verify zone protection settings and check for potential plugin interference or database lag.

---

### **Lag When Using Commands**

**Problem:** Commands such as `/f help` cause the server to lag or freeze, with high server latency reported.

**Solution:**

- High latency may be related to database performance. Switching from MySQL to SQLite or H2 may resolve latency issues.
- If the issue persists, reducing the load from other plugins or optimizing the server's performance could help.

**Key takeaway:** Try different database setups (SQLite, H2, MySQL) and optimize server performance to reduce lag during
command execution.

---

### **Faction Commands Not Responding**

**Problem:** The `/f help` or other faction commands do not respond, or the server freezes when trying to execute them.

**Solution:**

- Ensure that the plugin is fully installed with all dependencies (such as GUIEngine).
- Check the console logs for any specific errors related to the plugin and try running the command after reducing the
  server load by disabling unnecessary plugins.

**Key takeaway:** Ensure all dependencies are installed and review server logs for potential errors.

---

### **MySQL Access Denied**

**Problem:** Users receive an "Access denied for user" error when trying to connect to the MySQL database.

**Solution:**

- Ensure that the database name and credentials in the `config.yml` match those provided by your hosting service.
- If your hosting service prepends a string (e.g., `customer_724101_`) to your database name, make sure this is
  reflected in the configuration.

**Key takeaway:** Verify that the MySQL credentials and database name match the information provided by your hosting
service.

---

### **Placeholder API (PAPI) and GUI Engine Issues**

**Problem:** On-screen GUIs like walking into claimed areas or the `/f wilderness` command do not appear despite having
PlaceholderAPI and GUIEngine installed.

**Solution:**

- Ensure that the `config.yml` is correctly set up and that no settings are hiding the GUIs. Some settings in the config
  may hide notifications.
- If using an unsupported game version, check for updates to the libraries or plugin to ensure compatibility.

**Key takeaway:** Verify that the config doesn’t accidentally hide GUIs, and ensure compatibility with your game
version.

---

### **Faction Claim Issues**

**Problem:** Players in a faction other than the owner cannot use the `/f claim` command, leading to errors.

**Solution:**

- This issue may be related to how claims are grouped into clusters. If the cluster detection is failing, the plugin
  might not recognize the correct claim group for a faction.
- The developer is aware of the issue and is working on a fix.

**Key takeaway:** If players in a faction are unable to claim land, it may be related to claim cluster management, and a
fix may be needed from the developer.

---

### **Permission Issues with Faction Commands**

**Problem:** Players cannot use `/f home` or `/f wilderness` unless they are OP, and permissions don’t seem to be
applied correctly.

**Solution:**

- Permissions for these commands need to be explicitly set using a permissions manager like LuckPerms. The correct
  permissions are `factions.wilderness` and `factions.home`.
- Ensure that the permissions are set for the correct group and that any changes take effect after a restart or
  reloading the permissions.

**Key takeaway:** Set the correct permissions in your permission manager to allow players to use faction-related
commands.

---

### **Adding Players to Factions Manually**

**Problem:** Users wanted to manually add players to a faction and limit the number of factions.

**Solution:**

- There is currently no built-in way to manually add players to a faction or limit the number of factions. However, you
  can request these features on GitHub as feature requests for future updates.

**Key takeaway:** Manual faction management features can be requested but are not currently available.

---

### **Faction Invite Tab Completion Error**

**Problem:** When using `/f invite`, typing each character causes an internal error, and sometimes the player isn’t
recognized.

**Solution:**

- As a workaround, use `/f join <faction>` to manually join factions without requiring an invite.
- The developer is aware of the issue and will work on fixing the tab completion and invite recognition.

**Key takeaway:** Use `/f join <faction>` as a temporary fix for faction invites while the issue is being addressed.

### **How to Get Custom Player Heads in GUI**

**Problem:** User wanted to use custom heads in a GUI but encountered errors when using "head-texture".

**Solution:**

- You need to use the actual head texture ID, which is a long string containing numbers and letters. These can be found
  online through sites that provide player head textures or by using a plugin that retrieves them.

**Key takeaway:** Ensure you're using a valid head texture ID when adding custom heads to a GUI.

---

### **Improved Factions Plugin Shows as Red in `/plugins` Command**

**Problem:** The Improved Factions plugin shows up as red in the plugin list, indicating it isn’t working.

**Solution:**

- Check the server console for errors, especially for missing dependencies. Often, the issue is due to not having the
  required `GuiEngine` plugin installed. Download it from [Modrinth](https://modrinth.com/plugin/guiengine).

**Key takeaway:** Make sure `GuiEngine` is installed, as it is a dependency of Improved Factions.

---

### **How to Use LuckPerms for Permissions in Improved Factions**

**Problem:** User was new to hosting and unsure how permissions worked with Improved Factions.

**Solution:**

- Use a permissions manager like LuckPerms to manage permissions for factions. You can assign permissions such as
  `factions.wilderness` or `factions.home` using LuckPerms commands or the web editor.

**Key takeaway:** LuckPerms is the recommended plugin for managing permissions with Improved Factions.

---

### **Error When Running `/f invite` with Tab Completion**

**Problem:** The `/f invite` command causes errors when using tab completion, and some players aren’t recognized.

**Solution:**

- As a temporary workaround, use `/f join <faction>` instead of relying on the invite system.
- The developer is aware of this issue and a fix is in progress.

**Key takeaway:** Use `/f join <faction>` as a workaround until the invite tab completion issue is resolved.

---

### **Error When Inviting Friend to Faction: "The provided argument do not fit the requirements"**

**Problem:** User encountered an error when trying to invite a friend to the faction.

**Solution:**

- In version 2.x, the command requires both the player’s name and their rank. Example: `/f invite <player> <rank>`.
- If your friend is having issues accepting the invite, they can use the `/f invites` command to open a GUI where they
  can accept the invite without worrying about command arguments.

**Key takeaway:** Ensure the invite command includes both the player name and rank, or use the GUI to avoid argument
issues.

---

### **"The provided arguments don't fit" Error in `/f inviteaccept` Command**

**Problem:** A player encountered an error while using `/f inviteaccept` with arguments like `FACTIONNAME`.

**Solution:**

- Instead of typing the faction name, use `/f invites` to access the GUI for accepting invites. This simplifies the
  process and avoids argument-related errors.
- Alternatively, review the help command `/f help inviteaccept` for the correct syntax if you prefer using commands.

**Key takeaway:** Use the GUI for accepting faction invites to avoid argument issues.

---

### **Making a Faction Private**

**Problem:** User couldn’t find a way to make a faction private.

**Solution:**

- This feature was available in previous versions, but is not yet supported in version 2.x. If absolutely necessary, you
  can manually set this in the database.

**Key takeaway:** Faction privacy settings are not currently available in version 2.x, but can be adjusted via the
database.

---

### **PAPI Placeholders Not Working**

**Problem:** User reported that the `%faction_name%` placeholder from PlaceholderAPI (PAPI) was not returning the
correct value.

**Solution:**

- Ensure that both PAPI and Improved Factions are up to date.
- If placeholders still don’t work, check if the PAPI extension for Improved Factions has been registered in the
  console (e.g., `[ImprovedFactions] Loaded improved factions papi extension`).
- You may also need a chat injector plugin to display placeholders in chat,
  like [ChatInjector](https://www.spigotmc.org/resources/chatinjector-1-13.81201/).

**Key takeaway:** Ensure the PAPI extension is registered, and use a chat injector to display placeholders in chat.

---

### **How to Check Rank for Inviting Players**

**Problem:** User was unsure how to invite someone to their faction due to rank confusion.

**Solution:**

- The rank for the invite should be specified in the command. If unsure, check the rank structure or use the GUI to
  manage invitations more easily.

**Key takeaway:** Include both player name and rank in the invite command, or use the GUI for a simpler approach.

---

### **Permissions for Faction Commands Not Working**

**Problem:** Some users could not use `/f home` or other faction commands, and permissions didn’t seem to apply
correctly.

**Solution:**

- Use a permissions manager like LuckPerms to assign the appropriate permissions (e.g., `factions.home` or
  `factions.wilderness`).
- If the permissions aren’t taking effect, ensure the correct syntax and reload the permissions setup.

**Key takeaway:** Use a permission manager and double-check your permission setup for faction commands.

---

### **How to Use Placeholders in Chat**

**Problem:** User tried to use placeholders (e.g., `%faction_name%`) in chat, but it didn’t work even with VaultChat and
ChatInjector.

**Solution:**

- Check if the placeholders are registered with PAPI (`[ImprovedFactions] Loaded improved factions papi extension`).
- Ensure that both Improved Factions and PlaceholderAPI are up to date. The use of a chat injector plugin (
  like [ChatInjector](https://www.spigotmc.org/resources/chatinjector-1-13.81201/)) may be required for placeholders to
  work in chat.

**Key takeaway:** Ensure the PAPI extension is loaded, and use a chat injector to enable placeholders in chat.

---

### **Error in Faction Claiming**

**Problem:** Some faction members were unable to use `/f claim`, receiving an error.

**Solution:**

- This issue could be related to how claim clusters are handled within factions. Ensure that the player is in the
  correct faction and that there are no overlapping claim permissions.
- If the error persists, it may be a bug that requires further investigation by the plugin developer.

**Key takeaway:** Check faction claim settings and consult with the developer if the issue persists.

### **Can Improved Factions Work Without a GUI?**

**Problem:** User asked if Improved Factions can function without a graphical user interface (GUI).

**Solution:**

- Yes, Improved Factions works without the GUI. The plugin does not rely on the GUI to function. The GUI only enhances
  the experience but is not required.

**Key takeaway:** Improved Factions can be used entirely through commands, without needing the GUI.

---

### **Squaremap / Pl3xMap Support**

**Problem:** User inquired about the status of Squaremap or Pl3xMap support in Improved Factions.

**Solution:**

- As of now, there are no immediate plans to update the plugin to support Squaremap or Pl3xMap. The developer is focused
  on fixing known bugs before considering new map support.

**Key takeaway:** No current support for Squaremap or Pl3xMap, but it may be revisited after bug fixes.

---

### **How to Delete Factions**

**Problem:** User wanted to know how to delete a faction.

**Solution:**

- Use the command `/f delete confirm` to delete a faction.

**Key takeaway:** Use `/f delete confirm` to delete a faction.

---

### **Disabling PvP Between Faction Members Not Working**

**Problem:** User reported that setting PvP between faction members to disabled in the config doesn't work.

**Solution:**

- The developer is aware of the issue and has requested a bug ticket be created for investigation.

**Key takeaway:** If disabling PvP between faction members isn't working, submit a bug ticket for review.

---

### **Allowing Interaction with Specific Items in Private Claims**

**Problem:** User wanted to allow interactions with specific items (e.g., enchantment tables) in private claims.

**Solution:**

- This feature is not supported yet. If needed for zones like safezones, use WorldGuard's unmanaged zones to handle
  specific interactions.

**Key takeaway:** Currently, there is no direct support for allowing specific interactions in private claims within
Improved Factions.

---

### **Folia Compatibility**

**Problem:** User asked whether Improved Factions or similar plugins support the Folia server fork.

**Solution:**

- Improved Factions has not been explicitly tested with Folia, but since Folia is a fork of Paper, it may be compatible.
  However, Folia introduces significant multithreading changes, so compatibility is not guaranteed.
- The developer is considering adding support for Folia, though it may take some time.

**Key takeaway:** Folia compatibility is not confirmed, but support may be added in the future.

## FAQ from Chat Logs

### **Setting Up Server Areas on the Map (Warzones, Safezones)**

**Problem:** User wanted to know if they could set up different server areas, such as warzones or safezones, using
Improved Factions.

**Solution:**

- Yes, you can set up different zones using the command `/f zone claim <zoneType>`. For example, to claim a safezone,
  you can use `/f zone claim safezone`.

**Key takeaway:** Use the `/f zone claim <zoneType>` command to define server areas like warzones and safezones.

---

### **Preventing Creeper Explosions in Claimed Plots**

**Problem:** User wanted to prevent creeper explosions in claimed plots.

**Solution:**

- The plugin does not natively support this, but you can manage explosions using another plugin like WorldGuard in
  unmanaged zones.
- The developer has since added an option to prevent creeper explosions, which will be available in the next build.

**Key takeaway:** The plugin will soon have a setting to prevent creeper explosions in claimed areas.

---

### **Invalid Component in Faction GUI**

**Problem:** Server logs showed errors about an invalid component in the faction GUI, such as `faction-detail-page.gui`
and `rank-detail.gui`.

**Solution:**

- These errors can be ignored. The issue comes from custom placeholders in the GUI, but it doesn’t affect functionality.

**Key takeaway:** Ignore these "invalid component" errors; they don't impact server operation.

---

### **Crash When Using `/factions wilderness` Command**

**Problem:** The server crashed when using the `/factions wilderness` command.

**Solution:**

- This is a known issue that has been fixed in the latest development version of the plugin. The stable version may
  still contain this bug.

**Key takeaway:** Use the latest development version to resolve the `/factions wilderness` crash issue.

---

### **How to Read the Owner's UUID Stored as BLOB in the Database**

**Problem:** User noticed that the faction owner's UUID is stored as a BLOB in the database and wanted to know how to
display this on their website.

**Solution:**

- The UUID is stored in a BLOB format, and you can convert it to a readable format using a SQL query such as
  `SELECT HEX(owner) AS owner FROM table_name;`. This will return the UUID in a usable format for display.

**Key takeaway:** Convert the BLOB data to a readable UUID using `SELECT HEX(owner)` for web display.

---

### **Disabling the Wilderness Command**

**Problem:** User wanted to disable the `/factions wilderness` command.

**Solution:**

- Go to the `config.yml` file and disable the wilderness module under the modules section.

**Key takeaway:** Disable the wilderness module in `config.yml` to remove the `/factions wilderness` command.

---

### **Plot Protection: Using WorldGuard with Improved Factions**

**Problem:** User asked about linking WorldGuard with Improved Factions for managing plot protection.

**Solution:**

- You can create unmanaged zones with Improved Factions that prevent faction claiming, and then use WorldGuard to manage
  the plot protection for those areas.

**Key takeaway:** Use unmanaged zones in Improved Factions along with WorldGuard to manage protection in specific areas.

### **Invalid Entity Error When Using Faction Commands**

**Problem:** A user encountered an issue where the `/faction force join` and `/f join <faction>` commands result in an
error saying "Invalid entity" or "The provided argument is not valid."

**Solution:**

- Ensure that the player is not already in a faction.
- As a workaround, use `/f invite <player> <rank>` to invite the player manually.
- If the issue persists, you may need to force-leave the player from any faction (although this feature is currently
  missing from the plugin).
- The plugin is missing some admin commands, such as `/f force leave` and `/f force info`, which would provide more
  control over factions.

**Key takeaway:** Use manual invites as a workaround for the invalid entity issue, and watch for updates with new admin
commands.

---

### **Issue with Faction Wilderness Command**

**Problem:** The `/f wilderness` command fails to find a valid position in the wilderness and doesn't work as expected.

**Solution:**

- This is a known issue and is being tracked on GitHub. Users are advised to use the latest development build for
  potential fixes or workarounds. The bug is being investigated, and updates will be shared on the issue page.

**Key takeaway:** Check GitHub for updates on the `/f wilderness` issue and ensure you're using the latest development
build.

---

### **Safezone Not Protecting Players from Mob Damage**

**Problem:** Players in the safezone are still taking damage from mobs, even though the config option
`monster-damage-player` is set to true.

**Solution:**

- Verify that players are correctly positioned within the safezone.
- Double-check the world configurations to ensure the safezone is properly set up.
- If the problem persists, updating to the latest development build might resolve the issue.

**Key takeaway:** Ensure correct zone setup and update to the latest version if the safezone is not protecting players
as intended.

---

### **Clustering and Faction Border Issues**

**Problem:** Faction borders aren't combining properly, leading to visual and functional issues with claims.

**Solution:**

- This is due to clustering bugs in the development build. Clusters are computed lazily to save resources, but the
  feature is still being optimized.

**Key takeaway:** The issue is being worked on, and downgrading to a stable version might help in the interim.

---

### **Chat Logs and /f Chat Visibility**

**Problem:** Faction chat messages sent using `/f chat` are not visible in the server console.

**Solution:**

- This is a requested feature. Future versions of the plugin will include the ability for faction chat logs to appear in
  the console for better moderation.

**Key takeaway:** Watch for updates that include faction chat logs in the console.

---

### **SQL and Cross-Server Sync**

**Problem:** The user wanted to sync claims and faction data across multiple servers using MySQL.

**Solution:**

- The plugin supports MySQL but has not been fully tested for cross-server syncing. Features like MongoDB and Redis are
  not supported at this time, but may be added in the future.

**Key takeaway:** MySQL is supported, but syncing across multiple servers is still experimental.

---

### **Faction Home Command Causing Issues**

**Problem:** The `/f home` command causes players to "rubber-band" back to the faction home and experience issues when
teleporting.

**Solution:**

- The developer is aware of this bug and has released a fix in the latest development build. Users experiencing this
  issue should update to the latest development version.

**Key takeaway:** Update to the latest development build to resolve issues with the `/f home` command.

---

### **Faction Claim Permissions for Public Zones**

**Problem:** Users wanted to create public zones where anyone can interact with buttons or chests, but not place or
break blocks.

**Solution:**

- This feature is planned and will allow per-chunk permissions for more granular control. Users will be able to
  configure claim permissions to allow or restrict interactions.

**Key takeaway:** A feature to configure per-chunk permissions is planned and will enable public zones with restricted
building rights.

### **Fabric Support**

**Question:** Is there any plan to adapt the ImprovedFactions plugin for Fabric?

**Response:**

- The ImprovedFactions plugin is built for the Bukkit environment, which is incompatible with Fabric or Forge. However,
  there are mods that emulate the Bukkit environment, allowing you to run Bukkit/Spigot plugins on Forge. That said,
  support for Fabric is not planned, and running the plugin on Fabric may result in unstable behavior.

**Key takeaway:** No plans to adapt ImprovedFactions for Fabric, and using emulation may lead to instability.

---

### **Getting an Error with `/f wilderness`**

**Problem:** The `/f wilderness` command results in an internal error or fails to find valid locations in the
wilderness.

**Solution:**

- Update to the latest development build, as this command has undergone fixes. If the issue persists, it is recommended
  to check the configurations to ensure the wilderness is set up correctly, and make sure the world is included in the
  list of whitelisted worlds for factions.

**Key takeaway:** Use the latest dev build and ensure correct world configuration for wilderness functionality.

---

### **Max Faction Members and Power Gain**

**Question:** Is there a way to set a maximum number of faction members? How much power does a player gain per hour?

**Response:**

- There is currently no limit for faction members in the configuration. The default power gain is 10 power per hour for
  each online player. If two players are online, they will collectively gain 20 power per hour, and so on.

**Key takeaway:** No member limit, and power gain is configurable based on the number of players online.

---

### **Issues with SafeZone and WarZone**

**Problem:** Monsters are still dealing damage to players in SafeZone, even though the configuration is set to prevent
this.

**Solution:**

- Double-check the safezone configuration to ensure it is correctly applied. Additionally, there have been discussions
  about improving features like mob spawning and protection, so it's worth keeping an eye on future updates that may
  address this.

**Key takeaway:** Ensure proper configuration of SafeZones and look for future updates with expanded protection
features.

---

### **Clustering Warnings on Server Boot**

**Problem:** Warnings about clustering issues appear on server boot, indicating that chunks cannot be clustered.

**Solution:**

- These warnings can be safely ignored. They are informational messages indicating that the server found chunk data
  assigned to a zone without a faction claim, which is expected behavior.

**Key takeaway:** The clustering warnings can be ignored, as they do not affect gameplay.

---

### **Faction GUIs Not Working**

**Problem:** Clicking buttons like "Back" or "Create New Rank" in GUIs does nothing.

**Solution:**

- GUIs are currently a broken feature, and it's recommended to avoid using them until further notice. Instead, use the
  corresponding commands for any actions you need to perform.

**Key takeaway:** Avoid using GUIs for now and rely on commands.

---

### **Syncing Factions Data Across Servers Using MySQL**

**Question:** Can faction and claim data be synced across servers using MySQL?

**Response:**

- While the plugin supports MySQL, syncing between multiple servers has not been fully tested. In theory, it may work if
  servers are properly set up to access the same database, but there's no official support for cross-server syncing via
  Redis or MongoDB at this time.

**Key takeaway:** MySQL syncing is experimental; official support for Redis or MongoDB may come in the future.

---

### **Admin Commands for ImprovedFactions**

**Missing Commands:** Users have reported missing admin commands, such as:

- `/f force leave` to forcibly remove a player from a faction.
- `/f force info` to get detailed information about a player's faction.

**Solution:**

- These commands are being considered for future updates. For now, admins may need to manually manage factions through
  workarounds, such as using invites or ownership transfers.

**Key takeaway:** Admin commands for detailed control over factions are in development.

### **Task #23444667 for ImprovedFactions v2.2.0 Generated an Exception**

**Issue:**
The server console shows repeated warnings of a `java.lang.IllegalStateException` relating to tasks not being scheduled
correctly.

**Possible Solution:**

1. **Restart the Server:** As a temporary fix, restart the server.
2. **Update to Latest Dev Build:** Upgrade to the latest 2.2.1-dev build, as this may include fixes for issues related
   to task scheduling.
3. **Report the Issue on GitHub:** If the issue persists, report it on
   the [ImprovedFactions GitHub repository](https://github.com/ToberoCat/ImprovedFactions) to get further assistance
   from the developer.

**Key takeaway:** Update to the latest version and report on GitHub if necessary.

---

### **Power Management & Claiming Issues**

**Issue:** The plugin uses power to control the number of claims a faction can make, but there are issues where claims
aren’t registering or power isn’t deducted.

**Solution:**

1. **Op Bypass:** Operators have bypass permissions for claim limits. Test claims without OP status to ensure the
   feature works properly for regular players.
2. **Power Configuration:** Adjust the power-related settings in the `config.yml`. To limit faction claims, set the
   power accumulation rate to 0 and adjust `base-claim-power-cost` as needed.
3. **Configuration Reset:** If power is not deducted during claims, try resetting the power management section in the
   config to default values and test again.

**Key takeaway:** OPs bypass power limits. Test without OP status and adjust power settings in the config.

---

### **Players Unable to Claim Land**

**Issue:** Players cannot claim land even though they have the necessary power, and a message says, "You can’t claim
chunk at x y z."

**Solution:**

1. **Check Claim Permissions:** Ensure players have the appropriate faction rank and permissions to claim land.
2. **World Configuration:** Verify that the world where they are trying to claim land is not blacklisted. Check the
   `whitelisted-worlds` or `blacklisted-worlds` settings in `config.yml` to ensure the correct worlds are configured.
3. **Zone Limitations:** Claims cannot be made in special zones like SafeZone or WarZone. Make sure they are not
   attempting to claim land in these protected areas.

**Key takeaway:** Ensure claim permissions are set, the world is properly configured, and they are not in a restricted
zone.

---

### **Faction Power Issues with Skript**

**Question:** Is it possible to integrate Skript with ImprovedFactions to get or set faction power?

**Solution:**

- **Skript Reflect:** Use Skript reflect to execute commands like setting or retrieving faction power.
- **Commands in Skript:** You can use commands to manage faction power via Skript. For example, run `/f power` or
  `/f setpower` commands from a Skript event.

**Key takeaway:** Use Skript reflect for managing faction power via commands.

---

### **SafeZone & PvP Issues**

**Issue:** Players are becoming protected from PvP even outside faction claims.

**Solution:**

1. **Check Zone Settings:** PvP settings are managed in the `zones` section of the `config.yml`. Verify that SafeZone
   and WarZone PvP rules are configured correctly.
2. **Disable Plugin for Debugging:** Temporarily disable the ImprovedFactions plugin to verify whether it’s causing the
   PvP protection issue.

**Key takeaway:** Review the `zones` section in the config file for PvP settings.

---

### **General Bug Fixes & Feature Requests**

- **Faction Deletion & PvP Issues:** When a faction is deleted, PvP in the formerly claimed zone might still be blocked.
  This is a known bug and will be fixed in upcoming updates.
- **Faction Power & Claims:** Claims may load in unusual ways if players run out of power mid-claim. The system
  sometimes creates incomplete strips, which may need fixing in future updates.

**Key takeaway:** Known bugs are being addressed in future updates; watch for patches related to PvP and claim
management.

### **Enabling /wild or /f wilderness Command**

**Question:** How do I enable the /wild or /f wilderness command?

**Solution:**

- **Default Enabled:** The `/f wilderness` command is enabled by default. Use `/f wilderness` to teleport players to a
  random location in the wilderness.
- **Config Check:** Ensure the wilderness module is set to `true` in the `config.yml` under the `modules` section.

**Key takeaway:** Wilderness should be enabled by default, but check the `config.yml` if you encounter issues.

---

### **Overclaiming and Sieging**

**Question:** Is overclaiming the same as sieging in ImprovedFactions?

**Solution:**

- **Overclaiming:** Overclaiming occurs when a faction runs out of power and another faction takes their land.
- **Sieging:** A siege starts automatically when a faction tries to overclaim land. No specific command is needed for
  this, and it is based on power differences.

**Key takeaway:** Sieging is the automatic process triggered when overclaiming occurs due to power imbalance.

---

### **Error with /f wilderness Command**

**Issue:** Getting an error when using the `/f wilderness` command.

**Solution:**

1. **Update to Latest Version:** Make sure you are using the latest dev build of the plugin, as this error is commonly
   fixed in newer versions.
2. **World Configuration:** Verify that the world you are trying to use the command in is not blacklisted in the
   `config.yml`.

**Key takeaway:** Update to the latest build and check the world configuration to resolve wilderness command errors.

---

### **Faction Power Settings**

**Question:** How is faction power used, and can I customize it?

**Solution:**

- **Power Usage:** Power is used to claim chunks, and it accumulates over time. By default, power is deducted every
  hour.
- **Customization:** You can modify the power gain and deduction rates in the `power-management` section of the
  `config.yml`. Set `claim-power-cost-growth` and `base-claim-power-cost` to control how much power is required per
  claim.

**Key takeaway:** Power gain and usage can be customized in the `power-management` section of the config.

---

### **GUI Issues in ImprovedFactions**

**Issue:** Some GUIs, like the "Back" button or the "Create New Rank" button, are not working.

**Solution:**

- **Use Commands Instead:** GUIs are currently not fully functional, and it’s recommended to use commands for actions
  like creating ranks or navigating.
- **Disable GUI:** You can disable the GUI in the config if it’s causing issues and rely on command-based management.

**Key takeaway:** GUIs may not work as expected; use commands as an alternative.

---

### **How to Enable Modules in ImprovedFactions**

**Question:** How do I enable specific modules like SafeZone or WarZone in the config?

**Solution:**

- **Modules Section:** In the `config.yml`, find the `modules` section and set any module (e.g., SafeZone, WarZone) to
  `true` to enable it.
- **Reload Server:** After enabling a module, either reload the plugin or restart the server for changes to take effect.

**Key takeaway:** Enable modules in the `config.yml` under the `modules` section, and remember to reload your server.

---

### **Faction Claim Limits**

**Question:** Can I set a limit on the number of chunks a faction can claim without using power?

**Solution:**

- **Power Management:** You cannot fully disable the power system, but you can adjust the power gain and set limits on
  how much land can be claimed through the `claim-power-cost-growth` and `base-claim-power-cost` settings in the config.

**Key takeaway:** Use the `power-management` settings in the config to adjust claim limits and power costs.

### **Migrating from Factions3 to ImprovedFactions**

**Question:** Is it possible to migrate factions from Factions3 to ImprovedFactions?

**Solution:**

- **Manual Migration via MySQL:** If Factions3 uses MySQL, you can write a migration script to move data over to
  ImprovedFactions. First, create a faction in ImprovedFactions and compare the database structures. Then, align the old
  data with the new system using your script.
- **No Ready-made Solution:** Unfortunately, there is no ready-made migration tool, so manual migration or a custom
  script is necessary.

**Key takeaway:** You can migrate using MySQL and a custom script to align Factions3 data with ImprovedFactions.

---

### **Switching to Protection-Only Play Style**

**Question:** Can I disable the combat features and use ImprovedFactions just for protection?

**Solution:**

- **Disable Combat in Config:** ImprovedFactions allows you to turn off combat features in the config, making it
  suitable for protection-only servers. Look for the combat-related settings in the config file and disable them.

**Key takeaway:** Yes, you can configure ImprovedFactions to be protection-only by disabling combat features in the
config.

---

### **Choosing Between ImprovedFactions and Towny**

**Question:** Is Towny a better option for protection, and how difficult is the conversion?

**Solution:**

- **Towny for Protection:** If your focus is primarily on land protection, Towny might be a better fit as it was
  designed with that in mind.
- **Migration Difficulty:** Converting from Factions3 to Towny might be more complex than switching to ImprovedFactions,
  as there are no direct migration tools available for either plugin.

**Key takeaway:** Towny is a strong alternative for protection-focused servers, but converting from Factions3 may be
challenging.

### **Add the Faction Name to Player in Chat**

**Question:** How can I display the faction name next to a player’s name in chat using ImprovedFactions and Essentials?

**Solution:**

1. **Required Plugins:**

- **ImprovedFactions** for faction placeholders.
- **PlaceHolderAPI**: Download from [SpigotMC](https://www.spigotmc.org/resources/placeholderapi.6245/).
- A chat formatting plugin like **Essentials**, **ChatInjector**, or **LPC Chat Formatter**.

2. **Using Essentials & ChatInjector:**

- **Step 1:** After installing the required plugins, locate the `Essentials` folder in your server's `plugins`
  directory.
- **Step 2:** Open the `config.yml` file and find the line `format:` that defines how chat messages are displayed.
- **Step 3:** Install **ChatInjector** from [SpigotMC](https://www.spigotmc.org/resources/chatinjector-1-13.81201/).
  Essentials doesn’t natively support PlaceHolderAPI placeholders.
- **Step 4:** Edit the `format:` line by adding the faction name placeholder.
    - Example format: `format: '{faction_name} {displayname}: {message}'`
    - If not using Essentials, placeholders would follow the `%faction_name%` format.

3. **Handling Factionless Players:**

- In the latest versions, the plugin allows setting a default value for factionless players so that it doesn't display a
  blank or placeholder.

**Key takeaway:** Use PlaceHolderAPI and ChatInjector for Essentials, and customize chat formatting with the correct
placeholders.

----

### **How to Delete Other Players' Factions as an Admin**

**Question:** Is it possible for an admin to delete a faction owned by another player?

**Solution:**

- **Command:** Use `/f disband <faction>` to disband and delete a player's faction.

**Key takeaway:** Admins can delete any faction using the `/f disband` command.

---

### **How Does Overclaim Work?**

**Question:** How does the overclaim system function in ImprovedFactions?

**Solution:**

1. **Power Management:** Factions need sufficient power to maintain claims. If a faction's power falls below the
   required amount for their claims, the outer chunks near wilderness will become raidable first.
2. **Sieging:** Another faction can initiate a siege by entering these raidable chunks. If the defending faction does
   not repel the siege, the chunk can be overclaimed by the attacking faction.

**Key takeaway:** Overclaiming begins when a faction cannot maintain its claims due to low power. Chunks near the
wilderness become vulnerable to siege.

---

### **Faction GUI Issues with /f Invite**

**Issue:** When using the GUI interface, players are unable to accept invitations with `/f invite`.

**Solution:**

- **Workaround:** Disable the GUI interface temporarily, as it may cause issues with the `/f invite` command.
  Invitations can still be managed through manual commands.

**Key takeaway:** Temporarily disable the GUI interface if players experience issues accepting faction invites.

---

### **Hostile Mob Spawns in Claims**

**Question:** Can I disable hostile mob spawning in faction claims?

**Solution:**

- **Config Option:** Currently, the plugin does not provide an option to prevent mob spawns directly. However, you can
  disable player damage from mobs in specific zones via the `config.yml` file under the `zones` section.

**Key takeaway:** While you can't disable mob spawns directly, you can stop mobs from dealing damage in certain zones.

---

### **Increasing Power Accumulation Rate**

**Question:** How do I increase the amount of power players gain per hour?

**Solution:**

- **Config Settings:** In `config.yml` under the power module, adjust the following settings:
    - **accumulation-rate:** Defines how often power is accumulated. You can adjust the time unit (e.g., HOURS, MINUTES)
      and value (e.g., 1 for every hour).
    - **accumulation-multiplier:** Controls how much power is gained in each cycle.

**Key takeaway:** Adjust the `accumulation-rate` and `accumulation-multiplier` in the config to control how often and
how much power players gain.

---

### **Factionless Placeholder in Chat**

**Issue:** When a player without a faction sends a message, the faction placeholder shows as empty in chat.

**Solution:**

- **Fix:** ImprovedFactions added support for default values when a player has no faction. Update to the latest version
  and set a default faction name or message for factionless players.

**Key takeaway:** Ensure you're using the latest version of ImprovedFactions to customize placeholder behavior for
factionless players in chat.

---

### **Disabling Combat Features for Protection-Only Play Style**

**Question:** Can I disable combat features in ImprovedFactions to focus only on protection?

**Solution:**

- **Config Settings:** Combat-related features can be disabled in the `config.yml`. This allows for a protection-focused
  experience where factions are used solely for land claiming and defense.

**Key takeaway:** Disable combat-related features in the configuration for a protection-only server setup.

---

### **Unable to Use Basic Faction Commands**

**Issue:** Players are unable to use most `/f` commands in-game except for `/f info` or `/f help`. The error message
states: "The provided arguments do not fit the requirements."

**Solution:**

1. **Check Version Compatibility:** Ensure that you're using the latest stable or dev version of the ImprovedFactions
   plugin. Some older versions may not fully support newer Minecraft builds.
2. **Update Plugin:** If you're on an outdated version, update the plugin to the latest version (e.g., 2.2.0-stable).
3. **Faction Name Restrictions:** When using commands like `/f create`, ensure the faction name contains only alphabetic
   characters (a-z) with no special characters, spaces, or numbers. Consider using underscores (_) instead of spaces.

**Key takeaway:** Update to the latest plugin version and ensure proper naming conventions for factions to resolve
command errors.

---

### **Error when Deleting a Faction**

**Issue:** An error appears when trying to delete a faction, and the deletion doesn’t go through.

**Solution:**

- **Command Confirmation:** When deleting a faction, use the `/f delete confirm` command to confirm the deletion.

**Key takeaway:** Use `/f delete confirm` to properly delete a faction.

---

### **Faction Members Unable to Use /f Home**

**Issue:** Faction members cannot use the `/f home` command even after being given access by the faction owner.

**Solution:**

- **Temporary Fix:** There’s currently a known bug with home permissions. To fix it temporarily, ensure faction members
  have the **set home** permission. This workaround will allow them to use `/f home`.
- **Upcoming Fix:** This issue will be resolved in upcoming plugin builds.

**Key takeaway:** For now, give members the set home permission until the bug is fixed in a future build.

---

### **Plugin Update Notifications**

**Issue:** You receive notifications about a new plugin update on server startup.

**Solution:**

- **Dev Builds:** Dev builds are available that may fix existing issues, but these are not always marked as stable.
  Check the plugin’s GitHub page or development channel for the latest updates and fixes.

**Key takeaway:** You can download the latest dev builds for new fixes, but stable builds may still be in development.

### **How to Disable "Set Home" and "Home" Commands**

**Question:** How do I disable the `/f home` and `/f sethome` commands in ImprovedFactions?

**Solution:**

- **Disable Home Module:** To disable the home and sethome functionality, navigate to the `config.yml` file of the
  plugin and disable the **home module** by setting it to `false`.

**Key takeaway:** You can turn off home-related commands by disabling the home module in the configuration file.

---

### **Permissions List for ImprovedFactions**

**Question:** Where can I find the permissions list for ImprovedFactions?

**Solution:**

- **Use LuckPerms:** While there isn't a detailed list available online, you can see all permissions as you type them in
  LuckPerms. Permissions for ImprovedFactions typically begin with **`faction.`**.

**Key takeaway:** Use LuckPerms to access the full list of permissions dynamically.

---

### **Declaring Wars and Forming Alliances**

**Question:** Can I declare wars, make peace, or form alliances between factions?

**Solution:**

- **Alliance Module:** The **alliance module** is in development and will soon allow alliances between factions.
- **War and Peace Commands:** Commands for declaring war and making peace are planned to be added in future updates.

**Key takeaway:** The alliance system and war/peace features are under development and will be added in future releases.

---

### **Error When Deleting a Faction**

**Issue:** An error message appears when trying to delete a faction.

**Solution:**

- **Command Confirmation:** Use the `/f delete confirm` command to properly delete the faction.

**Key takeaway:** Always use `/f delete confirm` to delete factions.

---

### **Unable to Use /f Home for Faction Members**

**Issue:** Faction members cannot use the `/f home` command, but the owner can.

**Solution:**

- **Permission Issue:** Ensure that faction members have the **home teleport** permission. If the issue persists, the
  set home and teleport home permissions may be mixed up.
- **Temporary Fix:** Assign faction members the **set home** permission as a temporary solution. This issue is fixed in
  newer builds.

**Key takeaway:** Ensure that members have the correct permissions for home commands or use the temporary workaround.

---

### **Regex for Faction Names**

**Question:** How do I allow faction names with words like "Empire" or "Republic"?

**Solution:**

1. **Modify Regex in Config:** You can modify the faction name regex in the `config.yml`. Change it to allow spaces or
   other characters by updating the regex pattern to `[a-zA-Z0-9''-]*`. However, spaces are not recommended; use `-` or
   `_` instead.

**Key takeaway:** Adjust the regex in the configuration file to allow more flexible naming conventions.

### **Overclaiming When a Faction is Offline**

**Question:** Can players overclaim a faction when the faction is offline or if only one player is online?

**Solution:**

- **Overclaiming While Offline:** Yes, players can overclaim a faction even when it is offline. Overclaiming is
  determined by the faction's power and land claims, not the online status of the members.
- **Single Player Online:** The number of online players does not affect overclaiming. If a faction’s power is too low
  to maintain its claims, other players can overclaim the land.

**Key takeaway:** Overclaiming is based on power and land balance, not the online status of faction members.

---

### **Disabling Siege Overclaiming**

**Question:** Is it possible to disable overclaiming through siege mechanics but still allow overclaiming when a faction
has more land than power?

**Solution:**

- **Disable Siege Overclaiming:** You can turn off siege-based overclaiming by modifying the config to disable this
  feature while keeping power-based overclaiming active.
- **Config Settings:** Ensure the config for siege overclaiming is set to false while allowing power and land management
  overclaiming to remain enabled.

**Key takeaway:** You can disable siege overclaiming without affecting power-based overclaim mechanics.

---

### **Inactive Player Time and Power Accumulation**

**Question:** How is inactive player time calculated for power accumulation, and what unit is used for this?

**Solution:**

- **Config Settings:** The time unit for inactive player status can be configured in the `config.yml`. The default value
  often measures inactivity in days.
- **Power Formula:** Ensure that the time threshold in the config is set correctly for how long a player must be
  inactive before affecting faction power accumulation.

**Key takeaway:** Inactivity is usually measured in days, and correct configuration of inactive time is essential for
accurate power accumulation.

---

### **Basic Commands Not Working**

**Issue:** Players can’t use basic faction commands like /f create on Paper 1.20.1.

**Solution:**

- **Version Compatibility:** Ensure you are using the correct version of the plugin. Version 2.2.0 or later is
  recommended for compatibility with Minecraft 1.20.1.
- **Allowed Characters in Faction Names:** Faction names only support alphanumeric characters (A-Z, 0-9) by default. No
  special characters or spaces are allowed unless modified in the config.

**Key takeaway:** Update to the latest plugin version and check character restrictions in faction names.

---

### **Fix for /f home Command**

**Issue:** Faction members can't use the /f home command even after being given the permission.

**Solution:**

- **Permissions Fix:** Currently, the /f home permissions are bugged. A workaround is to temporarily give faction
  members the set home permission, which will also grant access to the /f home command.
- **Future Fix:** This issue is fixed in newer builds, but it might still occur in certain versions.

**Key takeaway:** Assign set home permission to faction members as a temporary fix until a stable update is available.

### **Error While Inviting Someone to a Faction**

**Issue:** Receiving an error message when inviting a player to a faction, with the error indicating an unhandled
exception related to the command execution.

**Solution:**

- **Issue with Colorized Player Names:** This error may be caused if player names are colorized or include special
  characters. Ensure player names do not contain unsupported characters.
- **Alternative Fix:** Use the provided updated version of the plugin, which has addressed this issue.
- **Workaround for GUI Issues:** If the GUI is malfunctioning, you can bypass the issue using the command
  `/f inviteaccept <player>` to manually accept faction invitations.

**Key takeaway:** Ensure player names follow standard formatting, and use alternative commands to work around GUI issues
if necessary.

---

### **Cannot Accept Faction Invite via GUI**

**Issue:** A player cannot accept a faction invite through the GUI, with the interface being empty or not responding
correctly.

**Solution:**

- **Use the Command Alternative:** If the GUI for accepting invites is bugged, the player can use the command
  `/f inviteaccept <player>` to join the faction manually.
- **Check Console for Errors:** If the GUI continues to malfunction, check the server console for errors such as
  “invalid component” or missing faction data.

**Key takeaway:** If the GUI is malfunctioning, use the command alternative to accept invites and check the console for
potential plugin issues.

---

### **Fix for "Set Home" and "Home" Commands**

**Issue:** Faction members are unable to use the `/f home` command, even after being given the necessary permissions.

**Solution:**

- **Temporary Workaround:** A temporary fix is to give faction members the "set home" permission, which will also allow
  them to use the `/f home` command.
- **Future Fix:** This issue is planned to be resolved in newer versions of the plugin.

**Key takeaway:** Granting the "set home" permission to faction members serves as a workaround for the broken `/f home`
permission system.

---

### **Inviting Players to a Faction - GUI Bug**

**Issue:** The invite GUI is empty, showing only "back" and "next" buttons, but no actionable options to accept the
faction invite.

**Solution:**

- **Command-Based Invite Acceptance:** Use the `/f inviteaccept <player>` command as an alternative to the GUI.
- **Server Console Check:** Check the console for any errors related to GUI components or missing data that might be
  causing the issue. Consider updating or fixing GUI Engine if necessary.

**Key takeaway:** If the invite GUI is not functioning properly, use command-based invites and check the console for
error logs to troubleshoot.

### **Peaceful Factions & PvP Protection**

**Question:** Can peaceful factions avoid PvP and base destruction, but still participate in collaboration with combat
factions?

**Solution:**

- **Peaceful Factions:** You can configure the plugin to allow factions to remain peaceful, avoiding PvP and base
  destruction. This is managed by adjusting the power loss on death and raid settings in the config file.
- **Customizable Playstyles:** You can control whether factions can engage in wars or stay neutral, ensuring they are
  protected from raids and PvP if they prefer a non-combat playstyle.

**Key takeaway:** The plugin allows flexible settings for peaceful factions to avoid PvP and destruction while still
participating in server activities.

---

### **Power System Overview**

**Question:** How does the power system work, and how do faction members gain or lose power?

**Solution:**

- **Gaining Power:** Faction members gain power through playtime. The more time spent online, the more power is
  accumulated.
- **Losing Power:** Power is lost when players die. This affects the faction's overall power and their ability to
  maintain claimed land.
- **Wars & Power:** Currently, there is no mechanic to directly add or subtract power based on winning or losing wars,
  but this is a planned feature for future versions of the plugin.

**Key takeaway:** Power is primarily gained through playtime and lost upon death. Future updates may introduce power
changes based on war outcomes.

---

### **Configuring Faction Power for Wars**

**Question:** Is it possible to configure wars to affect faction power?

**Solution:**

- **Current Status:** There is no built-in functionality for wars to directly affect faction power in the current
  version (v2.0.0+), but this feature was present in earlier versions and is planned for future releases.
- **Upcoming Feature:** You can follow development progress or suggest new features on the plugin’s GitHub project
  board .

**Key takeaway:** While this feature is not available in the current version, it is planned for future updates.

### **Setting Up Permissions for ImprovedFactions Using LuckPerms**

**Step-by-Step Guide:**

1. **Install LuckPerms:**

- First, download and install LuckPerms from [here](https://www.spigotmc.org/resources/luckperms.28140/).
- Ensure it’s added to your server's plugins folder and then restart your server.

2. **Open LuckPerms Editor:**

- In your server, type `/lp editor` (requires admin or OP permissions).
- A link will be provided in chat. Click it to open the LuckPerms web editor.

3. **Select a Group:**

- In the LuckPerms editor, select the group you want to edit (e.g., default for all players, or specific groups like
  Admin or Moderator).

4. **Adding Permissions:**

- For ImprovedFactions, you can add these permissions:
    - **Basic Commands for Players:** `faction.commands.*`
- To restrict certain commands, like admin commands, you can **deny** the following:
    - `faction.commands.zone`
    - `faction.commands.admin`
    - `faction.commands.plugin`
    - `faction.commands.config`
    - `faction.commands.extension`

- In the editor:
    - Use the large red field to input permissions (e.g., `faction.commands.*`).
    - In the smaller field next to it, choose "true" to allow, or "false" to deny.

5. **Apply and Save Changes:**

- Once you have configured the necessary permissions, press the **green Apply button** at the top of the editor.
- This will give you a command to paste back into your Minecraft server to apply the changes.

6. **Test Permissions:**

- Players should now be able to use the ImprovedFactions plugin based on the permissions you've set up.

By following these steps, your server should now have ImprovedFactions permissions configured correctly for your
players.