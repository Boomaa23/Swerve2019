package org.rivierarobotics.util;

import org.rivierarobotics.util.DriveUtil.*;

public enum ControlMode {
    SWERVE(Swerve.class),
    TANK(Tank.class),
    CRAB(Crab.class),
    AUTOMOBILE(Automobile.class);

    public Class<?> controlClass;

    ControlMode(Class<?> controlClass) {
        this.controlClass = controlClass;
    }
}
