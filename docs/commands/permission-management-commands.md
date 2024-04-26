# Permission Management commands

## factions rank

Permission: factions.rank

Usage: `/factions rank `

Description: Access rank commands

Module: core

| Argument | Description | Required |
| --- | --- | --- |

## factions rank set

Permission: factions.rank.set

Usage: `/factions rank set <RankName> <permission> <bool>`

Description: Set a permission for a rank

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| RankName | Assign a rank from your faction | true |
| permission | Provide a permission as an argument | true |
| bool | Provide a boolean value. It can be true or false | true |

## factions rank joinas

Permission: factions.rank.joinas

Usage: `/factions rank joinas <RankName>`

Description: Set the rank new members will join with

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| RankName | Assign a rank from your faction | true |

## factions rank edit

Permission: factions.rank.edit

Usage: `/factions rank edit <RankName>`

Description: Edit permissions for a specific rank via GUI

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| RankName | Assign a rank from your faction | true |

## factions rank create

Permission: factions.rank.create

Usage: `/factions rank create <RankName> <priority>`

Description: Create a new rank for your faction

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| RankName | Provide the name of a faction rank | true |
| priority | Provide a priority. It can manage all ranks below this rank | true |

## factions rank delete

Permission: factions.rank.delete

Usage: `/factions rank delete <RankName> [fallback]`

Description: Delete a rank

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| RankName | Assign a rank from your faction | true |
| fallback | The fallback rank is optional. If provided, all players having this rank will be placed back to this one | false |

## factions rank assign

Permission: factions.rank.assign

Usage: `/factions rank assign <player> <RankName>`

Description: Assign a player to a rank

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| player | Provide a player for this argument | true |
| RankName | Assign a rank from your faction | true |

