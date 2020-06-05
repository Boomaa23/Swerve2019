/*
 * This file is part of Swerve2020, licensed under the GNU General Public License (GPLv3).
 *
 * Copyright (c) Riviera Robotics <https://github.com/Team5818>
 * Copyright (c) contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.rivierarobotics.driver;

import edu.wpi.first.wpilibj.Joystick;

public class CompositeJoystick {
    private final BoundedJoystick x;
    private final BoundedJoystick y;
    private final BoundedJoystick z;
    private final BoundedJoystick buttons;

    public CompositeJoystick(BoundedJoystick x, BoundedJoystick y, BoundedJoystick z, BoundedJoystick buttons) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.buttons = buttons;
    }

    public CompositeJoystick(Joystick x, Joystick y, Joystick z, Joystick buttons) {
        this.x = (BoundedJoystick) x;
        this.y = (BoundedJoystick) y;
        this.z = (BoundedJoystick) z;
        this.buttons = (BoundedJoystick) buttons;
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

    public BoundedJoystick getButtons() {
        return buttons;
    }
}
