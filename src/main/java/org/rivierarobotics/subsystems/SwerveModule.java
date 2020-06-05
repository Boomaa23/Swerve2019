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

package org.rivierarobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.rivierarobotics.util.MotorGroup;
import org.rivierarobotics.util.MotorUtil;
import org.rivierarobotics.util.PIDConfig;

public class SwerveModule {
    public static final double TICKS_PER_INCH = 1;
    public static final double TICKS_PER_DEGREE = 4096.0 / 360;
    private static final PIDConfig STEER_PID_CONFIG = new PIDConfig(0.0005, 0, 0, 0, 0, 0.5);
    private static final PIDConfig DRIVE_PID_CONFIG = new PIDConfig(0.0005, 0, 0, 0, 0, 0.5);
    private final PIDController drivePID;
    private final WPI_TalonSRX steer;
    private final CANSparkMax drive;
    private final MotorGroup groupId;
    private boolean drivePidEnabled;

    public SwerveModule(MotorGroup groupId) {
        this.groupId = groupId;
        this.drive = new CANSparkMax(groupId.driveCANId, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.steer = new WPI_TalonSRX(groupId.steerCANId);
        this.drivePID = new PIDController(DRIVE_PID_CONFIG.getP(), DRIVE_PID_CONFIG.getI(), DRIVE_PID_CONFIG.getD());

        MotorUtil.setupMotionMagic(FeedbackDevice.PulseWidthEncodedPosition, STEER_PID_CONFIG, 0, steer);
        steer.setNeutralMode(NeutralMode.Coast);
        drivePID.setTolerance(DRIVE_PID_CONFIG.getTolerance());
        this.drivePidEnabled = false;

        if (groupId.lrSide == MotorGroup.Side.LEFT) {
            steer.setSensorPhase(true);
        } else {
            steer.setInverted(true);
        }
    }

    public void setDrivePower(double pwr) {
        drivePidEnabled = false;
        setRawDrivePower(pwr);
    }

    private void setRawDrivePower(double pwr) {
        SmartDashboard.putNumber(groupId.name() + " pwr", pwr);
        drive.set(pwr);
    }

    public void setSteerAngle(double angle) {
        angle *= TICKS_PER_DEGREE;
        SmartDashboard.putNumber(groupId.name() + " Setpoint", angle);
        steer.set(ControlMode.MotionMagic, angle);
    }

    public void setRelativeDriveDistance(double inches) {
        setDriveDistance(inches + getDriveDistance());
    }

    public void setDriveDistance(double inches) {
        this.drivePidEnabled = true;
        drivePID.setSetpoint((inches * TICKS_PER_INCH) + groupId.steerZeroTicks);
    }

    public int getDriveDistanceTicks() {
        return (int) drive.getEncoder().getPosition();
    }

    public double getDriveDistance() {
        return getDriveDistanceTicks() / TICKS_PER_INCH;
    }

    public int getSteerAngleTicks() {
        double ticks = steer.getSensorCollection().getPulseWidthPosition();
        SmartDashboard.putNumber(groupId.name() + " Steer Ticks", ticks);
        return (int) ticks;
    }

    public double getSteerAngle() {
        return getSteerAngleTicks() / TICKS_PER_DEGREE;
    }

    public void tickDrivePid() {
        if (drivePidEnabled) {
            setRawDrivePower(Math.min(DRIVE_PID_CONFIG.getRange(),
                    Math.max(-DRIVE_PID_CONFIG.getRange(), drivePID.calculate(getDriveDistanceTicks()))));
        }
    }

    public CANSparkMax getDrive() {
        return drive;
    }

    public WPI_TalonSRX getSteer() {
        return steer;
    }
}
