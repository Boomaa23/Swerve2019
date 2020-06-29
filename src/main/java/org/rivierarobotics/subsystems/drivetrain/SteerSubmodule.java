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
import org.rivierarobotics.util.MotorGroup;
import org.rivierarobotics.util.MotorUtil;
import org.rivierarobotics.util.PIDConfig;

public class SteerSubmodule implements SwerveSubmodule {
    public static final double TICKS_PER_DEGREE = 4096.0 / 360;
    private static final PIDConfig STEER_PID_CONFIG = new PIDConfig(0.0005, 0, 0, 0, 0, 0.5);
    private final WPI_TalonSRX steer;
    private final MotorGroup groupId;

    public SteerSubmodule(MotorGroup groupId) {
        this.groupId = groupId;
        this.steer = new WPI_TalonSRX(groupId.steerCANId);

        MotorUtil.setupMotionMagic(FeedbackDevice.PulseWidthEncodedPosition, STEER_PID_CONFIG, 0, steer);
        steer.setNeutralMode(NeutralMode.Coast);

        if (groupId.lrSide == MotorGroup.Side.LEFT) {
            steer.setSensorPhase(true);
        } else {
            steer.setInverted(true);
        }
    }

    public void setPower(double pwr) {
        setRawPower(pwr);
    }

    private void setRawPower(double pwr) {
        steer.set(ControlMode.PercentOutput, pwr);
    }

    public void setAngle(double angle) {
        angle *= TICKS_PER_DEGREE;
        steer.set(ControlMode.MotionMagic, angle + groupId.steerZeroTicks);
    }

    public int getAngleTicks() {
        return steer.getSensorCollection().getPulseWidthPosition();
    }

    public double getAngle() {
        return getAngleTicks() / TICKS_PER_DEGREE;
    }

    public WPI_TalonSRX getMotor() {
        return steer;
    }
}
