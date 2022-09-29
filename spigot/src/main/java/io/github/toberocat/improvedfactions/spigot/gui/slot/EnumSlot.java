package io.github.toberocat.improvedfactions.spigot.gui.slot;

import com.spivaknetwork.core.util.StringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class EnumSlot<T extends Enum<T>> extends Slot {

    protected int selected;
    protected String[] values;
    private final Runnable rerender;

    public EnumSlot(ItemStack stack, Class<T> enumType, int selected, Runnable rerender) {
        super(modify(stack, selected, enumType));
        this.selected = selected;
        this.values = Arrays.stream(enumType.getEnumConstants()).map(x -> StringUtils.format(x.toString())).toArray(String[]::new);
        this.rerender = rerender;
    }

    private static <T extends Enum<T>> ItemStack modify(ItemStack stack, int selected, Class<T> enumType) {

        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return stack;

        ArrayList<Component> lore = new ArrayList<>();

        lore.add(Component.text("§7Type: §eSelector"));
        lore.add(Component.text("§7Click to swap selected"));

        T[] values = enumType.getEnumConstants();

        for (int i = 0; i < values.length; i++) lore.add(Component.text(
                i == selected ? "§f-> " + ChatColor.stripColor(values[i].toString()) : values[i].toString()));

        meta.lore(lore);
        stack.setItemMeta(meta);

        return stack;
    }


    @Override
    public void click(@NotNull Player player, @Nullable ItemStack cursor) {
        selected = (selected + 1) % values.length;
        updateItem();
    }

    @Override
    public void rightClick(@NotNull Player player, @Nullable ItemStack cursor) {
        selected = (selected - 1);
        if (selected < 0) selected = values.length - 1;

        updateItem();
    }

    private void updateItem() {
        ItemMeta meta = getStack().getItemMeta();
        if (meta == null) return;

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Type: §eSelector"));
        lore.add(Component.text("§7Click to swap selected"));

        for (int i = 0; i < values.length; i++) lore.add(Component.text(
                i == selected ? "§f-> " + ChatColor.stripColor(values[i])
                        : values[i]));

        changeSelected(values[selected]);

        meta.lore(lore);
        getStack().setItemMeta(meta);

        rerender.run();
    }

    public abstract void changeSelected(String newValue);
}
