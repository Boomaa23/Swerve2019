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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.rivierarobotics.util.MotorGroup;
import org.rivierarobotics.util.MotorUtil;
import org.rivierarobotics.util.PIDConfig;

public class SwerveModule {
    public static final double TICKS_TO_INCHES = 1;
    public static final double ANGLE_SCALE = 4096.0 / 360;
    private static final PIDConfig STEER_CONFIG = new PIDConfig(0.0005, 0, 0, 0, 0.5);
    private final WPI_TalonSRX steer;
    private final CANSparkMax drive;
    private final MotorGroup groupId;

    public SwerveModule(MotorGroup groupId) {
        this.groupId = groupId;
        this.drive = new CANSparkMax(groupId.driveCANId, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.steer = new WPI_TalonSRX(groupId.steerCANId);
        MotorUtil.setupMotionMagic(FeedbackDevice.PulseWidthEncodedPosition, STEER_CONFIG, 0, steer);
        steer.setNeutralMode(NeutralMode.Coast);

        if (groupId.lrSide == MotorGroup.Side.LEFT) {
            steer.setSensorPhase(true);
        } else {
            steer.setInverted(true);
        }
        // TODO implement drive PID
    }

    public void setDrivePower(double pwr) {
        setRawDrivePower(pwr);
    }

    private void setRawDrivePower(double pwr) {
        SmartDashboard.putNumber(groupId.name() + " pwr", pwr);
        drive.set(pwr);
    }

    public void setSteerAngle(double angle) {
        angle *= ANGLE_SCALE;
        SmartDashboard.putNumber(groupId.name() + " Setpoint", angle);
        steer.set(ControlMode.MotionMagic, angle);
    }

    public int getDriveDistanceTicks() {
        return (int) drive.getEncoder().getPosition();
    }

    public double getDriveDistance() {
        return getDriveDistanceTicks() / TICKS_TO_INCHES;
    }

    public int getSteerAngleTicks() {
        double ticks = steer.getSensorCollection().getPulseWidthPosition();
        SmartDashboard.putNumber(groupId.name() + " Steer Ticks", ticks);
        return (int) ticks;
    }

    public double getSteerAngle() {
        return getSteerAngleTicks() / ANGLE_SCALE;
    }

    public CANSparkMax getDrive() {
        return drive;
    }

    public WPI_TalonSRX getSteer() {
        return steer;
    }
}
