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
import org.rivierarobotics.util.Dimensions;
import org.rivierarobotics.util.MotorGroup;
import org.rivierarobotics.util.MultiPID;
import org.rivierarobotics.util.PIDConfig;

public class DriveSubmodule implements SwerveSubmodule {
    //TODO record ticks per meter
    public static final double TICKS_PER_METER = 1;
    private final MultiPID pid;
    private final CANSparkMax drive;
    private final MotorGroup groupId;

    public DriveSubmodule(MotorGroup groupId) {
        this.groupId = groupId;
        this.drive = new CANSparkMax(groupId.driveCANId, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.pid = new MultiPID(new PIDConfig(0.005, 0, 0), new PIDConfig(0.005, 0, 0));
        //TODO do PID tuning for both pos and vel
    }

    public void setPower(double pwr) {
        pid.selectMode(MultiPID.Mode.NONE);
        setRawPower(pwr);
    }

    private void setRawPower(double pwr) {
        drive.set(pwr);
    }

    public void setRelativeDistance(double meters) {
        setDistance(meters + getDistance());
    }

    public void setDistance(double meters) {
        pid.selectMode(MultiPID.Mode.POSITION);
        pid.setSetpoint(meters * TICKS_PER_METER);
    }

    public void setVelocity(double metersPerSec) {
        pid.selectMode(MultiPID.Mode.VELOCITY);
        pid.setSetpoint(metersPerSec * TICKS_PER_METER);
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

    public double getPower() {
        return drive.get();
    }

    public void tickPID() {
        if (pid.getSelectedMode() == MultiPID.Mode.POSITION) {
            setRawPower(pid.calculatePos(getDistanceTicks()));
        } else if (pid.getSelectedMode() == MultiPID.Mode.VELOCITY) {
            setRawPower(pid.calculateVel(getVelocity()));
        }
    }

    public CANSparkMax getMotor() {
        return drive;
    }
}
