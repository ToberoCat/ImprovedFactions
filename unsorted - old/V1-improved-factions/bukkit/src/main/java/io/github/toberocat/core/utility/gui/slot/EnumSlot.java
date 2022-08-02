package io.github.toberocat.core.utility.gui.slot;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

import static io.github.toberocat.core.utility.language.Language.format;

public abstract class EnumSlot<T extends Enum<T>> extends Slot {

    protected int selected;
    protected String[] values;
    private final Runnable rerender;

    public EnumSlot(ItemStack stack, Class<T> enumType, Runnable rerender) {
        super(modify(stack, enumType));
        this.selected = -1;
        this.values = Arrays.stream(enumType.getEnumConstants()).map(x -> format(x.toString())).toArray(String[]::new);
        this.rerender = rerender;
    }

    private static <T extends Enum<T>> ItemStack modify(ItemStack stack, Class<T> enumType) {

        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return stack;

        ArrayList<String> lore = new ArrayList<>();

        lore.add("§7Type: §eSelector");
        lore.add("§7Click to swap selected");

        lore.addAll(Arrays.stream(enumType.getEnumConstants())
                .map(x -> format(x.toString())).toList());

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }


    @Override
    public void click(Player player) {
        selected = (selected + 1) % values.length;
        ItemMeta meta = getStack().getItemMeta();
        if (meta == null) return;

        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7Type: §eSelector");
        lore.add("§7Click to swap selected");

        for (int i = 0; i < values.length; i++) lore.add(i == selected ?
                "§f§n" + ChatColor.stripColor(values[i]) : "§7" + values[i]);

        changeSelected(values[selected]);

        meta.setLore(lore);
        getStack().setItemMeta(meta);

        rerender.run();
    }

    public abstract void changeSelected(String newValue);
}
