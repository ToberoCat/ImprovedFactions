---
id: transferowner
title: Transferowner
---

# Transferowner

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.transferowner.description`

## Usage

### For Player 👤

```bash
/transferowner <targetUser> [confirm]
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| targetUser | FactionUser | Yes | `base.commands.transferowner.arguments.targetUser.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.transferowner`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `ownershipTransferred` | `base.commands.transferowner.ownership-transferred` |
| `notOwner` | `base.commands.transferowner.not-owner` |
| `notInFaction` | `base.commands.transferowner.not-in-faction` |
| `missingRequiredArgument` | `base.commands.transferowner.missing-required-argument` |
| `confirmationNeeded` | `base.commands.transferowner.confirmation-needed` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
