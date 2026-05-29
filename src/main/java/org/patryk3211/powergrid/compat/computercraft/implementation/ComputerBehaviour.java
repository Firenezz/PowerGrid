package org.patryk3211.powergrid.compat.computercraft.implementation;

import com.simibubi.create.compat.computercraft.events.ComputerEvent;
import com.simibubi.create.compat.computercraft.implementation.luaObjects.PackageLuaObject;
import com.simibubi.create.compat.computercraft.implementation.peripherals.SyncedPeripheral;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dan200.computercraft.api.detail.VanillaDetailRegistries;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;
import org.patryk3211.powergrid.compat.computercraft.AbstractComputerBehaviour;
import org.patryk3211.powergrid.compat.computercraft.implementation.peripheral.VoltageGaugePeripheral;
import org.patryk3211.powergrid.electricity.gauge.VoltageGaugeBlockEntity;

import java.util.function.Supplier;

public class ComputerBehaviour extends AbstractComputerBehaviour {

    SyncedPeripheral<?> peripheral;
    Supplier<SyncedPeripheral<?>> peripheralSupplier;
    SmartBlockEntity smartBlockEntity;

    public ComputerBehaviour(SmartBlockEntity be) {
        super(be);
        this.peripheralSupplier = getPeripheralFor(be);
        this.smartBlockEntity = be;
    }

    private Supplier<SyncedPeripheral<?>> getPeripheralFor(SmartBlockEntity be) {
        if (be instanceof VoltageGaugeBlockEntity vgbe)
            return () -> new VoltageGaugePeripheral(vgbe);

        throw new IllegalArgumentException(
                "No peripheral available for " + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType()));
    }

    public static void registerItemDetailProviders() {
        VanillaDetailRegistries.ITEM_STACK.addProvider((out, stack) -> {
            if (PackageItem.isPackage(stack)) {
                PackageLuaObject packageLuaObject = new PackageLuaObject(null, stack);
                out.put("package", packageLuaObject);
            }
        });
    }

    @Override
    public IPeripheral getPeripheralCapability() {
        if (peripheral == null)
            peripheral = peripheralSupplier.get();
        return peripheral;
    }

    @Override
    public void prepareComputerEvent(@NotNull ComputerEvent event) {
        if (peripheral != null)
            peripheral.prepareComputerEvent(event);
    }
}
