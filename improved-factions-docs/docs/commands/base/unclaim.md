---
id: unclaim
title: Unclaim
---

# Unclaim

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.unclaim.description`

## Usage

### For Player 👤

```bash
/unclaim [radius]
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| radius | Int | No | `base.commands.unclaim.arguments.radius.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.unclaim`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `unclaimed` | `base.commands.unclaim.unclaimed` |
| `unclaimedRadius` | `base.commands.unclaim.unclaimed-radius` |
| `unclaimError` | `base.commands.unclaim.unclaim-error` |
| `notInFaction` | `base.commands.unclaim.not-in-faction` |
| `noPermission` | `base.commands.unclaim.no-permission` |
| `missingRequiredArgument` | `base.commands.unclaim.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
