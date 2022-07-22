package io.github.toberocat.core.utility.gui.builder;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.action.ActionCore;
import io.github.toberocat.core.utility.gui.AbstractGui;
import io.github.toberocat.core.utility.gui.AutoGui;
import io.github.toberocat.core.utility.gui.TabbedGui;
import io.github.toberocat.core.utility.gui.exception.CouldntFindAClickEventException;
import io.github.toberocat.core.utility.gui.exception.WrongGuiTypeException;
import io.github.toberocat.core.utility.gui.page.Page;
import io.github.toberocat.core.utility.gui.settings.GuiSettings;
import io.github.toberocat.core.utility.gui.slot.Slot;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

public class GuiBuilder {
    private int size = -1;
    private String title = null;
    private boolean pageArrows = false;
    private LinkedList<String> quitActions;
    private LinkedList<PageBuilder> pages = new LinkedList<>();
    private final LinkedHashMap<String, Consumer<Player>> clickEvents = new LinkedHashMap<>();
    /*
    public static void openFromConfig(Player player, ConfigurationSection section) {
        GuiBuilderSerializer serializer = new GuiBuilderSerializer(section);
        GuiBuilder builder = new GuiBuilder();

        builder.rows(serializer.readInt("rows", 1));
        builder.title(StringUtils.replace(serializer.readString("title", null), info.placeholders()));
        builder.arrows(serializer.readBool("arrows", false));
        builder.quit(serializer.readList("quit", null));

        for (int i = 0; i < serializer.readInt("pages", 1); i++) builder.addPage();

        for (String key : serializer.readKeys("slots")) {
            int invSlot = serializer.readInt("slots." + key + ".slot", 0);
            int page = serializer.readInt("slots." + key + ".page", 0);
            List<String> clickEvents = serializer.readList("slots." + key + ".click",
                    new LinkedList<>());

            ConfigurationSection icon = section.getConfigurationSection("slots." + key + ".icon");
            if (icon == null) throw new EachSlotNeedsAIconException("Couldn't serializse slot" +
                    key + ", as it couldn't find a .icon property");

            ItemStack stack = ItemCore.create(icon, info);
            if (stack == null)
                throw new ItemNotFoundException("The item wasn't found for slots." + key + ".icon");

            builder.add(stack, page, invSlot, (player) -> {
                for (String action : clickEvents) {
                    if (!ActionCore.run(action.replaceAll("\\{player}", player.getName()), player)) {
                        Consumer<Player> run = builder.clickEvents.get(action);
                        if (run == null) {
                            SpivakCore.getPlugin(SpivakCore.class).getLogger()
                                    .log(Level.WARNING, "Couldn't find click event " + action);
                            continue;
                        }
                        run.accept(player);
                    }
                }
            });

        }

        return builder;
    }

    public static GuiBuilder fromConfig(ConfigurationSection section) {
        GuiBuilderSerializer serializer = new GuiBuilderSerializer(section);
        GuiBuilder builder = new GuiBuilder();

        builder.rows(serializer.readInt("rows", 1));
        builder.title(serializer.readString("title", "Gui"));
        builder.arrows(serializer.readBool("arrows", false));
        builder.quit(serializer.readList("quit", null));

        for (int i = 0; i < serializer.readInt("pages", 1); i++) builder.addPage();

        for (String key : serializer.readKeys("slots")) {
            int invSlot = serializer.readInt("slots." + key + ".slot", 0);
            int page = serializer.readInt("slots." + key + ".page", 0);
            List<String> clickEvents = serializer.readList("slots." + key + ".click",
                    new LinkedList<>());

            ConfigurationSection icon = section.getConfigurationSection("slots." + key + ".icon");
            if (icon == null) throw new EachSlotNeedsAIconException("Couldn't serializse slot" +
                    key + ", as it couldn't find a .icon property");

            ItemStack stack = ItemCore.create(icon);

            builder.add(stack, page, invSlot, (player) -> {
                for (String action : clickEvents) {
                    if (!ActionCore.run(action.replaceAll("\\{player}", player.getName()), player)) {
                        Consumer<Player> run = builder.clickEvents.get(action);
                        if (run == null) {
                            SpivakCore.getPlugin(SpivakCore.class).getLogger()
                                    .log(Level.WARNING, "Couldn't find click event " + action);
                            continue;
                        }
                        run.accept(player);
                    }
                }
            });

        }

        return builder;
    }
    */

    public GuiBuilder clickEvent(String id, Consumer<Player> click) {
        clickEvents.put(id, click);
        return this;
    }

    public TabbedGui buildTabbedGui(Player player) {
        if (size == -1) size = 9;
        if (title == null) title = "GuiBuilder";

        TabbedGui gui = new TabbedGui(player, AbstractGui.createInventory(player, size, title)) {
            @Override
            protected GuiSettings readSettings() {
                GuiSettings settings = super.readSettings().setPageArrows(pageArrows);

                if (quitActions != null) settings.setQuitGui(() ->
                        ActionCore.run(quitActions, player));

                return settings;
            }
        };

        addSlots(gui);

        return gui;
    }

