Luckperms is a free to use permission editor. It allows you to assign groups for a player on your server.
ImprovedFactions permissions aren't setup to be server ready, that's why I'm going to explain to you how to set them up correctly.

At first, you need Luckperms (https://www.spigotmc.org/resources/luckperms.28140/).

When you have luckperms installed on your server, enter the command: /lp editor (Note: You have to be op to do this).
Then you have to click the link that you received.

This will open the luckperms web editor, where you can create  & manage permissions.

Selecting the group
When adding the permissions, you need to select the group. If you want everybody to have access to the commands, use the default group.

Adding the permissions
That all commands are setup correctly, you have to allow
faction.commands.*

When all commands are allowed, you should remove:
faction.commands.zone
faction.commands.admin
faction.commands.plugin
faction.commands.config
faction.commands.extension


The large red field tells is the permission input, while the smaller one allows you to set something to allowed / denied.
True means it's allowed, while false means it's denied

Save
When you added all permissions, you can press the green apply button. Past the command into minecraft.



After you have done these steps, your players should be able to use the plugin properly