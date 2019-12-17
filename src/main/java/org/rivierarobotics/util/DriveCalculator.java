package org.rivierarobotics.util;

import org.rivierarobotics.driver.CompositeJoystick;

import java.util.Map;

@FunctionalInterface
public interface DriveCalculator {
    Map<MotorGroup, ControlDirective> calculate(CompositeJoystick composite, MotorGroup... groups);
}
