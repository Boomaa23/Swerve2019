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

package org.rivierarobotics.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.rivierarobotics.commands.SwerveControl;
import org.rivierarobotics.util.RobotConstants;

public class DriveTrain extends Subsystem {
    private final SwerveModule fl, fr, bl, br;
    private final PigeonIMU gyro;

    public DriveTrain() {
        this.fl = new SwerveModule(RobotConstants.MotorGroups.FL);
        this.fr = new SwerveModule(RobotConstants.MotorGroups.FR);
        this.bl = new SwerveModule(RobotConstants.MotorGroups.BL);
        this.br = new SwerveModule(RobotConstants.MotorGroups.BR);
        this.gyro = new PigeonIMU(RobotConstants.CANDevices.GYRO);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new SwerveControl());
    }
}
