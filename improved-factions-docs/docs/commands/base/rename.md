---
id: rename
title: Rename
---

# Rename

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.rename.description`

## Usage

### For Player 👤

```bash
/rename <newName>
```

### For Console 🖥️

```bash
/rename <target> <newName>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| newName | String | Yes | `base.commands.rename.arguments.newName.description` |

</details>

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| target | OfflinePlayer | Yes | `base.commands.rename.arguments.target.description` |
| newName | String | Yes | `base.commands.rename.arguments.newName.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.rename`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `renamedFaction` | `base.commands.rename.renamed-faction` |
| `factionNeeded` | `base.commands.rename.faction-needed` |
| `notFactionOwner` | `base.commands.rename.not-faction-owner` |
| `noPermission` | `base.commands.rename.no-permission` |
| `invalidName` | `base.commands.rename.invalid-name` |
| `nameTooLong` | `base.commands.rename.name-too-long` |
| `factionNameExists` | `base.commands.rename.faction-name-exists` |
| `missingRequiredArgument` | `base.commands.rename.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
