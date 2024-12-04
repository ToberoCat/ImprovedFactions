# Power Raids

This module is responsible for handing faction power, as well as it's associated side effects, like raids and chunk
overclaiming

# How it works

Based on an internal discussion on the discord, the power system should be implemented like this (This is the message
summarising all the discussed content):

Okay, after this small discussion, I would implement the power like this (All inlined values will probably be
configurable):

- Each **faction** (Not player, for simplicity reasons) has power. It ranges from 0 to 10.
- Each member a faction gains brings a change to it's power. The increase of power will be calculated on a base value.
  like 50 (called `base-member-constant` in power raid config section). Then, this base value will get multiplied by a
  factor of 1 over the current member amount.
  **Example Log:**
  FactionMax Power: 10; 1 Member
  New Member: 10 + 50 * 1 / 1 (last member count) = 60
  New Member: 60 + 50 * 1 / 2 = 85
  New Member: 85 + 50 * 1 / 3 = 102 (When adding, ceil)
  Left Member: 102- 50 * 1 / 3 (member count - 1) = 85 (When subtracting, floor)

**Why?**
This tries to help smaller factions. When for example the third member joined, it gained the faction 17 max power, while
when the 20th member would have joined, it would have gained the faction only 3 max power. This drop-off can be
configured.

- Each online member accumulates the their full power potential. Additionally, to motivate factions to play at the same
  time, there will be a boost at the power accumulation based on the amount of people that have taken part in the
  accumulation. All members counted as inactive (Last seen 7 days or so) will have a negative effect on this
  accumulation.
  This is forgiving for players that just can't login daily, but will still punish alts / inactive players.

- Claiming a chunk costs a faction power. The more chunks a faction owns, the more expensive the chunks get, controlled
  by a constant in the config. This should help factions value their chunks if the max chunks is set to a very high
  number.

- When a faction accumulates power, their allies will receive a small fraction of it. This will help control the amount
  of allies being formed. Too many allies and you won't have any power left for yourself. Not the right allies / not
  enough and you'll have major disadvantages in combat.

**Accumulation**

The accumulation formula is quite complex, but its purpose is to balance the power accumulation in a faction-based game.
Here's a breakdown of how it works and why it's designed this way:

**The formular:**
`base + max(((1 + activeMembers / totalMembers)^accumulation-active-exponent - inactiveMembers * accumulation-inactive-multiplier) * accumulation-multiplier, 0)`

**Purpose:**
The formula aims to create a fair system for factions of different sizes, especially favoring smaller factions. It
encourages factions to have more active members while penalizing inactive ones.

**Formula Explanation:**
The formula has several components:

1. **Active Member Normalization:**

- Active members are normalized between 1 and 2. This means that a small faction with a few active members can achieve a
  similar accumulation rate as a larger faction, promoting fairness. For example, a faction with 1 online member and 2
  total members would accumulate 1.5, while a faction with 50 members and only 1 active member would accumulate only
  1.02.

2. **Penalty for Inactive Members:**

- Inactive members are penalized without normalization. This means that if, for instance, 10% of a faction's members are
  inactive, there will be a penalty of 1 for a faction of 10 members and the same penalty for a faction of 100 members.
  This encourages factions to keep their members active.

3. **Zero Accumulation Floor:**

- The formula ensures that the accumulated power won't drop below zero. This prevents factions from facing catastrophic
  losses in power.

In summary, this complex formula is designed to level the playing field among factions of varying sizes, motivating them
to have more active members while discouraging inactivity. It fosters fairness and competition in the game environment.

**Overclaiming & Raiding**

- Claims will get unprotected when a faction can't afford the base claim power cost. This means when they have more
  claims then they have power. The chunk will be raidable by **everyone**. Once a chunk is no longer protected, factions
  that are in war with this faction can start the overclaiming process.

- Overclaiming will work by having someone of a faction you're in war with stay in your chunk for x time units. This
  makes sure that the enemy faction has to "capture" the claim before overclaiming it. This also prevents them from
  overclaiming in such a short time, that the other faction had no chance to defend their property.

- Allies can offer help by sending power. This is just a idea I had right now, but maybe that gives allies another
  purpose to exist
