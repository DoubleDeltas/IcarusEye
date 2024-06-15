package com.doubledeltas.icaruseye;

import org.bukkit.ChatColor;

public enum DisplayLevel {
    IDLE(ChatColor.DARK_GRAY),
    CAUTION(ChatColor.YELLOW),
    WARNING(ChatColor.RED, ChatColor.DARK_GRAY),
    DISABLED(ChatColor.RED),
    ENABLED(ChatColor.AQUA),
    ;

    public static final long FLICKING_PERIOD_HALF = 20;

    private final ChatColor[] colors;

    DisplayLevel(ChatColor color1, ChatColor color2) {
        this.colors = new ChatColor[] {color1, color2};
    }

    DisplayLevel(ChatColor color) {
        this(color, color);
    }

    public ChatColor getColor(long ticks) {
        return this.colors[(int)((ticks / FLICKING_PERIOD_HALF) % 2L)];
    }
}
