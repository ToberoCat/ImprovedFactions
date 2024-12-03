---
id: create
title: Create
---

# Create

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.create.description`

## Usage

### For Player 👤

```bash
/create <name>
```

### For Console 🖥️

```bash
/create <owner> <name>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| name | String | Yes | `base.commands.create.arguments.name.description` |

</details>

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| owner | OfflinePlayer | Yes | `base.commands.create.arguments.owner.description` |
| name | String | Yes | `base.commands.create.arguments.name.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.create`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `createdFaction` | `base.commands.create.created-faction` |
| `factionAlreadyExists` | `base.commands.create.faction-already-exists` |
| `alreadyInFaction` | `base.commands.create.already-in-faction` |
| `invalidName` | `base.commands.create.invalid-name` |
| `nameTooLong` | `base.commands.create.name-too-long` |
| `missingRequiredArgument` | `base.commands.create.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
