---
id: admin bypass
title: Admin bypass
description: Admin bypass command allows you to bypass the admin protection of a player.
tags: [base, admin, bypass]
---

# Admin bypass

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.admin-bypass.description`

## Usage

### For Player 👤

```bash
/admin bypass [target]
```

### For Console 🖥️

```bash
/admin bypass <target>
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| target | Player | No | `base.commands.admin-bypass.arguments.target.description` |

</details>

### For Console 🖥️

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| target | Player | Yes | `base.commands.admin-bypass.arguments.target.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.admin-bypass`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `bypassAdded` | `base.commands.admin-bypass.bypass-added` |
| `bypassRemoved` | `base.commands.admin-bypass.bypass-removed` |
| `playerNotFound` | `base.commands.admin-bypass.player-not-found` |
| `missingRequiredArgument` | `base.commands.admin-bypass.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
