package io.github.toberocat.improvedFactions.core.utils;

import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@SuppressWarnings("unused")
public final class Formatting {

    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00", SYMBOLS);
    private static final DecimalFormat DECIMAL_POTENTIAL_FORMAT = new DecimalFormat("#,##0.##", SYMBOLS);
    private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#,##0", SYMBOLS);

    private static @NotNull String getName() {
        return Formatting.class.getSimpleName();
    }

    /* Number */

    public static @NotNull String integer(double arg) {
        return INTEGER_FORMAT.format(arg);
    }

    public static @NotNull String integer(@NotNull BigDecimal arg) {
        return INTEGER_FORMAT.format(arg);
    }

    public static @NotNull String decimal(double arg) {
        return DECIMAL_FORMAT.format(arg);
    }

    public static @NotNull String decimal(@NotNull BigDecimal arg) {
        return DECIMAL_FORMAT.format(arg);
    }

    public static @NotNull String decimalPotential(double arg) {
        return DECIMAL_POTENTIAL_FORMAT.format(arg);
    }

    public static @NotNull String decimalPotential(@NotNull BigDecimal arg) {
        return DECIMAL_POTENTIAL_FORMAT.format(arg);
    }

    public static @NotNull SortedSet<Map.Entry<String, BigDecimal>> numberSymbols() {
        SortedSet<Map.Entry<String, BigDecimal>> set = new TreeSet<>(Map.Entry.comparingByValue());
        ConfigFile api = ImprovedFactions.api().getConfig();
        List<String> symbols = api.getSubSections("number-symbols");
        for (String symbol : symbols)
            set.add(Map.entry(symbol,
                    new BigDecimal(api.getString("number-symbols." + symbol, "0"))));
        return set;
    }

    public static @NotNull String shorten(@NotNull BigDecimal arg) {
        Map.Entry<String, BigDecimal> checkpoint = null;

        for (Map.Entry<String, BigDecimal> entry : numberSymbols()) {
            BigDecimal number = entry.getValue();

            if (arg.compareTo(number) >= 0) {
                checkpoint = entry;
            }
        }

        if (checkpoint == null) {
            return integer(arg);
        }

        BigDecimal multiplierValue = checkpoint.getValue();
        MathContext context = new MathContext(5, RoundingMode.FLOOR);
        BigDecimal divide = arg.divide(multiplierValue, context);

        return decimalPotential(divide) + checkpoint.getKey();
    }

    public static @NotNull String shorten(double arg) {
        return shorten(new BigDecimal(arg));
    }
}
