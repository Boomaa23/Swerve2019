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

public class RobotMap {
    public interface CANDevices {
        int GYRO = 8;
    }

    public interface Joysticks {
        int LEFT_JS = 0;
        int RIGHT_JS = 1;
        int BUTTONS = 4;
    }

    public enum MotorGroup {
        FR(0, 1),
        FL(2, 3),
        BL(4, 5),
        BR(6, 7);

        public final int steerCANId, driveCANId;

        MotorGroup(int steerCANId, int driveCANId) {
            this.steerCANId = steerCANId;
            this.driveCANId = driveCANId;
        }
    }

    public interface Dimensions {
        int WHEELBASE = 1;
        int TRACKWIDTH = 1;
    }
}
