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

An enchantment that does exactly one thing: hold right-click to draw the Zenith Sword
back, release to be thrown in the direction you are looking, like a firework under an
elytra. Left-click still swings.

**Craft it in a crafting table:** one book in the middle, 8 Bars of Zenith around it. That
yields a Zenith's Power book, which you apply to the sword on an anvil.

- 3 second cooldown.
- Costs one durability, taken down the same path as a swing, so Unbreaking gets its roll.
- No fall protection. With an elytra you fly; without one you come back down the hard way.
- Wind-up is 5 ticks. Release earlier and nothing fires.
- The sword draws back over four stages across the first 9 ticks, then holds there for as
  long as you keep the button down.

It **stacks with everything else**. The sword is an ordinary sword as far as the game is
concerned, so Sharpness, Looting, Fire Aspect, Mending and the rest apply normally from
the enchanting table or an anvil, and Zenith's Power sits alongside them.

What Zenith's Power will *not* do is go on anything else. Its definition names
`zenithmod:zenith_sword` as the only item it supports, so the anvil refuses it on any
other weapon.

### How it is built

The enchantment JSON carries **no effects at all** — it exists to bind to the sword and be
read. No enchantment effect component can move the player, so the launch is item code in
`ZenithSwordItem` that checks for the enchantment.

The drawn-back pose is two things: `getUseAnimation` returning `SPEAR` for the arm, plus a
model swap in `assets/zenithmod/items/zenith_sword.json` that range-dispatches on
`minecraft:use_duration` through `zenith_sword_charging_1` to `_4` and holds at the last.
The bow pulls the same way. The arm pose alone is nearly invisible on a flat sprite, so
the model swap is what you actually see.

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
