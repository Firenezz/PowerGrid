package org.patryk3211.powergrid.compat.computercraft.implementation.peripheral;

import dan200.computercraft.api.lua.LuaFunction;
import org.jetbrains.annotations.NotNull;
import org.patryk3211.powergrid.electricity.dac.DacBlockEntity;
import org.patryk3211.powergrid.utility.Unit;

public class DacPeripheral extends SyncedPeripheral<DacBlockEntity> {

    public DacPeripheral(DacBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final void setLevel(int level) {
        blockEntity.setOutputLevel(level);
        blockEntity.setComputerControlled(true);
    }

    @LuaFunction
    public final int getLevel() {
        return blockEntity.getOutputLevel();
    }

    @LuaFunction
    public final double getTargetVoltage() {
        return blockEntity.getTargetVoltage();
    }

    @LuaFunction
    public final float getMeasuredVoltage() {
        return blockEntity.getMeasuredVoltage();
    }

    @NotNull
    @Override
    public String getType() {
        return "PowerGrid_DAC";
    }
}
