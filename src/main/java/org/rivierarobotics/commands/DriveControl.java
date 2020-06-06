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

import edu.wpi.first.wpilibj.command.Command;
import org.rivierarobotics.driver.CompositeJoystick;
import org.rivierarobotics.driver.CurrentControlMode;
import org.rivierarobotics.inject.Input;
import org.rivierarobotics.subsystems.PigeonGyro;
import org.rivierarobotics.subsystems.drivetrain.DriveTrain;
import org.rivierarobotics.util.MotorGroup;

import javax.inject.Inject;

public class DriveControl extends Command {
    private final DriveTrain driveTrain;
    private final PigeonGyro gyro;
    private final CurrentControlMode controlMode;
    private final CompositeJoystick joystick;

    @Inject
    public DriveControl(DriveTrain driveTrain,
                        PigeonGyro gyro,
                        CurrentControlMode controlMode,
                        @Input.Composite(Input.User.DRIVER) CompositeJoystick joystick) {
        this.driveTrain = driveTrain;
        this.gyro = gyro;
        this.controlMode = controlMode;
        this.joystick = joystick;
        requires(driveTrain);
    }

    @Override
    protected void execute() {
        driveTrain.setMappedControlDirective(controlMode.get().calculator.calculate(joystick, gyro, MotorGroup.values()));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
