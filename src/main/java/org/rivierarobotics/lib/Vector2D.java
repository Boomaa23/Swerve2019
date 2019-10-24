package org.rivierarobotics.lib;

import org.rivierarobotics.util.MathUtil;

public class Vector2D {
    private double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public double getDirection() {
        return MathUtil.wrapRadians(Math.atan2(y, x));
    }

    public double getAngle() {
        return Math.toDegrees(getDirection());
    }
}
