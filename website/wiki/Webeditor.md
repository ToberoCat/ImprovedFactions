Customize your guis to stand out from other minecraft servers.
This will be possible with the new gui webeditor coming with v2.0.0. If you want to try it out right now, you can.

Accessing the gui editor


How to access it from minecraft
When you use a dev build 5 or later you can simply type /f admin gui edit <gui-id>. This will send you a link that you have to click. When you confirmed you want to open it, you can start editing.

How to access it without minecraft
https://toberocat.github.io/ImprovedFactions_new/website/editor/
When you clicked the link above you will be redirected to a page asking you to input a gui file. You can find your gui files in plugins/ImprovedFactions/guis. If you haven't used a dev build yet (or the latest one), you can use the sample posted below
The toolbox

When you managed to get the gui view, you are now presented with a bunch of options.
Here is a quick overview:
- The checkbox in the headline - This allows you to toggle between light and darkmode
- The share button - Allows you to receive your current gui as a url. When pressed the url will be pasted into your clipboard. Note:  The url always exceeds discord 2k character limit, therefore if you want to share your progress with your friends, neither use a link shortener or just export it into a file which can be sent via discord
- The export button - This will export the gui into a format recognisable by improved factions. You can also export it to save your work and resume later. The format can be read by the webeditor and the plugin. The file you receive should replace the old one, you chose to format. If you used the ingame command, go to plugins/ImprovedFactions/guis/<gui-id>.gui and replace this file and reload your server
- The rows button - You can change the amout of rows your gui has. Min is 1 and max is 6
- The item states - As soon as you clicked onto an item in the gui, you can see the item states. This will display the different states a item can have in a gui. Each state can have it's own icon as well as a id. The id is being used in the lang files to get the item title, lore, etc. The DefaultState is whenever your item has no other state, basically with the current guis the normal display type.
- The Gui view - By dragging any item onto a slot, it will change it's item type. By clicking on a item you can load the items states and modify it.
- Item view - The item view has a search bar and a scrollable list of all items. These items are for the latest version of the game. When you aren't using the latest one, thee items don't exist, so avoid using them, else your gui won't load anymore within minecraft