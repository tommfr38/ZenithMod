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

## Enchanting

`data/minecraft/tags/item/swords.json` appends the Zenith Sword to `#minecraft:swords`.
That one tag cascades through the vanilla enchantable tags, so the sword gets exactly
what a vanilla sword gets: Sharpness/Smite/Bane of Arthropods, Looting, Knockback,
Fire Aspect, Sweeping Edge, Unbreaking, **Mending**, Curse of Vanishing — plus sweep
attacks and normal anvil/grindstone behaviour.

Anvil repair material is the Bar of Zenith (`data/zenithmod/tags/item/zenith_repair_materials.json`).

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
