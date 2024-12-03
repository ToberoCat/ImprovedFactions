---
id: rank edit
title: Rank edit
---

# Rank edit

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.rank-edit.description`

## Usage

### For Player 👤

```bash
/rank edit <rank>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| rank | FactionRank | Yes | `base.commands.rank-edit.arguments.rank.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.rank-edit`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `editPermissionsHeader` | `base.commands.rank-edit.edit-permissions-header` |
| `permissionDetails` | `base.commands.rank-edit.permission-details` |
| `invalidRank` | `base.commands.rank-edit.invalid-rank` |
| `notInFaction` | `base.commands.rank-edit.not-in-faction` |
| `rankEdited` | `base.commands.rank-edit.rank-edited` |
| `missingRequiredArgument` | `base.commands.rank-edit.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
