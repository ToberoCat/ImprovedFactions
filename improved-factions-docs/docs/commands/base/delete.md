---
id: delete
title: Delete
---

# Delete

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.delete.description`

## Usage

### For Player 👤

```bash
/delete [confirm]
```

### For Console 🖥️

```bash
/delete <target> [confirm]
```

## Parameters

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| target | OfflinePlayer | Yes | `base.commands.delete.arguments.target.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.delete`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `deletedFaction` | `base.commands.delete.deleted-faction` |
| `notInFaction` | `base.commands.delete.not-in-faction` |
| `notFactionOwner` | `base.commands.delete.not-faction-owner` |
| `missingRequiredArgument` | `base.commands.delete.missing-required-argument` |
| `confirmationNeeded` | `base.commands.delete.confirmation-needed` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
