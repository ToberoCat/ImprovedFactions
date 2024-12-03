---
id: power set
title: Power set
---

# Power set

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`power-raids.commands.power-set.description`

## Usage

### For Console 🖥️

```bash
/power set <powerType> <faction> <power>
```

## Parameters

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| powerType | PowerType | Yes | `power-raids.commands.power-set.arguments.powerType.description` |
| faction | Faction | Yes | `power-raids.commands.power-set.arguments.faction.description` |
| power | Int | Yes | `power-raids.commands.power-set.arguments.power.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.power-set`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `powerSet` | `power-raids.commands.power-set.power-set` |
| `invalidArguments` | `power-raids.commands.power-set.invalid-arguments` |
| `factionNotFound` | `power-raids.commands.power-set.faction-not-found` |
| `missingRequiredArgument` | `power-raids.commands.power-set.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
