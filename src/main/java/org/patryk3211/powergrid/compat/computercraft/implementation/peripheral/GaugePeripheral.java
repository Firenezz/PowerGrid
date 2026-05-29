package org.patryk3211.powergrid.compat.computercraft.implementation.peripheral;

import com.simibubi.create.compat.computercraft.implementation.peripherals.SyncedPeripheral;
import dan200.computercraft.api.lua.LuaFunction;
import org.patryk3211.powergrid.electricity.gauge.GaugeBlockEntity;

public abstract class GaugePeripheral extends SyncedPeripheral<GaugeBlockEntity> {
    public GaugePeripheral(GaugeBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final float getValue() {
        return blockEntity.getValue();
    }
}
