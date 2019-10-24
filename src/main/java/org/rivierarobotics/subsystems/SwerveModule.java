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
import org.rivierarobotics.util.RobotConstants;

import java.util.Arrays;
import java.util.List;

public class SwerveModule {
    private static final double TICKS_TO_INCHES = 0;
    private static final double d_kP = 0, d_kI = 0, d_kD = 0, d_kF = 0;
    private static final double s_kP = 0, s_kI = 0, s_kD = 0, s_kF = 0;
    private final CANSparkMax steer;
    private final CANSparkMax drive;
    private final PIDController steerPID;
    private final PIDController drivePID;

    public SwerveModule(RobotConstants.MotorGroup groupId) {
        this.drive = new CANSparkMax(groupId.driveCANId, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.steer = new CANSparkMax(groupId.steerCANId, CANSparkMaxLowLevel.MotorType.kBrushless);
        drivePID = new PIDController(d_kP, d_kI, d_kD, d_kF, new AbstractPIDSource(this::getSteerAngle), this::setRawDrivePower, 0.01);
        steerPID = new PIDController(s_kP, s_kI, s_kD, s_kF, new AbstractPIDSource(this::getDriveDistance), this::setRawSteerPower, 0.01);
    }

    public void setDrivePower(double pwr) {
        if(pwr != 0 && drivePID.isEnabled()) { drivePID.disable(); }
        setRawDrivePower(pwr);
    }

    private void setRawDrivePower(double pwr) {
        drive.set(pwr);
    }

    private void setRawSteerPower(double pwr) {
        drive.set(pwr);
    }

    public void setDriveDistance(double distance) {
        drivePID.setSetpoint(distance / TICKS_TO_INCHES);
        drivePID.enable();
    }

    public void setWheelAngle(double angle) {
        steerPID.setSetpoint(angle);
        drivePID.enable();
    }

    public double getSteerAngle() {
        return steer.getEncoder().getPosition();
    }

    public double getDriveDistance() {
        return drive.getEncoder().getPosition();
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
