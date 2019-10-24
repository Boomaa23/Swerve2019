package org.rivierarobotics.driver;

import edu.wpi.first.wpilibj.Joystick;

public class CompositeJoystick {
    private BoundedJoystick x, y, z;

    public CompositeJoystick(BoundedJoystick x, BoundedJoystick y, BoundedJoystick z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CompositeJoystick(Joystick x, Joystick y, Joystick z) {
        this.x = (BoundedJoystick) x;
        this.y = (BoundedJoystick) y;
        this.z = (BoundedJoystick) z;
    }

    public double getX() {
        return x.getDimension('X');
    }

    public double getY() {
        return y.getDimension('Y');
    }

    public double getZ() {
        return z.getDimension('Z');
    }
}
