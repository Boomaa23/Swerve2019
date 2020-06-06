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

package org.rivierarobotics.subsystems.drivetrain;

import org.rivierarobotics.util.MotorGroup;

public class SwerveModule {
    private final MotorGroup groupId;
    private final DriveSubmodule drive;
    private final SteerSubmodule steer;

    public SwerveModule(MotorGroup groupId) {
        this.groupId = groupId;
        this.drive = new DriveSubmodule(groupId);
        this.steer = new SteerSubmodule(groupId);
    }

    public MotorGroup getGroup() {
        return groupId;
    }

    public DriveSubmodule getDrive() {
        return drive;
    }

    public SteerSubmodule getSteer() {
        return steer;
    }

    public void tick() {
        drive.tickDrivePid();
    }
}
