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

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.patryk3211.powergrid.collections.ModdedBlockEntities;
import org.patryk3211.powergrid.electricity.base.HorizontalElectricBlock;
import org.patryk3211.powergrid.electricity.base.IDecoratedTerminal;
import org.patryk3211.powergrid.electricity.base.TerminalBoundingBox;
import org.patryk3211.powergrid.electricity.gauge.IGaugeBlock;

public class DacBlock extends HorizontalElectricBlock implements IBE<DacBlockEntity>, IGaugeBlock {

    private static final TerminalBoundingBox[] TERMINALS_NORTH = new TerminalBoundingBox[] {
            new TerminalBoundingBox(IDecoratedTerminal.CONNECTOR, 6, 13, 13, 10, 15, 15)
                    .withColor(IDecoratedTerminal.GRAY),
            new TerminalBoundingBox(IDecoratedTerminal.NEGATIVE, 0, 13, 6, 2, 15, 10)
                    .withColor(IDecoratedTerminal.BLUE),
            new TerminalBoundingBox(IDecoratedTerminal.INPUT, 14, 13, 6, 16, 15, 10)
                    .withColor(IDecoratedTerminal.GREEN)
    };

    private static final VoxelShape SHAPE = box(1, 0, 2, 15, 14, 14);

    public DacBlock(Properties settings) {
        super(settings);
        setTerminalCollection(horizontalNorthTerminals(this, TERMINALS_NORTH, SHAPE));
    }

    /** Bottom face — green control terminal and wired modem attach here. */
    public static Direction getPeripheralFace(BlockState _state) {
        return Direction.DOWN;
    }

    @Override
    public Class<DacBlockEntity> getBlockEntityClass() {
        return DacBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DacBlockEntity> getBlockEntityType() {
        return ModdedBlockEntities.DAC.get();
    }

    @Override
    public boolean shouldRenderHeadOnFace(Level world, BlockPos pos, BlockState state, Direction dir) {
        var facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return dir.getAxis() == facing.getAxis();
    }
}
