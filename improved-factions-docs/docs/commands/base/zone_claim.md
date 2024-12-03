---
id: zone claim
title: Zone claim
---

# Zone claim

> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.

## Description

`base.commands.zone-claim.description`

## Usage

### For Player 👤

```bash
/zone claim <zone> [radius]
```

## Parameters

### For Player 👤

<details>
<summary>View Parameters</summary>

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| zone | Zone | Yes | `base.commands.zone-claim.arguments.zone.description` |
| radius | Int | No | `base.commands.zone-claim.arguments.radius.description` |

</details>

## Permissions

🔒 **Permission Required:** `factions.commands.zone-claim`

## Responses

| Response Code             | Description                                         |
|---------------------------|-----------------------------------------------------|
| `zoneClaimed` | `base.commands.zone-claim.zone-claimed` |
| `zoneClaimedRadius` | `base.commands.zone-claim.zone-claimed-radius` |
| `claimError` | `base.commands.zone-claim.claim-error` |
| `missingRequiredArgument` | `base.commands.zone-claim.missing-required-argument` |

---
**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.
