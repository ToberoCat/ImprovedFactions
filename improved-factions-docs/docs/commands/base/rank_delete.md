---
id: rank delete
title: Rank delete
---

# Rank delete

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.rank-delete.description`

## Usage

### For Player 👤

```bash
/rank delete <rank> [fallbackRank]
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| rank | FactionRank | Yes | `base.commands.rank-delete.arguments.rank.description` |
| fallbackRank | FactionRank | No | `base.commands.rank-delete.arguments.fallbackRank.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.rank-delete`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `noPermission` | `base.commands.rank-delete.no-permission` |
| `rankDeleted` | `base.commands.rank-delete.rank-deleted` |
| `rankIsDefault` | `base.commands.rank-delete.rank-is-default` |
| `notInFaction` | `base.commands.rank-delete.not-in-faction` |
| `invalidRank` | `base.commands.rank-delete.invalid-rank` |
| `missingRequiredArgument` | `base.commands.rank-delete.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
