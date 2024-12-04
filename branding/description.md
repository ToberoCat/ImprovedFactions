# Improved Factions ✨ A modern factions plugin ✨ [1.18-1.20.1]

![Icon](https://github.com/ToberoCat/ImprovedFactions_new/blob/main/branding/banners/full-scale-banner.png?raw=true)

[![ToberoCat Improved Factions](https://img.shields.io/static/v1?label=ToberoCat&message=ImprovedFactions&color=%23FEDD58&logo=github)](https://github.com/ToberoCat/ImprovedFactions_new)
[![Donate via PayPal](https://img.shields.io/badge/Donate-PayPal-green.svg?logo=paypal&style=flat-square)](https://www.paypal.com/donate/?hosted_button_id=QVJDUKN2VJ6BE)
[![Latest Release](https://img.shields.io/github/release/ToberoCat/ImprovedFactions_new?include_prereleases=&sort=semver&color=%23FEDD58)](https://github.com/ToberoCat/GuiEngine/releases/)

## Introducing Improved Factions ✨

Improved factions is a modern factions plugin. It is designed to provide a seamless experience for both server owners
and players. With a wide range of features, Improved Factions offers a unique and customizable factions experience.
Whether you're a server owner looking to enhance your server's factions gameplay or a player seeking a new and exciting
factions experience, Improved Factions has something for everyone.

## What is Factions? 🏰

Factions is a popular game mode in Minecraft that allows players to create and join communities, also called factions to
claim land and engage in PvP battles. Players can form alliances, build bases, and conquer territory to become the most
powerful faction on the server. Factions is a competitive and strategic game mode that requires teamwork, communication,
and skill to succeed.

### Why Choose Improved Factions?

This plugin is designed to enhance the factions experience for both server owners and players. When first installed, the
plugin will be setup to work out of the box. However, it is highly customizable, allowing server owners to tailor the
factions experience to their liking. With a wide range of features and options, Improved Factions offers a unique and
engaging factions experience that is sure to keep players coming back for more.

#### Features

- Extremly customizable
- Papi support
- Easily enable or disable features
- Dynmap support
- Advanced power system
- Player customizable factions
- Custom permission management
- Advanced claim system (raids, etc)
- Integrated wilderness
- Fully translatable
- Permissions
- And much more!

## Get Started Today! 🚀

Ready to take your factions experience to the next level? Download Improved Factions today and start customizing your
factions server! Whether you're a server owner looking to enhance your server's factions gameplay or a player seeking a
new and exciting factions experience, Improved Factions has something for everyone. With a wide range of features and
options, Improved Factions offers a unique and engaging factions experience that is sure to keep players coming back for
more.

## Configuration 🛠️

As already mentioned, Improved Factions is highly customizable. You can take a look at the config file for the
plugin [here](https://github.com/ToberoCat/ImprovedFactions_new/blob/main/improved-factions-base/src/main/resources/config.yml).
The config file is well documented and easy to understand, making it simple to customize the factions experience to your
liking.
For more advanced configuration options, you can also take a look at
the [docs](https://github.com/ToberoCat/ImprovedFactions_new/tree/main/docs), going into more detail about the plugin's
features and how to customize them.

## BStats 📊

This plugin uses bstats to collect very minimalistic data about the plugin. Visit
the [plugin's bstat](https://bstats.org/plugin/bukkit/ImprovedFactions/14810) site to view the metrics being collected
![BStats Signature](https://bstats.org/signatures/bukkit/ImprovedFactions.svg)

## The provided Modules 🧩

### Core

The core module is the main module of the plugin. It provides all the basic functionalities of the plugin. This includes the management of factions, claiming, inviting, and kicking players. The core module is required for the plugin to function properly, therefore it can't be disabled.

![Rank gui](https://github.com/ToberoCat/ImprovedFactions_new/blob/main/branding/media/rank-gui.png?raw=true)

### Wilderness

The wilderness module is an optional module that allows players to teleport to the wilderness. This module is useful for players who want to explore the world or gather resources far away from faction claims. The wilderness module can be enabled or disabled in the config file.

![Wilderness Teleport gif](https://github.com/ToberoCat/ImprovedFactions_new/blob/main/branding/media/wilderness.gif?raw=true)

### Dynmap

The dynmap module is an optional module that integrates Improved Factions with the Dynmap plugin. This module allows players to view faction claims on the Dynmap, making it easier to see which areas are claimed by factions. The dynmap module can be enabled or disabled in the config file.

![Dynmap Integration](https://github.com/ToberoCat/ImprovedFactions_new/blob/main/branding/media/dynmap.png?raw=true)

### Home

The home module is an optional module that allows players to set and teleport to their faction's home location. This module is useful for players who want to quickly return to their faction's base or other important locations. The home module can be enabled or disabled in the config file.

### Power & Raid

The power & raid module is an optional module that adds an advanced power system to factions. This module allows factions to gain or lose power based on various factors, such as player deaths, kills, and territory control. Factions with low power are vulnerable to raids, making it important for players to manage their power carefully. The power & raid module can be enabled or disabled in the config file.


#### Power Dynamics

Factions gain power through online members, used for claiming more chunks. Each claimed chunk consumes power over time. Faction power is influenced by member actions:

- **Member Addition:** Power increases based on a configurable base value, favoring smaller factions.

- **Online Member Accumulation:** Members contribute their full power potential, with a bonus for simultaneous faction activity.

- **Claiming Chunks:** Each claimed chunk consumes power, with costs increasing based on the faction's total chunk count.

- **Ally Power Sharing:** Factions share a fraction of accumulated power with allies.

#### Overclaiming & Raiding

- **Unprotected Claims:** Claims become raidable when a faction can't afford the base claim power cost.

- **Overclaiming Process:** Requires an enemy faction member to stay in the claimed chunk for a set time.

- **Allied Assistance:** Allies can offer help by sending power.

Refer to the attached screenshot for a visual reference, and see `power-raids.md` for detailed implementation information.

![Power System](https://github.com/ToberoCat/ImprovedFactions_new/blob/main/branding/media/power_raid.png?raw=true)

## Compatible Plugins

The following plugins might be of interest for you, when dealing with factions

- [Papi](https://www.spigotmc.org/resources/placeholderapi.6245/) allows for placeholders. View
  the [placeholder list](https://github.com/ToberoCat/ImprovedFactions_new/blob/main/docs/placeholders.md) for more
  infos about the existing placeholders
- [GuiEngine](https://modrinth.com/plugin/guiengine) This plugin is needed for the guis to work. It is a powerful gui
  engine, that allows for easy gui creation
- [Command Aliases](https://www.spigotmc.org/resources/custom-command-aliases.105037/) Adds the ability to create custom
  commands - This is a perfect fit for this plugin, if you plan to open guis via commands

## Support 📞

If you have any questions or need help with the plugin, feel free to join
the [Discord server](https://discord.com/invite/VmSbFNZejz). The community is friendly and helpful, and you're sure to
find the support you need.

## Contributing 🤝

If you're interested in contributing to the plugin, feel free to fork the repository and submit a pull request. The
plugin is open source, and contributions are always welcome. Whether you're a developer looking to add new features or
fix bugs, or a player with suggestions for improving the plugin, your contributions are appreciated.

## Donations 💰

If you enjoy using the plugin and would like to support its development, consider making a donation. Your donations help
cover the costs of maintaining the plugin and allow me to continue adding new features and improvements. You can donate
via PayPal by clicking the button below.

[![Donate via PayPal](https://img.shields.io/badge/Donate-PayPal-green.svg?logo=paypal&style=flat-square)](https://www.paypal.com/donate/?hosted_button_id=QVJDUKN2VJ6BE)

## Links

- [GitHub](https://github.com/ToberoCat/ImprovedFactions_new/issues)
- [Wiki](https://github.com/ToberoCat/ImprovedFactions_new/tree/main/docs)
- [Discord](https://discord.com/invite/VmSbFNZejz)

