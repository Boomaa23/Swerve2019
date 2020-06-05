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

package org.rivierarobotics.util;

public enum MotorGroup {
    FR(2, 1, 0, Side.FRONT, Side.RIGHT),
    FL(4, 3, 0, Side.FRONT, Side.LEFT),
    BL(6, 5, 0, Side.BACK, Side.LEFT),
    BR(8, 7, 0, Side.BACK, Side.RIGHT);

    public final int steerCANId;
    public final int driveCANId;
    public final int steerZeroTicks;
    public final Side fbSide;
    public final Side lrSide;

    MotorGroup(int steerCANId, int driveCANId, int steerZeroTicks, Side fbSide, Side lrSide) {
        this.steerCANId = steerCANId;
        this.driveCANId = driveCANId;
        this.steerZeroTicks = steerZeroTicks;
        this.fbSide = fbSide;
        this.lrSide = lrSide;
    }

    public enum Side {
        LEFT, RIGHT, FRONT, BACK
    }
}
