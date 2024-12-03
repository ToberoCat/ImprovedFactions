---
id: war
title: War
---

# War

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`relations.commands.war.description`

## Usage

### For Player 👤

```bash
/war <targetFaction>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| targetFaction | Faction | Yes | `relations.commands.war.arguments.targetFaction.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.war`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `warDeclared` | `relations.commands.war.war-declared` |
| `notInFaction` | `relations.commands.war.not-in-faction` |
| `noPermission` | `relations.commands.war.no-permission` |
| `missingRequiredArgument` | `relations.commands.war.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
