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

package org.rivierarobotics.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.rivierarobotics.inject.CommandComponent;
import org.rivierarobotics.inject.DaggerGlobalComponent;
import org.rivierarobotics.inject.GlobalComponent;

public class Robot extends TimedRobot {
    private GlobalComponent globalComponent;
    private CommandComponent commandComponent;

    @Override
    public void robotInit() {
        this.globalComponent = DaggerGlobalComponent.create();
        this.commandComponent = globalComponent.getCommandComponentBuilder().build();
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        printShuffleboard();
    }

    @Override
    public void teleopInit() {
        globalComponent.getGyro().reset();
        globalComponent.getDriveTrain().resetDriveEncoders();
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        printShuffleboard();
    }

    @Override
    public void disabledPeriodic() {
        printShuffleboard();
    }

    private void printShuffleboard() {
        SmartDashboard.putNumber("gyro", globalComponent.getGyro().getAngle());
        SmartDashboard.putNumber("Y", globalComponent.getDriverJoystick().getY());
        SmartDashboard.putNumber("X", globalComponent.getDriverJoystick().getX());
        SmartDashboard.putNumber("Z", globalComponent.getDriverJoystick().getZ());
        if (!globalComponent.getDriveTrain().getCurrentCommandName().equals("DriveControl")) {
            SmartDashboard.putString("Current Command", globalComponent.getDriveTrain().getCurrentCommandName());
        }
    }
}
