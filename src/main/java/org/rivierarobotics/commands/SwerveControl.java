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

package org.rivierarobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.rivierarobotics.driver.BoundedJoystick;
import org.rivierarobotics.robot.Robot;
import org.rivierarobotics.subsystems.DriveTrain;
import org.rivierarobotics.util.RobotConstants.Dimensions;
import org.rivierarobotics.util.RobotConstants.MotorGroup;

import java.util.Map;

public class SwerveControl extends Command {
    private final BoundedJoystick left;
    private final BoundedJoystick right;
    private DriveTrain driveTrain;

    private double A, B, C, D;

    public SwerveControl() {
        this.left = Robot.driver.left;
        this.right = Robot.driver.right;
        this.driveTrain = Robot.driveTrain;
        requires(driveTrain);
    }

    @Override
    protected void execute() {
        Map<Character, Double> js_dim = left.getXYZMap();
        double robotAngle = driveTrain.getRobotAngle();
        double FWD = js_dim.get('Y') * Math.cos(robotAngle) + js_dim.get('X') * Math.sin(robotAngle);
        double STR = -js_dim.get('Y') * Math.sin(robotAngle) + js_dim.get('X') * Math.cos(robotAngle);
        double R = Math.sqrt(Math.pow(Dimensions.WHEELBASE, 2) / Math.pow(Dimensions.TRACKWIDTH, 2));
        A = STR - js_dim.get('Z') * (Dimensions.WHEELBASE / Dimensions.TRACKWIDTH);
        B = STR + js_dim.get('Z') * (Dimensions.WHEELBASE / Dimensions.TRACKWIDTH);
        C = FWD - js_dim.get('Z') * (Dimensions.WHEELBASE / R);
        D = FWD + js_dim.get('Z') * (Dimensions.WHEELBASE / R);

        driveTrain.setPower(calcWheelSpeed(MotorGroup.FR), calcWheelSpeed(MotorGroup.FL),
                calcWheelSpeed(MotorGroup.BL), calcWheelSpeed(MotorGroup.BR));
        driveTrain.setAngle(calcWheelAngle(MotorGroup.FR), calcWheelAngle(MotorGroup.FL),
                calcWheelAngle(MotorGroup.BL), calcWheelAngle(MotorGroup.BR));
    }

    private double calcWheelSpeed(MotorGroup group) {
        double[] calcIndexes = getCalcIndexes(group);
        return Math.sqrt(Math.pow(calcIndexes[0], 2) + Math.pow(calcIndexes[1], 2));
    }

    private double calcWheelAngle(MotorGroup group) {
        double[] calcIndexes = getCalcIndexes(group);
        return Math.toDegrees(Math.atan2(calcIndexes[0], calcIndexes[1]));
    }

    private double[] getCalcIndexes(MotorGroup group) {
        double calcIndexes[];
        switch(group) {
            case FR: calcIndexes = new double[]{B, C}; break;
            case FL: calcIndexes = new double[]{B, D}; break;
            case BL: calcIndexes = new double[]{A, D}; break;
            case BR: calcIndexes = new double[]{A, C}; break;
            default: calcIndexes = new double[]{0, 0}; break;
        }
        return calcIndexes;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
