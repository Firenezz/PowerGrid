package org.patryk3211.powergrid.compat.computercraft;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

public class FallbackComputerBehaviour extends AbstractComputerBehaviour {
    public FallbackComputerBehaviour(SmartBlockEntity be) {
        super(be);
    }

    @Override
    public boolean hasAttachedComputer() {
        return false;
    }
}
