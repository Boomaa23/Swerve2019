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

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.rivierarobotics.commands.DriveControl;
import org.rivierarobotics.util.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DriveTrain extends Subsystem {
    private final SwerveModule fr, fl, bl, br;
    private final SwerveModule[] allModules;
    private final PigeonIMU gyro;

    public DriveTrain() {
        this.fr = new SwerveModule(MotorGroup.FR);
        this.fl = new SwerveModule(MotorGroup.FL);
        this.bl = new SwerveModule(MotorGroup.BL);
        this.br = new SwerveModule(MotorGroup.BR);
        this.gyro = new PigeonIMU(RobotMap.CANDevices.GYRO);
        this.allModules = new SwerveModule[]{fr, fl, bl, br};
    }

    public void setMappedControlDirective(Map<MotorGroup, ControlDirective> powerMap) {
        for (Map.Entry<MotorGroup, ControlDirective> pwrMap : powerMap.entrySet()) {
            SwerveModule aModule = getSwerveModule(pwrMap.getKey());
            aModule.setDrivePower(pwrMap.getValue().getPower());
            aModule.setSteerAngle(pwrMap.getValue().getAngle());
        }
    }

    public void setOrderedPowers(double... ordered_pwrs) {
        for (int i = 0; i < ordered_pwrs.length; i++) {
            allModules[i].setDrivePower(ordered_pwrs[i]);
        }
    }

    public void setAllPowers(double pwr) {
        for (SwerveModule module : allModules) {
            module.setDrivePower(pwr);
        }
    }

    public void setOrderedAngles(double... ordered_angles) {
        for (int i = 0; i < ordered_angles.length; i++) {
            allModules[i].setSteerAngle(ordered_angles[i]);
        }
    }

    public void setAllAngles(double angle) {
        for (SwerveModule module : allModules) {
            module.setSteerAngle(angle);
        }
    }

    public double getSteerTicks(MotorGroup group) {
        return getSwerveModule(group).getSteerAngleTicks();
    }

    public double[] getAllValues(boolean isTicks, boolean isDistance) {
        List<Double> ticks = new ArrayList<>();
        for (SwerveModule module : allModules) {
            if (isDistance) {
                ticks.add(isTicks ? module.getDriveDistanceTicks() : module.getDriveDistance());
            } else {
                ticks.add(isTicks ? module.getSteerAngleTicks() : module.getSteerAngle());
            }
        }
        return ticks.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public double[] getAllPowers(boolean isDrive) {
        double[] powers = new double[allModules.length];
        for (int i = 0; i < allModules.length; i++) {
            powers[i] = isDrive ? allModules[i].getDrive().get() : allModules[i].getSteer().get();
        }
        return powers;
    }

    public double getGyroAngle() {
        double[] ypr = new double[3];
        gyro.getYawPitchRoll(ypr);
        return MathUtil.fitToDegCircle(ypr[0]);
    }

    public void resetGyro() {
        gyro.setYaw(0);
    }

    public void resetDriveEncoders() {
        for (SwerveModule module : allModules) {
            module.getDrive().getEncoder().setPosition(0.0);
        }
    }

    public SwerveModule getSwerveModule(MotorGroup group) {
        switch (group) {
            case FR: return fr;
            case FL: return fl;
            case BL: return bl;
            case BR: return br;
            default: throw new InvalidParameterException("Motor group " + group.name() + " could not be found");
        }
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveControl());
    }
}
