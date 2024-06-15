package com.doubledeltas.icaruseye;

import com.doubledeltas.icaruseye.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class HUDAction extends BukkitRunnable {
    // block/ticks -> km/h
    private static final double BPT_TO_KMPH = 72.0;

    private static final long PERIOD_TICK = 2L;

    private long timer = 0L;

    @Override
    public void run() {
        Iterable<? extends Player> players = IcarusEye.getInstance().getServer().getOnlinePlayers();
        for (Player player: players) {
            if (!player.isGliding())
                continue;
            displayHUDTo(player);
        }
        timer += PERIOD_TICK;
    }

    public void displayHUDTo(Player player) {
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        // unit: km/h
        double vel = player.getVelocity().length() * BPT_TO_KMPH;
        StringBuilder velMsgBuilder = new StringBuilder("[");
        if (vel < 100.0) velMsgBuilder.append("§80");
        if (vel <  10.0) velMsgBuilder.append("§80");
        velMsgBuilder.append(String.format("§r%.2f km/h]", vel));

        String msg = String.format(
                "§c%d §a%d §9%d §r| %s | §%c\uD83D\uDE80§%c\uD83D\uDC94§%c\uD83D\uDD31",
                x, y, z,
                velMsgBuilder,
                getRocketLevel(player).getColor(timer).getChar(),
                getDurabilityLevel(player).getColor(timer).getChar(),
                getRiptideLevel(player).getColor(timer).getChar()
        );

        MessageUtil.sendActionBar(player, msg);
    }

    @NotNull
    private static DisplayLevel getRocketLevel(Player player) {
        ItemStack onHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        int sum = 0;
        if (onHand.getItemMeta() != null && onHand.getItemMeta() instanceof FireworkMeta) {
            FireworkMeta onHandMeta = (FireworkMeta) onHand.getItemMeta();
            if (!onHandMeta.hasEffects()) {
                sum += onHand.getAmount();
            }
        }
        if (offHand.getItemMeta() != null && offHand.getItemMeta() instanceof FireworkMeta) {
            FireworkMeta offHandMeta = (FireworkMeta) offHand.getItemMeta();
            if (!offHandMeta.hasEffects()) {
                sum += offHand.getAmount();
            }
        }
        if (sum ==  0) return DisplayLevel.DISABLED;
        if (sum <=  4) return DisplayLevel.WARNING;
        if (sum <= 16) return DisplayLevel.CAUTION;
        return DisplayLevel.IDLE;
    }

    @NotNull
    private static DisplayLevel getDurabilityLevel(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null)
            return DisplayLevel.DISABLED;

        short currentDurability = chestplate.getDurability();
        short maxDurability = chestplate.getType().getMaxDurability();
        DisplayLevel durabilityLevel = DisplayLevel.DISABLED;
        if (maxDurability > 0) {
            double durabilityRatio = 1.0 - (double) currentDurability / maxDurability;
            if      (durabilityRatio > 0.3) { durabilityLevel = DisplayLevel.IDLE; }
            else if (durabilityRatio > 0.1) { durabilityLevel = DisplayLevel.CAUTION; }
            else if (durabilityRatio > 0.0) { durabilityLevel = DisplayLevel.WARNING; }
        }
        return durabilityLevel;
    }

    @NotNull
    private static DisplayLevel getRiptideLevel(Player player) {
        Location loc = player.getLocation();

        boolean isInWater = loc.getBlock().getType() == Material.WATER;
        boolean isRaining = player.getWorld().hasStorm();
        boolean isNoCeiling = loc.getBlockY() > player.getWorld().getHighestBlockYAt(loc);

        double temperature = loc.getBlock().getTemperature();
        boolean isInRainingTemperature = 0.15 <= temperature && temperature <= 0.95;

        boolean isRiptideable = isInWater || (isRaining && isInRainingTemperature && isNoCeiling);

        return isRiptideable ? DisplayLevel.ENABLED : DisplayLevel.IDLE;
    }

    public void start() {
        this.runTaskTimer(IcarusEye.getInstance(), 0L, PERIOD_TICK);
    }
}
