package org.patryk3211.powergrid.compat.computercraft;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import org.patryk3211.powergrid.compat.Mods;
import org.patryk3211.powergrid.compat.computercraft.implementation.ComputerBehaviour;

import java.util.function.Function;

public class ComputerCraftProxy {

    private static Function<SmartBlockEntity, ? extends AbstractComputerBehaviour> fallbackFactory;
    private static Function<SmartBlockEntity, ? extends AbstractComputerBehaviour> computerFactory;

    public static void register() {
        fallbackFactory = ComputerBehaviour::new;
        Mods.COMPUTERCRAFT.executeIfInstalled(() -> ComputerCraftProxy::registerWithDependency);
    }

    public static void registerWithDependency() {
        computerFactory = ComputerBehaviour::new;
    }

    public static AbstractComputerBehaviour getBehaviourFor(SmartBlockEntity smartBlockEntity) {
        if (computerFactory == null)
            return fallbackFactory.apply(smartBlockEntity);
        return computerFactory.apply(smartBlockEntity);
    }
}
