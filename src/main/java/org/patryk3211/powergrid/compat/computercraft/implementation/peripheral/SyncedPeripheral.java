package org.patryk3211.powergrid.compat.computercraft.implementation.peripheral;

import com.simibubi.create.compat.computercraft.events.ComputerEvent;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.patryk3211.powergrid.compat.computercraft.AbstractComputerBehaviour;
import org.patryk3211.powergrid.electricity.gauge.GaugeBlockEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class SyncedPeripheral<T extends SmartBlockEntity> implements IPeripheral {

    protected final T blockEntity;
    private final List<@NotNull IComputerAccess> computers = new ArrayList<>();

    public SyncedPeripheral(T blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public void attach(@NotNull IComputerAccess computer) {
        synchronized (computers) {
            computers.add(computer);
            if (computers.size() == 1)
                onFirstAttach();
            updateBlockEntity();
        }
    }

    protected void onFirstAttach() {}

    @Override
    public void detach(@NotNull IComputerAccess computer) {
        synchronized (computers) {
            computers.remove(computer);
            updateBlockEntity();
            if (computers.isEmpty())
                onLastDetach();
        }
    }

    protected void onLastDetach() {}

    private void updateBlockEntity() {
        boolean hasAttachedComputer = !computers.isEmpty();
        if (blockEntity instanceof GaugeBlockEntity gauge && gauge.computerBehaviour != null) {
            gauge.computerBehaviour.setHasAttachedComputer(hasAttachedComputer);
            return;
        }
        var behaviour = blockEntity.getBehaviour(AbstractComputerBehaviour.TYPE);
        if (behaviour != null)
            behaviour.setHasAttachedComputer(hasAttachedComputer);
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other;
    }

    public void prepareComputerEvent(@NotNull ComputerEvent event) {}

    protected void queueEvent(@NotNull String event, @Nullable Object... arguments) {
        Object[] sourceAndArgs = new Object[arguments.length + 1];
        System.arraycopy(arguments, 0, sourceAndArgs, 1, arguments.length);
        synchronized (computers) {
            for (IComputerAccess computer : computers) {
                sourceAndArgs[0] = computer.getAttachmentName();
                computer.queueEvent(event, sourceAndArgs);
            }
        }
    }
}
