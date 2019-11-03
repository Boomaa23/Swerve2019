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
import org.rivierarobotics.driver.CompositeJoystick;
import org.rivierarobotics.robot.Robot;
import org.rivierarobotics.subsystems.DriveTrain;
import org.rivierarobotics.util.DriveUtil.*;
import org.rivierarobotics.util.RobotMap.MotorGroup;

import java.util.Map;

public class DriveControl extends Command {
    private DriveTrain driveTrain;
    private MotorGroup[] allMotorGroups;
    private CompositeJoystick compositeJoystick;

    public DriveControl() {
        this.driveTrain = Robot.runningRobot.driveTrain;
        this.compositeJoystick = Robot.runningRobot.controller.composite;
        this.allMotorGroups = new MotorGroup[] { MotorGroup.FR, MotorGroup.FL, MotorGroup.BL, MotorGroup.BR };
        requires(driveTrain);
    }

    @Override
    protected void execute() {
        Map<MotorGroup, Double> calcSpeeds = null;
        Map<MotorGroup, Double> calcAngles = null;

        switch(Robot.runningRobot.currentControlMode) {
            case CRAB:
                //TODO implement a crab mode (front/back diff steering)
                break;
            case SWERVE:
                Swerve.control(driveTrain.getGyroAngle(), compositeJoystick);
                calcSpeeds = Swerve.calculate(CalcType.SPEED, allMotorGroups);
                calcAngles = Swerve.calculate(CalcType.ANGLE, allMotorGroups);
                break;
            case AUTOMOBILE:
                //TODO implement an "automobile" mode (back two wheels fixed, front two do steering)
                break;
            case TANK:
                calcSpeeds = Tank.calculate(CalcType.SPEED, compositeJoystick, allMotorGroups);
                calcAngles = Tank.calculate(CalcType.ANGLE, compositeJoystick, allMotorGroups);
                break;
            default:
                throw new IllegalArgumentException("Current control mode could not be determined");
        }

        driveTrain.setMappedValues(CalcType.SPEED, calcSpeeds);
        driveTrain.setMappedValues(CalcType.ANGLE, calcAngles);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }


}
