---
id: rank create
title: Rank create
---

# Rank create

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.rank-create.description`

## Usage

### For Player 👤

```bash
/rank create <rankName> <priority>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| rankName | String | Yes | `base.commands.rank-create.arguments.rankName.description` |
| priority | Int | Yes | `base.commands.rank-create.arguments.priority.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.rank-create`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `rankCreated` | `base.commands.rank-create.rank-created` |
| `notInFaction` | `base.commands.rank-create.not-in-faction` |
| `invalidPriority` | `base.commands.rank-create.invalid-priority` |
| `invalidRankName` | `base.commands.rank-create.invalid-rank-name` |
| `noPermission` | `base.commands.rank-create.no-permission` |
| `missingRequiredArgument` | `base.commands.rank-create.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
