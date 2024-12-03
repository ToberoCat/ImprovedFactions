---
id: unban
title: Unban
---

# Unban

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.unban.description`

## Usage

### For Player 👤

```bash
/unban <ban>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| ban | FactionBan | Yes | `base.commands.unban.arguments.ban.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.unban`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `unbannedTarget` | `base.commands.unban.unbanned-target` |
| `banNotFound` | `base.commands.unban.ban-not-found` |
| `notInFaction` | `base.commands.unban.not-in-faction` |
| `noPermission` | `base.commands.unban.no-permission` |
| `missingRequiredArgument` | `base.commands.unban.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
