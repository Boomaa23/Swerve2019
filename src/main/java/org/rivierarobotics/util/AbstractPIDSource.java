package org.rivierarobotics.util;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

import java.util.function.DoubleSupplier;

public class AbstractPIDSource implements PIDSource {
    private final DoubleSupplier source;
    private PIDSourceType sourceType = PIDSourceType.kDisplacement;

    public AbstractPIDSource(DoubleSupplier source) {
        this.source = source;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return sourceType;
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        this.sourceType = pidSource;
    }

    @Override
    public double pidGet() {
        return source.getAsDouble();
    }

}