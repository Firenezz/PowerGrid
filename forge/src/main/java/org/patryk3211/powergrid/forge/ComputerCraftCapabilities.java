/*
 * Copyright 2025 patryk3211
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.patryk3211.powergrid.forge;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.patryk3211.powergrid.collections.ModdedBlockEntities;
import org.patryk3211.powergrid.compat.Mods;
import org.patryk3211.powergrid.compat.computercraft.AbstractComputerBehaviour;
import org.patryk3211.powergrid.electricity.dac.DacBlock;
import org.patryk3211.powergrid.electricity.dac.DacBlockEntity;
import org.patryk3211.powergrid.electricity.gauge.CurrentGaugeBlockEntity;
import org.patryk3211.powergrid.electricity.gauge.PowerGaugeBlockEntity;
import org.patryk3211.powergrid.electricity.gauge.VoltageGaugeBlockEntity;

public class ComputerCraftCapabilities {

    public static void register(RegisterCapabilitiesEvent event) {
        if (!Mods.COMPUTERCRAFT.isLoaded())
            return;

        registerComputerPeripheral(event, ModdedBlockEntities.VOLTAGE_METER.get(),
                (VoltageGaugeBlockEntity be) -> be.computerBehaviour);
        registerComputerPeripheral(event, ModdedBlockEntities.CURRENT_METER.get(),
                (CurrentGaugeBlockEntity be) -> be.computerBehaviour);
        registerComputerPeripheral(event, ModdedBlockEntities.POWER_METER.get(),
                (PowerGaugeBlockEntity be) -> be.computerBehaviour);
        registerComputerPeripheral(event, ModdedBlockEntities.DAC.get(),
                (DacBlockEntity be) -> be.computerBehaviour,
                (be, side) -> side == DacBlock.getPeripheralFace(be.getBlockState()));
    }

    private static <BE extends SmartBlockEntity> void registerComputerPeripheral(
            RegisterCapabilitiesEvent event,
            BlockEntityType<BE> type,
            ComputerBehaviourGetter<BE> getter) {
        registerComputerPeripheral(event, type, getter, (be, side) -> true);
    }

    private static <BE extends SmartBlockEntity> void registerComputerPeripheral(
            RegisterCapabilitiesEvent event,
            BlockEntityType<BE> type,
            ComputerBehaviourGetter<BE> getter,
            PeripheralSideFilter<BE> sideFilter) {
        event.registerBlockEntity(
                PeripheralCapability.get(),
                type,
                (be, side) -> {
                    if (!sideFilter.test(be, side))
                        return null;
                    var behaviour = getter.get(be);
                    return behaviour != null ? behaviour.getPeripheralCapability() : null;
                }
        );
    }

    @FunctionalInterface
    private interface ComputerBehaviourGetter<BE extends SmartBlockEntity> {
        AbstractComputerBehaviour get(BE blockEntity);
    }

    @FunctionalInterface
    private interface PeripheralSideFilter<BE extends SmartBlockEntity> {
        boolean test(BE blockEntity, Direction side);
    }
}
