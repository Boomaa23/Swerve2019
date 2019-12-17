package org.rivierarobotics.util;

public class ControlDirective {
    private double angle, power;

    public ControlDirective(double angle, double power) {
        this.angle = angle;
        this.power = power;
    }

    public double getAngle() {
        return angle;
    }

    public double getPower() {
        return power;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setPower(double power) {
        this.power = power;
    }
}