    public AutoGui buildAutoGui(Player player, int[] slots) {
        if (size == -1) size = 9;
        if (title == null) title = "GuiBuilder";

        AutoGui gui = new AutoGui(player, AbstractGui.createInventory(player, size, title), slots) {
            @Override
            protected GuiSettings readSettings() {
                GuiSettings settings = new GuiSettings().setPageArrows(pageArrows);

                if (quitActions != null) settings.setQuitGui(() ->
                        ActionCore.run(quitActions, player));

                return settings;
            }
        };

        return addSlots(gui);
    }

    private AutoGui addSlots(AutoGui gui) {
        for (int i = 0; i < pages.size(); i++) {
            PageBuilder page = pages.get(i);
            for (SlotBuilder slot : page.getSlots()) {
                if (!clickEvents.containsKey(slot.getClickEvent())) throw new
                        CouldntFindAClickEventException("Didn't find a click event for your slot. Available: " +
                        clickEvents.keySet() + " - Given " + slot.getClickEvent());

                if (slot.getSlot() == -1) {
                    gui.addSlot(slot.getStack(), clickEvents.get(slot.getClickEvent()));
                } else {
                    gui.addSlot(slot.getStack(), i, slot.getSlot(),
                            clickEvents.get(slot.getClickEvent()));
                }
            }
        }

        return gui;
    }

    public AbstractGui buildAbstract(Player player) {
        if (size == -1) size = 9;
        if (title == null) title = "GuiBuilder";

        AbstractGui gui = new AbstractGui(player, AbstractGui.createInventory(player, size, title)) {
            @Override
            protected void addPage() {
                pages.add(new Page(size));
            }

            @Override
            protected GuiSettings readSettings() {
                GuiSettings settings = new GuiSettings().setPageArrows(pageArrows);

                if (quitActions != null) settings.setQuitGui(() ->
                        ActionCore.run(quitActions, player));


                return settings;
            }
        };

        for (int i = 0; i < pages.size(); i++) {
            PageBuilder page = pages.get(i);
            for (SlotBuilder slot : page.getSlots()) {
                if (slot.getSlot() == -1) throw new WrongGuiTypeException("Can't use a abstract gui" +
                        " for a auto assign slot. Use a buildAutoGui or buildTabbedGui or specify" +
                        " a inventory slot and the page");

                if (!clickEvents.containsKey(slot.getClickEvent())) throw new
                        CouldntFindAClickEventException("Didn't find a click event for your slot");

                gui.addSlot(slot.getStack(), i, slot.getSlot(),
                        clickEvents.get(slot.getClickEvent()));
            }
        }
        return gui;
    }

    public GuiBuilder addPage() {
        pages.add(new PageBuilder());
        return this;
    }

    public GuiBuilder add(Slot slot, int page, int invSlot) {
        if (page >= pages.size()) throw new IllegalArgumentException("Add another page before " +
                "trying to accessing it");

        pages.get(page).add(slot, invSlot);
        clickEvents.put(slot.getStack().getType().name() + "::" + invSlot, slot::click);

        return this;
    }

    public GuiBuilder add(ItemStack stack, int page, int invSlot, Consumer<Player> clickEvent) {
        if (page >= pages.size()) throw new IllegalArgumentException("Add another page before " +
                "trying to accessing it");

        pages.get(page).add(stack, invSlot);
        clickEvents.put(stack.getType().name() + "::" + invSlot, clickEvent);

        return this;
    }


    public GuiBuilder add(ItemStack stack, int page, int invSlot, String clickEvent) {
        if (page >= pages.size()) throw new IllegalArgumentException("Add another page before " +
                "trying to accessing it");

        pages.get(page).add(stack, invSlot, clickEvent);

        return this;
    }

    public GuiBuilder add(Slot slot) { // Using auto pages
        pages.get(pages.size() - 1).add(slot);
        clickEvents.put(slot.getStack().getType().name() + "::-1", slot::click);

        return this;
    }

    public int rows() {
        return size;
    }


    public GuiBuilder rows(int size) {
        this.size = Utility.clamp(size, 0, 6) * 9;
        return this;
    }

    public String title() {
        return title;
    }

    public GuiBuilder title(String title) {
        this.title = title;
        return this;
    }

    public boolean arrows() {
        return pageArrows;
    }


    public GuiBuilder arrows(boolean pageArrows) {
        this.pageArrows = pageArrows;
        return this;
    }

    public LinkedList<String> quit() {
        return quitActions;
    }

    public GuiBuilder quit(List<String> quitActions) {
        if (quitActions == null) return this;
        this.quitActions = new LinkedList<>(quitActions);
        return this;
    }

    public LinkedList<PageBuilder> pages() {
        return pages;
    }

    public GuiBuilder pages(LinkedList<PageBuilder> pages) {
        this.pages = pages;
        return this;
    }
}
