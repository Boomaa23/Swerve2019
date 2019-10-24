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

package org.rivierarobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.rivierarobotics.robot.Robot;
import org.rivierarobotics.subsystems.DriveTrain;
import org.rivierarobotics.util.RobotConstants.MotorGroup;
import org.rivierarobotics.util.SwerveUtil;

import static org.rivierarobotics.util.SwerveUtil.calcWheelAngle;
import static org.rivierarobotics.util.SwerveUtil.calcWheelSpeed;

public class SwerveControl extends Command {
    private DriveTrain driveTrain;

    public SwerveControl() {
        this.driveTrain = Robot.driveTrain;
        requires(driveTrain);
    }

    @Override
    protected void execute() {
        SwerveUtil.swerveControl(driveTrain.getRobotAngle());

        driveTrain.setPower(calcWheelSpeed(MotorGroup.FR), calcWheelSpeed(MotorGroup.FL),
                calcWheelSpeed(MotorGroup.BL), calcWheelSpeed(MotorGroup.BR));
        driveTrain.setAngle(calcWheelAngle(MotorGroup.FR), calcWheelAngle(MotorGroup.FL),
                calcWheelAngle(MotorGroup.BL), calcWheelAngle(MotorGroup.BR));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
