package org.patryk3211.powergrid.compat;

import dev.architectury.platform.Platform;
import net.createmod.catnip.lang.Lang;

import java.util.function.Supplier;

public enum Mods {
    COMPUTERCRAFT;

    private final String id;
    private final boolean isLoaded;

    Mods() {
        this.id = Lang.asId(name());
        this.isLoaded = Platform.isModLoaded(this.id);
    }

    /**
     * @return a boolean of whether the mod is loaded or not based on mod id
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Execute the runnable if mod is loaded
     * @param runnable Code to run
     */
    public void executeIfInstalled(Supplier<Runnable> runnable) {
        if (isLoaded()) {
            runnable.get().run();
        }
    }
}
