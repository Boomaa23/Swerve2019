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
import edu.wpi.first.wpilibj.geometry.Pose2d;
import org.rivierarobotics.driver.CompositeJoystick;
import org.rivierarobotics.inject.CommandComponent;
import org.rivierarobotics.inject.DaggerGlobalComponent;
import org.rivierarobotics.inject.GlobalComponent;
import org.rivierarobotics.subsystems.drivetrain.DriveTrain;
import org.rivierarobotics.subsystems.drivetrain.SwerveData;
import org.rivierarobotics.util.MotorGroup;
import org.rivierarobotics.util.MotorMapped;
import org.rivierarobotics.util.RSTab;
import org.rivierarobotics.util.RobotShuffleboard;

import java.util.Map;

public class Robot extends TimedRobot {
    private GlobalComponent globalComponent;
    private CommandComponent commandComponent;

    @Override
    public void robotInit() {
        this.globalComponent = DaggerGlobalComponent.create();
        this.commandComponent = globalComponent.getCommandComponentBuilder().build();
        globalComponent.robotInit();
    }

    @Override
    public void robotPeriodic() {
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
        globalComponent.getGyro().requireOdometryReset();
        globalComponent.getButtonConfiguration().initTeleop();
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        printShuffleboard();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
        printShuffleboard();
    }

    private void printShuffleboard() {
        RobotShuffleboard shuffleboard = globalComponent.getShuffleboard();
        RSTab generalTab = shuffleboard.getTab("General");
        DriveTrain dt = globalComponent.getDriveTrain();
        CompositeJoystick js = globalComponent.getDriverJoystick();
        generalTab.setEntry("JS X", js.getX());
        generalTab.setEntry("JS Y", js.getY());
        generalTab.setEntry("JS Z", js.getZ());

        generalTab.setEntry("Gyro", globalComponent.getGyro().getWrappedAngle());
        Pose2d currentPosition = dt.getCurrentPosition();
        generalTab.setEntry("Pos X", currentPosition.getTranslation().getX());
        generalTab.setEntry("Pos Y", currentPosition.getTranslation().getY());
        generalTab.setEntry("Pos Rotation", currentPosition.getRotation().getDegrees());

        String currentCommandName = dt.getCurrentCommandName();
        if (!currentCommandName.equals("DriveControl")) {
            generalTab.setEntry("Current Command", currentCommandName);
        }
        generalTab.setEntry("Control Mode", globalComponent.getCurrentControlMode().get().name());

        RSTab motorTab = shuffleboard.getTab("Motors");
        printAll(dt.getAll(SwerveData.POWER), "Speed", motorTab);
        printAll(dt.getAll(SwerveData.ANGLE), "Angle", motorTab);
        printAll(dt.getAll(SwerveData.ANGLE_TICKS), "Angle Ticks", motorTab);
    }

    private void printAll(MotorMapped<Double> data, String name, RSTab tab) {
        for (Map.Entry<MotorGroup, Double> dataEntry : data.entrySet()) {
            tab.setEntry(dataEntry.getKey().name() + " " + name, dataEntry.getValue());
        }
    }
}
