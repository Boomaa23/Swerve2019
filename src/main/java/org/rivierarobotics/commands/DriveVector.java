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

package org.rivierarobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import net.octyl.aptcreator.GenerateCreator;
import net.octyl.aptcreator.Provided;
import org.rivierarobotics.subsystems.drivetrain.DriveTrain;
import org.rivierarobotics.subsystems.drivetrain.SwerveData;
import org.rivierarobotics.util.MotorMapped;

@GenerateCreator
public class DriveVector extends InstantCommand {
    private final DriveTrain driveTrain;
    private final double distance;
    private final double angle;

    public DriveVector(@Provided DriveTrain driveTrain, double distance, double angle) {
        this.driveTrain = driveTrain;
        this.distance = distance;
        this.angle = Math.toRadians(angle);
        requires(driveTrain);
    }

    @Override
    public void execute() {
        driveTrain.setAll(SwerveData.ANGLE, MotorMapped.fromDouble(angle));
        driveTrain.setAll(SwerveData.DISTANCE_RELATIVE, MotorMapped.fromDouble(distance));
    }
}
