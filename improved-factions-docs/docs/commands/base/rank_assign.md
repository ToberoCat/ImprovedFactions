---
id: rank assign
title: Rank assign
---

# Rank assign

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.rank-assign.description`

## Usage

### For Player 👤

```bash
/rank assign <target> <rank>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| target | OfflinePlayer | Yes | `base.commands.rank-assign.arguments.target.description` |
| rank | FactionRank | Yes | `base.commands.rank-assign.arguments.rank.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.rank-assign`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `rankAssigned` | `base.commands.rank-assign.rank-assigned` |
| `notInFaction` | `base.commands.rank-assign.not-in-faction` |
| `notInSameFaction` | `base.commands.rank-assign.not-in-same-faction` |
| `noPermission` | `base.commands.rank-assign.no-permission` |
| `missingRequiredArgument` | `base.commands.rank-assign.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
