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

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.controller.PIDController;
import org.rivierarobotics.util.Dimensions;
import org.rivierarobotics.util.MotorGroup;
import org.rivierarobotics.util.PIDConfig;

public class DriveSubmodule implements SwerveSubmodule {
    public static final double TICKS_PER_METER = 1;
    private static final PIDConfig DRIVE_PID_CONFIG = new PIDConfig(0.0005, 0, 0, 0, 0, 0.5);
    private final PIDController drivePID;
    private final CANSparkMax drive;
    private final MotorGroup groupId;
    private boolean drivePidEnabled;

    public DriveSubmodule(MotorGroup groupId) {
        this.groupId = groupId;
        this.drive = new CANSparkMax(groupId.driveCANId, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.drivePID = new PIDController(DRIVE_PID_CONFIG.getP(), DRIVE_PID_CONFIG.getI(), DRIVE_PID_CONFIG.getD());

        drivePID.setTolerance(DRIVE_PID_CONFIG.getTolerance());
        this.drivePidEnabled = false;
    }

    public void setPower(double pwr) {
        drivePidEnabled = false;
        setRawPower(pwr);
    }

    private void setRawPower(double pwr) {
        drive.set(pwr);
    }

    public void setRelativeDistance(double meters) {
        setDistance(meters + getDistance());
    }

    public void setDistance(double meters) {
        this.drivePidEnabled = true;
        drivePID.setSetpoint(meters * TICKS_PER_METER);
    }

    public int getDistanceTicks() {
        return (int) drive.getEncoder().getPosition();
    }

    public double getDistance() {
        return getDistanceTicks() / TICKS_PER_METER;
    }

    public double getVelocity() {
        return getRPM() * 2 * Math.PI * Dimensions.WHEEL_RADIUS / 60;
    }

    private double getRPM() {
        return drive.getEncoder().getVelocity();
    }

    public void tickDrivePid() {
        if (drivePidEnabled) {
            setRawPower(Math.min(DRIVE_PID_CONFIG.getRange(),
                    Math.max(-DRIVE_PID_CONFIG.getRange(), drivePID.calculate(getDistanceTicks()))));
        }
    }

    public CANSparkMax getMotor() {
        return drive;
    }
}
