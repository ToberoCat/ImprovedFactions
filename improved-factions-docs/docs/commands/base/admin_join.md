---
id: admin join
title: Admin join
---

# Admin join

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.admin-join.description`

## Usage

### For Console 🖥️

```bash
/admin join <faction> <target> <rank>
```

## Parameters

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| faction | Faction | Yes | `base.commands.admin-join.arguments.faction.description` |
| target | OfflinePlayer | Yes | `base.commands.admin-join.arguments.target.description` |
| rank | FactionRank | Yes | `base.commands.admin-join.arguments.rank.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.admin-join`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `success` | `base.commands.admin-join.success` |
| `playerAlreadyInFaction` | `base.commands.admin-join.player-already-in-faction` |
| `missingRequiredArgument` | `base.commands.admin-join.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
