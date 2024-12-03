---
id: admin unclaim
title: Admin unclaim
---

# Admin unclaim

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.admin-unclaim.description`

## Usage

### For Player 👤

```bash
/admin unclaim <faction>
```

### For Console 🖥️

```bash
/admin unclaim <faction> <world> <blockX> <blockZ>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| faction | Faction | Yes | `base.commands.admin-unclaim.arguments.faction.description` |

</details>

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| faction | Faction | Yes | `base.commands.admin-unclaim.arguments.faction.description` |
| world | World | Yes | `base.commands.admin-unclaim.arguments.world.description` |
| blockX | Int | Yes | `base.commands.admin-unclaim.arguments.blockX.description` |
| blockZ | Int | Yes | `base.commands.admin-unclaim.arguments.blockZ.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.admin-unclaim`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `factionUnclaimed` | `base.commands.admin-unclaim.faction-unclaimed` |
| `unclaimError` | `base.commands.admin-unclaim.unclaim-error` |
| `factionNotFound` | `base.commands.admin-unclaim.faction-not-found` |
| `noPermission` | `base.commands.admin-unclaim.no-permission` |
| `missingRequiredArgument` | `base.commands.admin-unclaim.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
