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

package org.rivierarobotics.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.rivierarobotics.driver.Controller;
import org.rivierarobotics.subsystems.DriveTrain;

public class Robot extends TimedRobot {
    public static Robot runningRobot;
    public DriveTrain driveTrain;
    public Controller controller;

    public Robot() {
        this.driveTrain = new DriveTrain();
        this.controller = new Controller();
        runningRobot = this;
    }

    @Override
    public void robotInit() {
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
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        printShuffleboard();
    }

    @Override
    public void disabledPeriodic() {
        driveTrain.stop();
        printShuffleboard();
    }

    private void printShuffleboard() {
        SmartDashboard.putNumberArray("Drive Distances", this.driveTrain.getDistances());
        SmartDashboard.putNumberArray("Wheel Angles", this.driveTrain.getAngles());
        SmartDashboard.putNumberArray("Drive Powers", this.driveTrain.getAllPowers(true));
        SmartDashboard.putNumberArray("Steering Powers", this.driveTrain.getAllPowers(false));
        SmartDashboard.putNumberArray("Drive Encoder Ticks", this.driveTrain.getDistances());
        SmartDashboard.putNumberArray("Steering Encoder Ticks", this.driveTrain.getAngles());
    }
}
