package net.tomi.zenithmod.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.tomi.zenithmod.init.ZenithEnchantments;
import net.tomi.zenithmod.init.ZenithItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Forging recipe for Zenith's Strength: an Unbreaking III book in the left slot plus
 * Bars of Zenith in the right one produces a Zenith's Strength book.
 *
 * <p>Vanilla has no data-driven anvil recipes, so this hooks the menu directly.
 * Consumption is left to vanilla: setting {@code repairItemCountCost} makes
 * {@code onTake} take exactly the bars we charge for, and the book slot is emptied
 * the way any anvil input is.
 */
@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    private static final int BAR_COST = 5;
    private static final int UNBREAKING_LEVEL = 3;
    private static final int LEVEL_COST = 10;

    protected AnvilMenuMixin(MenuType<?> type, int id, Inventory inventory,
                             ContainerLevelAccess access, ItemCombinerMenuSlotDefinition definition) {
        super(type, id, inventory, access, definition);
    }

    @Shadow
    private int repairItemCountCost;

    @Shadow
    @Final
    private DataSlot cost;

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void zenithmod$forgeZenithsStrength(CallbackInfo ci) {
        ItemStack book = this.inputSlots.getItem(0);
        ItemStack bars = this.inputSlots.getItem(1);

        // Exactly one book: an anvil consumes the whole left stack, and silently eating
        // a stack of Unbreaking books would be a nasty surprise.
        if (book.getCount() != 1 || !book.is(Items.ENCHANTED_BOOK)) {
            return;
        }
        if (!bars.is(ZenithItems.BAR_OF_ZENITH) || bars.getCount() < BAR_COST) {
            return;
        }

        HolderLookup.RegistryLookup<Enchantment> lookup =
                this.player.level().registryAccess().lookup(Registries.ENCHANTMENT).orElse(null);
        if (lookup == null) {
            return;
        }
        Holder<Enchantment> unbreaking = lookup.get(Enchantments.UNBREAKING).orElse(null);
        Holder<Enchantment> zenithsStrength = lookup.get(ZenithEnchantments.ZENITHS_STRENGTH).orElse(null);
        if (unbreaking == null || zenithsStrength == null) {
            return;
        }

        ItemEnchantments stored = book.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (stored.getLevel(unbreaking) < UNBREAKING_LEVEL) {
            return;
        }

        ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(zenithsStrength, 1);
        result.set(DataComponents.STORED_ENCHANTMENTS, enchantments.toImmutable());

        this.resultSlots.setItem(0, result);
        this.cost.set(LEVEL_COST);
        this.repairItemCountCost = BAR_COST;
        this.broadcastChanges();
        ci.cancel();
    }
}
