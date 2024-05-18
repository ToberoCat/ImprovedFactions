# Admin commands

## factions reload

Permission: factions.reload

Usage: `/factions reload `

Description: Reload the faction configs

Module: core

| Argument | Description | Required |
| --- | --- | --- |

## factions zone

Permission: factions.zone

Usage: `/factions zone `

Description: Access the admin zone commands

Module: core

| Argument | Description | Required |
| --- | --- | --- |

## factions zone unclaim

Permission: factions.zone.unclaim

Usage: `/factions zone unclaim [radius]`

Description: Unmark a claim from any zone associations

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| radius | Provide a radius for a square | false |

## factions zone claim

Permission: factions.zone.claim

Usage: `/factions zone claim <zone> [radius]`

Description: Mark a claim as property of this zone

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| zone | Provide a zone | true |
| radius | Provide a radius for a square | false |

## factions power add

Permission: factions.power.add

Usage: `/factions power add <powertype> <power> <faction>`

Description: Set the power of a faction

Module: power-raids

| Argument | Description | Required |
| --- | --- | --- |
| powertype | Provide a power type. This can be maximum or accumulated | true |
| power | Provide a power value | true |
| faction | Provide a faction | true |

## factions power set

Permission: factions.power.set

Usage: `/factions power set <powertype> <power> <faction>`

Description: Set the power of a faction

Module: power-raids

| Argument | Description | Required |
| --- | --- | --- |
| powertype | Provide a power type. This can be maximum or accumulated | true |
| power | Provide a power value | true |
| faction | Provide a faction | true |

## factions bypass

Permission: factions.bypass

Usage: `/factions bypass [player]`

Description: Add or remove a bypass for a player. A bypass only stays until the next server restart

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| player | Provide a player for this argument | false |

## factions force

Permission: factions.force

Usage: `/factions force `

Description: Force a player to certain faction-related actions

Module: core

| Argument | Description | Required |
| --- | --- | --- |

## factions force unclaim

Permission: factions.force.unclaim

Usage: `/factions force unclaim <faction>`

Description: Force a faction to unclaim a chunk

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| faction | Provide a faction | true |

## factions force join

Permission: factions.force.join

Usage: `/factions force join <player> <faction>`

Description: Force a player to join a faction

Module: core

| Argument | Description | Required |
| --- | --- | --- |
| player | Provide a player for this argument | true |
| faction | Provide a faction | true |

