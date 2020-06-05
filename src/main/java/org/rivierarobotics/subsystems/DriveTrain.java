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

import edu.wpi.first.wpilibj.command.Subsystem;
import org.rivierarobotics.commands.DriveControl;
import org.rivierarobotics.inject.CommandComponent;
import org.rivierarobotics.inject.Corner;
import org.rivierarobotics.util.ControlDirective;
import org.rivierarobotics.util.MotorGroup;
import org.rivierarobotics.util.Vector2D;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class DriveTrain extends Subsystem {
    private final SwerveModule fr;
    private final SwerveModule fl;
    private final SwerveModule bl;
    private final SwerveModule br;
    private final Provider<DriveControl> command;
    private final SwerveModule[] allModules;
    private Vector2D currentPosition;

    @Inject
    public DriveTrain(@Corner(MotorGroup.FL) SwerveModule fr,
                      @Corner(MotorGroup.FL) SwerveModule fl,
                      @Corner(MotorGroup.FL) SwerveModule bl,
                      @Corner(MotorGroup.FL) SwerveModule br,
                      Provider<DriveControl> command) {
        this.fr = fr;
        this.fl = fl;
        this.bl = bl;
        this.br = br;
        this.command = command;
        this.allModules = new SwerveModule[]{ fr, fl, bl, br };
        this.currentPosition = new Vector2D();
    }

    public void setMappedControlDirective(Map<MotorGroup, ControlDirective> powerMap) {
        for (Map.Entry<MotorGroup, ControlDirective> pwrMap : powerMap.entrySet()) {
            SwerveModule aModule = getSwerveModule(pwrMap.getKey());
            aModule.setDrivePower(pwrMap.getValue().getPower());
            aModule.setSteerAngle(pwrMap.getValue().getAngle());
        }
    }

    public void setOrderedPowers(double... orderedPwrs) {
        for (int i = 0; i < orderedPwrs.length; i++) {
            allModules[i].setDrivePower(orderedPwrs[i]);
        }
    }

    public void setAllPowers(double pwr) {
        for (SwerveModule module : allModules) {
            module.setDrivePower(pwr);
        }
    }

    public void setOrderedAngles(double... orderedAngles) {
        for (int i = 0; i < orderedAngles.length; i++) {
            allModules[i].setSteerAngle(orderedAngles[i]);
        }
    }

    public void setAllAngles(double angle) {
        for (SwerveModule module : allModules) {
            module.setSteerAngle(angle);
        }
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

    public Vector2D getCurrentPosition() {
        return currentPosition;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(command.get());
    }
}
