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
package org.patryk3211.powergrid.electricity.dac;

import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.patryk3211.powergrid.compat.computercraft.AbstractComputerBehaviour;
import org.patryk3211.powergrid.compat.computercraft.ComputerCraftProxy;
import org.patryk3211.powergrid.electricity.base.ElectricBehaviour;
import org.patryk3211.powergrid.electricity.base.ElectricBlockEntity;
import org.patryk3211.powergrid.electricity.gauge.GaugeValueBehaviour;
import org.patryk3211.powergrid.electricity.sim.ElectricWire;
import org.patryk3211.powergrid.electricity.sim.node.IElectricNode;
import org.patryk3211.powergrid.electricity.sim.node.VoltageSourceCoupling;
import org.patryk3211.powergrid.utility.Lang;
import org.patryk3211.powergrid.utility.Unit;

import java.util.List;

import static org.patryk3211.powergrid.kinetics.motor.ElectricMotorBlockEntity.calculateSpeed;

public class DacBlockEntity extends ElectricBlockEntity {
    public static final float[] MAX_VALUES = new float[] { 2, 20, 120 };

    public static final int DEFAULT_LEVEL = 1;
    public static final int MAX_LEVEL = 256;

    public AbstractComputerBehaviour computerBehaviour;

    protected ElectricBehaviour electricBehaviour;
    private ScrollValueBehaviour value;
    private VoltageSourceCoupling source;
    private IElectricNode vout;
    private IElectricNode vref;
    private ElectricWire control;

    private int outputLevel;
    private boolean computerControlled;

    public float prevDialState;
    public float dialState;
    private float dialTarget;

    public DacBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        electricBehaviour = new ElectricBehaviour(this);
        behaviours.add(electricBehaviour);

        value  = new ScrollValueBehaviour(CreateLang.translateDirect("kinetics.creative_motor.rotation_speed"),
                this, new DacBlockEntity.BoxTransform());

        value.between(0,MAX_LEVEL).setValue(DEFAULT_LEVEL);
        behaviours.add(value);
        behaviours.add(computerBehaviour = ComputerCraftProxy.getBehaviourFor(this));
    }

    @Override
    public void buildCircuit(CircuitBuilder builder) {
        builder.setTerminalCount(3);
        vout = builder.terminalNode(0);
        vref = builder.terminalNode(1);

        control = builder.connect(1000f,  builder.terminalNode(2), builder.terminalNode(1));

        source = builder.addInternalNode(VoltageSourceCoupling.class, vout, vref, 1e-4f);
        applyOutputVoltage();
    }

    @Override
    public void tick() {
        super.tick();

        prevDialState = dialState;
        dialState += (dialTarget - dialState) * .125f;

        if(!level.isClientSide || isVirtual()) {
            applyOutputVoltage();
            notifyUpdate();
        }
    }

    public void setOutputLevel(int level) {
        outputLevel = Mth.clamp(level + 1, 1, 256);
        applyOutputVoltage();
        setChanged();
    }

    public int getOutputLevel() {
        return outputLevel;
    }

    public double getTargetVoltage() {
        return ((outputLevel / 256f) * control.potentialDifference()) + vref.getVoltage();
    }

    public float getMeasuredVoltage() {
        if (vout == null || vref == null)
            return 0;
        return (float) (vout.getVoltage() - vref.getVoltage());
    }

    public void setComputerControlled(boolean computerControlled) {
        this.computerControlled = computerControlled;
    }

    public boolean isComputerControlled() {
        return computerControlled;
    }

    private void applyOutputVoltage() {
        dialTarget = getOutputLevel() / 256f;
        if (source != null)
            source.setVoltage(getTargetVoltage());
    }

    @Override
    public void remove() {
        super.remove();
        if(electricBehaviour != null) {
            electricBehaviour.remove();
        }
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        outputLevel = tag.getInt("OutputLevel");
        computerControlled = tag.getBoolean("ComputerControlled");
        applyOutputVoltage();
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putInt("OutputLevel", outputLevel);
        tag.putBoolean("ComputerControlled", computerControlled);
    }

    public static class BoxTransform extends CenteredSideValueBoxTransform {
        public BoxTransform() {
            super((state, dir) -> dir == Direction.UP);
        }

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8.0f, 8.0f, 14.5f);
        }
    }
}
