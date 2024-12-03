---
id: admin claim
title: Admin claim
---

# Admin claim

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.admin-claim.description`

## Usage

### For Player 👤

```bash
/admin claim <faction>
```

### For Console 🖥️

```bash
/admin claim <faction> <world> <blockX> <blockZ>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| faction | Faction | Yes | `base.commands.admin-claim.arguments.faction.description` |

</details>

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| faction | Faction | Yes | `base.commands.admin-claim.arguments.faction.description` |
| world | World | Yes | `base.commands.admin-claim.arguments.world.description` |
| blockX | Int | Yes | `base.commands.admin-claim.arguments.blockX.description` |
| blockZ | Int | Yes | `base.commands.admin-claim.arguments.blockZ.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.admin-claim`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `factionClaimed` | `base.commands.admin-claim.faction-claimed` |
| `claimError` | `base.commands.admin-claim.claim-error` |
| `factionNotFound` | `base.commands.admin-claim.faction-not-found` |
| `noPermission` | `base.commands.admin-claim.no-permission` |
| `missingRequiredArgument` | `base.commands.admin-claim.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
