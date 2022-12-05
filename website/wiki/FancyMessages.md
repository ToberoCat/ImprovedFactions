With this feature your servers messages won't look boring anymore and make your server be something special for your playerbase.

Why use fancy messages
Note for dev build 5: Only the browser message supports it


Currently, fancy messages are optional, but once the v2.0.0 is out of dev build, it will be forced to be used with all messages you can find the lang file. Once you got the twist, you can make good looking nested messages without knowledge of programming.

The basics
Fancy messages rely on message blocks. Each block has it's own ruleset.
A single block gets defined by {}. This block is incomplete, because every block requires a text message.
Example block: {text: §7A message #ff00AAthat also supports hex colours when above §e1.6}.

These blocks support Papi (When installed, local IF placeholders and global IF placeholders (The global ones replace papi when it's not present)).
Another valid block would be: {text:You got invited by {inviter_faction_displayname}. Your rank: %vault_rank%}

The extra modules
When you just need a simple text, you don't even need a block. Just type it as usual. All colors are supported as well as the placeholders. Blocks get important when you start using text types.

Block modules

hover - Example: You got invited by §e{text: {inviter_faction_displayname}; hover: §eRegisty: {inviter_faction_registry}}
url - Example: {text:Click here to go to youtube; url:https://youtube.com/}
suggest_command - Example: {text: Create a faction; suggest_command: /f create your_name}
command - Example: {text:Click here to say hi in chat; command:Hi everyone!}

Multiple actions
"What? That's not enough?" I've told you that these things are reallly fancy. You can even combine multiple modules into one text block, like this example shows: {text:Multiple attributes; hover:Hovering; suggest_command:This is a command suggestion}
All examples
- {text:&6Sample colored text}
- {text:Hover over the message; hover:&cHello}
- {text:Click here to say hi in chat; command:Hi everyone!}
- {text:Click here to change your gamemode; command:/gamemode creative}
- {text:Click here to go to youtube; url:https://youtube.com/}
- {text:Multiple attributes; hover:Hovering; suggest_command:This is a command suggestion}
- {text:First hover; hover:First} {text:Second hover; hover:Second} {text:Broadcast; command:/broadcast Hello!}