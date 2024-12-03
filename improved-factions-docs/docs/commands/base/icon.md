---
id: icon
title: Icon
---

# Icon

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.icon.description`

## Usage

### For Player 👤

```bash
/icon
```

### For Console 🖥️

```bash
/icon <target>
```

## Parameters

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| target | Player | Yes | `base.commands.icon.arguments.target.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.icon`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `setIconSuccess` | `base.commands.icon.set-icon-success` |
| `invalidIcon` | `base.commands.icon.invalid-icon` |
| `factionNeeded` | `base.commands.icon.faction-needed` |
| `notFactionOwner` | `base.commands.icon.not-faction-owner` |
| `noPermission` | `base.commands.icon.no-permission` |
| `missingRequiredArgument` | `base.commands.icon.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
