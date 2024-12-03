---
id: info
title: Info
---

# Info

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.info.description`

## Usage

### For Player 👤

```bash
/info <faction>
```

### For Console 🖥️

```bash
/info <faction>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| faction | Faction | Yes | `base.commands.info.arguments.faction.description` |

</details>

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| faction | Faction | Yes | `base.commands.info.arguments.faction.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.info`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `infoHeader` | `base.commands.info.info-header` |
| `infoDetail` | `base.commands.info.info-detail` |
| `noFactionFound` | `base.commands.info.no-faction-found` |
| `missingRequiredArgument` | `base.commands.info.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
