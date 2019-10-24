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
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.rivierarobotics.commands.SwerveControl;
import org.rivierarobotics.util.RobotConstants;

import java.util.ArrayList;
import java.util.List;

public class DriveTrain extends Subsystem {
    private final SwerveModule fr, fl, bl, br;
    private final SwerveModule[] allModules;
    private final PigeonIMU gyro;

    public DriveTrain() {
        this.fr = new SwerveModule(RobotConstants.MotorGroup.FR);
        this.fl = new SwerveModule(RobotConstants.MotorGroup.FL);
        this.bl = new SwerveModule(RobotConstants.MotorGroup.BL);
        this.br = new SwerveModule(RobotConstants.MotorGroup.BR);
        this.gyro = new PigeonIMU(RobotConstants.CANDevices.GYRO);
        this.allModules = new SwerveModule[]{fr, fl, bl, br};
    }

    public void setPower(double... ordered_pwrs) {
        for(int i = 0;i < ordered_pwrs.length;i++) {
            allModules[i].setDrivePower(ordered_pwrs[i]);
        }
    }

    public void setAngle(double... ordered_angles) {
        for(int i = 0;i < ordered_angles.length;i++) {
            allModules[i].setWheelAngle(ordered_angles[i]);
        }
    }

    public double getRobotAngle() {
        double[] ypr = new double[3];
        gyro.getYawPitchRoll(ypr);
        return ypr[0];
    }

    public double[] getDistances() { return getTicks(true, fr, fl, bl, br); }
    public double[] getAngles() { return getTicks(false, fr, fl, bl, br); }

    private double[] getTicks(boolean isDistance, SwerveModule... modules) {
        List<Double> ticks = new ArrayList<>();
        for(SwerveModule module : modules) {
            CANSparkMax controller = !isDistance ? module.getSteer() : module.getDrive();
            ticks.add(controller.getEncoder().getPosition());
        }
        return ticks.stream().mapToDouble(Double::doubleValue).toArray();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new SwerveControl());
    }
}
