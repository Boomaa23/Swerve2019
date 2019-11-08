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

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import org.rivierarobotics.util.AbstractPIDSource;
import org.rivierarobotics.util.MathUtil;
import org.rivierarobotics.util.MotorGroup;

public class SwerveModule {
    public static final double TICKS_TO_INCHES = 1, ANGLE_SCALE = 4096.0 / 360,
            MAX_PID_STEER = 0.5, MAX_PID_DRIVE = 0.5;
    private static final double d_kP = 0, d_kI = 0, d_kD = 0, d_kF = 0;
    private static final double s_kP = 0, s_kI = 0, s_kD = 0, s_kF = 0;
    private final WPI_TalonSRX steer;
    private final CANSparkMax drive;
    private final PIDController steerPID, drivePID;
    private final MotorGroup groupId;

    public SwerveModule(MotorGroup groupId) {
        this.groupId = groupId;
        this.drive = new CANSparkMax(groupId.driveCANId, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.steer = new WPI_TalonSRX(groupId.steerCANId);
        this.drivePID = new PIDController(s_kP, s_kI, s_kD, s_kF,
                new AbstractPIDSource(this::getDriveDistanceTicks), this::setRawSteerPower, 0.01);
        this.steerPID = new PIDController(d_kP, d_kI, d_kD, d_kF,
                new AbstractPIDSource(() -> MathUtil.moduloPositive(getSteerAngleTicks(), 4096)),
                this::setRawDrivePower, 0.01);

        steerPID.setInputRange(0, 4096);
        steerPID.setContinuous(true);
        steerPID.setOutputRange(-MAX_PID_STEER, MAX_PID_STEER);
        drivePID.setOutputRange(-MAX_PID_DRIVE, MAX_PID_DRIVE);
    }

    public void setDrivePower(double pwr) {
        if(pwr != 0 && drivePID.isEnabled()) { drivePID.disable(); }
        setRawDrivePower(pwr);
    }

    private void setRawDrivePower(double pwr) {
        drive.set(pwr);
    }

    private void setRawSteerPower(double pwr) {
        steer.set(pwr);
    }

    public void setDriveDistance(double distance) {
        drivePID.setSetpoint((distance / TICKS_TO_INCHES) + getDriveDistanceTicks());
        drivePID.enable();
    }

    public void setSteerAngle(double angle) {
        steerPID.setSetpoint((angle * ANGLE_SCALE) + groupId.steerZeroTicks);
        steerPID.enable();
    }

    public int getDriveDistanceTicks() {
        return (int) drive.getEncoder().getPosition();
    }

    public double getDriveDistance() {
        return getDriveDistanceTicks() / TICKS_TO_INCHES;
    }

    public int getSteerAngleTicks() {
        return steer.getSensorCollection().getPulseWidthPosition();
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

    public PIDController getPIDController(SpeedController controller) {
        return controller instanceof CANSparkMax && controller.equals(drive) ? drivePID
                : controller instanceof WPI_TalonSRX && controller.equals(steer) ? steerPID : null;
    }
}
