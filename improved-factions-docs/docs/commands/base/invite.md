---
id: invite
title: Invite
---

# Invite

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.invite.description`

## Usage

### For Player 👤

```bash
/invite <invited> [rank]
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| invited | OfflinePlayer | Yes | `base.commands.invite.arguments.invited.description` |
| rank | FactionRank | No | `base.commands.invite.arguments.rank.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.invite`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `invitedPlayer` | `base.commands.invite.invited-player` |
| `playerNoFaction` | `base.commands.invite.player-no-faction` |
| `playerInvited` | `base.commands.invite.player-invited` |
| `noPermission` | `base.commands.invite.no-permission` |
| `rankNotFound` | `base.commands.invite.rank-not-found` |
| `missingRequiredArgument` | `base.commands.invite.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
