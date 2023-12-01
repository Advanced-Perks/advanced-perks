![Logo](images/logo.png)
For support please refere to the support discord:  [https://discord.gg/nEAuWF4Btw](https://discord.gg/nEAuWF4Btw)



# What is Advanced Perks?

Advanced Perks is a robust perk system designed to enrich the player experience.
It offers distinctive unlockable abilities, each with a unique impact on the player—from mitigating fall damage to enabling night vision.
Players can individually activate or deactivate perks making it possible to provide perks only to specific player.
They are granted through customizable permissions or by using the command /perks buy.
Additionally, a Gui is in development to streamline the perk unlocking process in the future.



# Features

-   Over 15 Perks with an upcoming option to create custom perks
-   Extensively customizable perk system
-   Complete customization of all system messages
-   Developer API and Events for seamless integration into your projects
-   PlaceholderAPI Support
-   Persistent saving of perk data for every player, both locally and to a database
-   User-friendly commands for efficient operation of the perk system
-   Highly customizable GUI for enabling and disabling perks
-   Integration support for LuckPerms
-   Full economy support through Vault



# Requirements

Spigot: Version 1.16.5 or newer
Fork Compatibility: Any fork should be supported
Java: Version 17 or newer
Economy Integration: Requires Vault and any Vault-compatible economy plugin.



# Perk Gui
![Perk Gui](images/gui.png)

# Commands
**/perks buy <perk_identifier>**
**Permission:**  advancedperks.command.buy
When Vault and a Vault-compatible Economy Plugin are installed alongside this plugin and a price is set in the perks.yml file, the player can buy the perk to activate it, even if they don't have the required permission for it.

**/perks disable <perk_identifier> [player]**
**Permission:**  advancedperks.command.disable
This command forcefully disables the specified perk for the command sender or a specified player.

**/perks enable <perk_identifier> [player]**
**Permission:**  advancedperks.command.enable
This command forcefully enables the specified perk for the command sender or a specified player, bypassing all checks like permissions or disallowed worlds.

**/perks info [player]**
**Permission:**  advancedperks.command.info
This command lists all enabled and bought perks for the command sender or a specified player.

# Placeholder Api

**%advancedperks.use_<perk_identifier>%**
Indicates whether the player has permission to use the specified perk.
returns either true or false[/QUOTE]

**%advancedperks.enabled<perk_identifier>%**
Indicates whether the player has the specific perk enabled.
returns either true or false[/QUOTE]