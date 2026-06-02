package org.patryk3211.powergrid.compat.computercraft.implementation.peripheral;

import dan200.computercraft.api.lua.LuaFunction;
import org.jetbrains.annotations.NotNull;
import org.patryk3211.powergrid.electricity.gauge.PowerGaugeBlockEntity;
import org.patryk3211.powergrid.utility.Unit;

public class PowerGaugePeripheral extends GaugePeripheral {

    public PowerGaugePeripheral(PowerGaugeBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final String getFormattedInfo() {
        return Unit.POWER.format(this.blockEntity.getValue()).getString();
    }

    @NotNull
    @Override
    public String getType() {
        return "PowerGrid_PowerGauge";
    }
}
