---
id: claim
title: Claim
---

# Claim

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.claim.description`

## Usage

### For Player 👤

```bash
/claim [radius]
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| radius | Int | No | `base.commands.claim.arguments.radius.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.claim`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `claimed` | `base.commands.claim.claimed` |
| `claimedRadius` | `base.commands.claim.claimed-radius` |
| `claimError` | `base.commands.claim.claim-error` |
| `notInFaction` | `base.commands.claim.not-in-faction` |
| `noPermission` | `base.commands.claim.no-permission` |
| `missingRequiredArgument` | `base.commands.claim.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
