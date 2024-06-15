package com.doubledeltas.icaruseye;

import com.doubledeltas.icaruseye.util.MessageUtil;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class IcarusEye extends JavaPlugin {
    // c.f. What is 'Audience'? - https://docs.advntr.dev/audiences.html
    private AudienceProvider audienceProvider;

    public static IcarusEye getInstance() {
        return IcarusEye.getPlugin(IcarusEye.class);
    }

    public AudienceProvider getAudienceProvider() {
        if (audienceProvider == null) {
            throw new IllegalStateException("Accessed to audience provider before enabled!");
        }
        return audienceProvider;
    }

    @Override
    public void onEnable() {
        audienceProvider = BukkitAudiences.create(this);

        new HUDAction().start();

        MessageUtil.log(Level.INFO, "Icarus' Eye is opened!");
    }

    @Override
    public void onDisable() {
        if (audienceProvider != null) {
            audienceProvider.close();
            audienceProvider = null;
        }
        MessageUtil.log(Level.INFO, "Icarus' Eye is closed!");
    }
}
