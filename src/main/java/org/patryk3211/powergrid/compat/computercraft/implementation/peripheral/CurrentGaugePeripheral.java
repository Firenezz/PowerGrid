package org.patryk3211.powergrid.compat.computercraft.implementation.peripheral;

import dan200.computercraft.api.lua.LuaFunction;
import org.jetbrains.annotations.NotNull;
import org.patryk3211.powergrid.electricity.gauge.CurrentGaugeBlockEntity;
import org.patryk3211.powergrid.utility.Unit;

public class CurrentGaugePeripheral extends GaugePeripheral {

    public CurrentGaugePeripheral(CurrentGaugeBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final String getFormattedInfo() {
        return Unit.CURRENT.format(this.blockEntity.getValue()).getString();
    }

    @NotNull
    @Override
    public String getType() {
        return "PowerGrid_CurrentGauge";
    }
}
