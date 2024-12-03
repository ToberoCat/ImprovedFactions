---
id: kick
title: Kick
---

# Kick

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.kick.description`

## Usage

### For Player 👤

```bash
/kick [target]
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| target | FactionUser | No | `base.commands.kick.arguments.target.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.kick`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `kickedPlayer` | `base.commands.kick.kicked-player` |
| `notInFaction` | `base.commands.kick.not-in-faction` |
| `noPermission` | `base.commands.kick.no-permission` |
| `invalidMember` | `base.commands.kick.invalid-member` |
| `missingRequiredArgument` | `base.commands.kick.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
