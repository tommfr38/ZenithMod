# Zenith

Fabric mod for Minecraft **26.1.2**. Adds the Bar of Zenith and the legendary Zenith Sword.

## Items

| Item | Notes |
| --- | --- |
| **Bar of Zenith** (`zenithmod:bar_of_zenith`) | Crafting material, animated rainbow texture, Rare. |
| **Zenith Sword** (`zenithmod:zenith_sword`) | 20 attack damage, 64 durability, 1.6 attacks/sec, Epic. |
| **Block of Zenith** (`zenithmod:zenith_block`) | Decorative. Diagonal rainbow that sweeps and pulses, light level 15, pickaxe-mined (diamond or better). |

## Recipes

**Bar of Zenith** — 8 emerald blocks around 1 netherite block, yields **1** bar. A sword
costs three of them, so three full crafts.

```
E E E
E N E     E = minecraft:emerald_block
E E E     N = minecraft:netherite_block
```

**Zenith Sword** — 3 bars, 1 dragon egg, 1 stick. The egg is consumed, so the sword
cannot exist before the Ender Dragon is beaten.

```
. B .
B D B     B = zenithmod:bar_of_zenith
. S .     D = minecraft:dragon_egg, S = minecraft:stick
```

**Dragon egg refund** — put the Zenith Sword alone in a crafting grid to get the dragon
egg back. The sword is consumed; the bars, the stick and any enchantments are not
returned, and the sword's remaining durability does not matter.

**Block of Zenith** — 9 bars in a 3x3, and a shapeless recipe turns the block back into
9 bars. Standard storage-block convention, so nothing is ever lost by crafting it.

## Zenith's Power

The only enchantment the Zenith Sword can ever hold — one enchantment carrying what
would otherwise be seven:

| Rolled in | Effect |
| --- | --- |
| Sharpness V | +3 attack damage |
| Sweeping Edge III | sweeping damage ratio +0.75 |
| Fire Aspect II | victim burns for 8 seconds |
| Knockback II | +2 knockback |
| Looting III | +3 loot rolls, and +3% equipment drop chance |
| Unbreaking III | 75% chance to skip durability loss |
| Mending | XP repairs at 2 durability per point |

**Craft it in a crafting table:** one book in the middle, 8 Bars of Zenith around it.
That yields a Zenith's Power book, which you apply to the sword on an anvil.

### Why the sword is not in #minecraft:swords

Removing it from that tag is what makes "only this enchantment" true. Every vanilla
`enchantable/*` tag is fed by `#minecraft:swords`, so being outside it means Sharpness,
Mending and the rest have nothing to bind to — the enchanting table offers nothing and
the anvil rejects every other book. Zenith's Power binds through its own
`supported_items` instead, which names the sword directly.

That tag controls exactly one other thing in the game: `Player.isSweepAttack`. So
`PlayerMixin` redirects that single tag check to also accept the sword, leaving every
movement and cooldown condition untouched.

### Why Looting needs a mixin

Looting's drop bonus is not something an enchantment grants by itself. Every mob loot
table calls `minecraft:enchanted_count_increase` naming `minecraft:looting` outright, so
no custom enchantment can trigger it. `EnchantmentHelperMixin` reports a Looting level of
3 for a sword carrying Zenith's Power, which makes the bonus apply in modded loot tables
as well as vanilla ones.

The other six fold in cleanly as ordinary datapack effects — see
`data/zenithmod/enchantment/zeniths_power.json`.

## Tuning

- Damage and attack speed: `ZenithItems.ZENITH_SWORD`, the `sword(ZENITH, 19.0F, -2.4F)` call.
  The tooltip number is the player's base 1 plus the 19 here.
- Durability and enchantability: the `ZENITH` `ToolMaterial` in the same file.
- Bars per craft: the `count` in `data/zenithmod/recipe/bar_of_zenith.json`.
- Block glow: the `lightLevel(state -> 15)` call in `ZenithBlocks`. Drop it for a dark block.
- Pulse speed: `frametime` in `zenith_block.png.mcmeta`. 1 tick per frame over 32 frames is
  one full pulse every 1.6s.
- Static instead of animated textures: delete the two `.png.mcmeta` files and crop each
  PNG down to its top 16x16 frame.

## Building

Needs JDK 25. The toolchain used here lives at
`../TwilightBoss/toolchain/jdk-25.0.4+7`.

```
JAVA_HOME=../TwilightBoss/toolchain/jdk-25.0.4+7 ./gradlew build
```

Output lands in `build/libs/zenithmod-1.0.0.jar`.
