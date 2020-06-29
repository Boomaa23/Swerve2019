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

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.rivierarobotics.util.Dimensions;
import org.rivierarobotics.util.MotorGroup;
import org.rivierarobotics.util.MotorUtil;
import org.rivierarobotics.util.PIDConfig;

public class DriveSubmodule implements SwerveSubmodule {
    public static final double TICKS_PER_METER = 1;
    private static final PIDConfig DRIVE_PID_CONFIG = new PIDConfig(0.0005, 0, 0, 0, 0, 0.5);
    private final WPI_TalonSRX drive;
    private final MotorGroup groupId;

    public DriveSubmodule(MotorGroup groupId) {
        this.groupId = groupId;
        this.drive = new WPI_TalonSRX(groupId.driveCANId);
        MotorUtil.setupMotionMagic(FeedbackDevice.PulseWidthEncodedPosition, DRIVE_PID_CONFIG, 0, drive);
        drive.setNeutralMode(NeutralMode.Brake);
        //TODO figure out inverting and sensor phase
    }

    public void setPower(double pwr) {
        setRawPower(pwr);
    }

    private void setRawPower(double pwr) {
        drive.set(ControlMode.PercentOutput, pwr);
    }

    public void setRelativeDistance(double meters) {
        setDistance(meters + getDistance());
    }

    public void setDistance(double meters) {
        drive.set(ControlMode.MotionMagic, meters * TICKS_PER_METER);
    }

    public int getDistanceTicks() {
        return drive.getSensorCollection().getPulseWidthPosition();
    }

    public double getDistance() {
        return getDistanceTicks() / TICKS_PER_METER;
    }

    public double getVelocity() {
        return getRPM() * 2 * Math.PI * Dimensions.WHEEL_RADIUS / 60;
    }

    private double getRPM() {
        return drive.getSensorCollection().getPulseWidthVelocity();
    }

    public WPI_TalonSRX getMotor() {
        return drive;
    }
}
