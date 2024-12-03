---
id: rank set
title: Rank set
---

# Rank set

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.rank-set.description`

## Usage

### For Player 👤

```bash
/rank set <rank> <permission> <value>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| rank | FactionRank | Yes | `base.commands.rank-set.arguments.rank.description` |
| permission | String | Yes | `base.commands.rank-set.arguments.permission.description` |
| value | Boolean | Yes | `base.commands.rank-set.arguments.value.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.rank-set`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `permissionUpdated` | `base.commands.rank-set.permission-updated` |
| `noPermission` | `base.commands.rank-set.no-permission` |
| `notInFaction` | `base.commands.rank-set.not-in-faction` |
| `missingRequiredArgument` | `base.commands.rank-set.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
