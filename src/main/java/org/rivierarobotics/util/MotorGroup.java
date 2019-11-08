/*
 * This file is part of Swerve2019, licensed under the GNU General Public License (GPLv3).
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
    FR(0, 1, 0, Side.RIGHT, Side.FRONT),
    FL(2, 3, 0, Side.LEFT, Side.FRONT),
    BL(4, 5, 0, Side.LEFT, Side.BACK),
    BR(6, 7, 0, Side.RIGHT, Side.BACK);

    public final int steerCANId, driveCANId, steerZeroTicks;
    public final Side LRSide, FBSide;

    MotorGroup(int steerCANId, int driveCANId, int steerZeroTicks, Side LRSide, Side FBSide) {
        this.steerCANId = steerCANId;
        this.driveCANId = driveCANId;
        this.steerZeroTicks = steerZeroTicks;
        this.LRSide = LRSide;
        this.FBSide = FBSide;
    }

    public enum Side {
        LEFT, RIGHT, FRONT, BACK
    }
}
