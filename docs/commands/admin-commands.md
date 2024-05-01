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

