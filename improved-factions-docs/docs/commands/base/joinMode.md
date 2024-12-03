---
id: joinMode
title: JoinMode
---

# JoinMode

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.joinmode.description`

## Usage

### For Player 👤

```bash
/joinMode <joinType>
```

### For Console 🖥️

```bash
/joinMode <target> <joinType>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| joinType | FactionJoinType | Yes | `base.commands.joinmode.arguments.joinType.description` |

</details>

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| target | OfflinePlayer | Yes | `base.commands.joinmode.arguments.target.description` |
| joinType | FactionJoinType | Yes | `base.commands.joinmode.arguments.joinType.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.joinmode`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `joinModeChanged` | `base.commands.joinmode.join-mode-changed` |
| `invalidJoinType` | `base.commands.joinmode.invalid-join-type` |
| `notInFaction` | `base.commands.joinmode.not-in-faction` |
| `noPermission` | `base.commands.joinmode.no-permission` |
| `missingRequiredArgument` | `base.commands.joinmode.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
