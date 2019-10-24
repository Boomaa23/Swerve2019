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

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.PIDController;
import org.rivierarobotics.util.AbstractPIDSource;
import org.rivierarobotics.util.MathUtil;
import org.rivierarobotics.util.RobotConstants;

import java.util.Arrays;
import java.util.List;

public class SwerveModule {
    private static final double P = 0, I = 0, D = 0, F = 0;
    private final CANSparkMax steer;
    private final CANSparkMax drive;
    private final PIDController pidLoop;

    public SwerveModule(RobotConstants.MotorGroup groupId) {
        this.drive = new CANSparkMax(groupId.driveCANId, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.steer = new CANSparkMax(groupId.steerCANId, CANSparkMaxLowLevel.MotorType.kBrushless);
        pidLoop = new PIDController(P, I, D, F, new AbstractPIDSource(this::getTurnAngle), this::setDrivePower, 0.01);
    }

    public void setDrivePower(double pwr) {
        drive.set(pwr);
    }

    public void setWheelAngle(double angle) {
        pidLoop.setSetpoint(angle);
    }

    public double getTurnAngle() {
        return steer.getEncoder().getPosition();
    }

    public List<CANSparkMax> getModuleMotors() {
        return Arrays.asList(drive, steer);
    }

    public CANSparkMax getDrive() {
        return drive;
    }

    public CANSparkMax getSteer() {
        return steer;
    }
}
