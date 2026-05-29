package org.patryk3211.powergrid.compat.computercraft.implementation.peripheral;

import dan200.computercraft.api.lua.LuaFunction;
import org.jetbrains.annotations.NotNull;
import org.patryk3211.powergrid.electricity.gauge.GaugeBlockEntity;
import org.patryk3211.powergrid.electricity.gauge.VoltageGaugeBlockEntity;
import org.patryk3211.powergrid.utility.Unit;

public class VoltageGaugePeripheral extends GaugePeripheral{

    public VoltageGaugePeripheral(VoltageGaugeBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final String getFormattedInfo() {
        return Unit.VOLTAGE.format(this.blockEntity.getValue()).getString();
    }

    @NotNull
    @Override
    public String getType() {
        return "PowerGrid_VoltageGauge";
    }
}
