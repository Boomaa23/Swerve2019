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

package org.rivierarobotics.util;

import org.rivierarobotics.driver.CompositeJoystick;
import org.rivierarobotics.robot.Robot;
import org.rivierarobotics.util.RobotMap.Dimensions;

import java.util.HashMap;
import java.util.Map;

public class DriveCalculation {
    public static class Swerve {
        private static double A = 0, B = 0, C = 0, D = 0;

        public static final DriveCalculator CALCULATOR = (CompositeJoystick composite, MotorGroup... groups) -> {
            control(composite, Robot.runningRobot.driveTrain.getGyroAngle());
            Map<MotorGroup, ControlDirective> calcs = new HashMap<>();
            for (int i = 0; i < groups.length; i++) {
                calcs.put(groups[i], new ControlDirective(calcWheelAngle(groups[i]), calcWheelSpeed(groups[i])));
            }
            return calcs;
        };

        private static void control(CompositeJoystick composite, double robotAngle) {
            double fwd = composite.getY();
            double str = composite.getX();
            double r = Math.sqrt(Math.pow(RobotMap.Dimensions.WHEELBASE, 2) + Math.pow(RobotMap.Dimensions.TRACKWIDTH, 2));
            A = str - composite.getZ() * (RobotMap.Dimensions.WHEELBASE / r);
            B = str + composite.getZ() * (RobotMap.Dimensions.WHEELBASE / r);
            C = fwd - composite.getZ() * (RobotMap.Dimensions.TRACKWIDTH / r);
            D = fwd + composite.getZ() * (RobotMap.Dimensions.TRACKWIDTH / r);
        }

        private static double calcWheelSpeed(MotorGroup group) {
            double[] calcIndexes = getCalcIndexes(group);
            return Math.sqrt(Math.pow(calcIndexes[0], 2) + Math.pow(calcIndexes[1], 2));
        }

        private static double calcWheelAngle(MotorGroup group) {
            double[] calcIndexes = getCalcIndexes(group);
            return MathUtil.fitToDegCircle(Math.toDegrees(Math.atan2(calcIndexes[0], calcIndexes[1])));
        }

        private static double[] getCalcIndexes(MotorGroup group) {
            double calcIndexes[];
            switch (group) {
                case FR: calcIndexes = new double[]{B, C}; break;
                case FL: calcIndexes = new double[]{B, D}; break;
                case BL: calcIndexes = new double[]{A, D}; break;
                case BR: calcIndexes = new double[]{A, C}; break;
                default: calcIndexes = new double[]{0, 0}; break;
            }
            return calcIndexes;
        }
    }

    public static class Tank {
        public static final DriveCalculator CALCULATOR = (CompositeJoystick composite, MotorGroup... groups) -> {
            Map<MotorGroup, ControlDirective> calcs = new HashMap<>();
            for (int i = 0; i < groups.length; i++) {
                calcs.put(groups[i], new ControlDirective(0.0,
                        calcWheelSpeed(groups[i].LRSide, composite.getX(), composite.getY())));
            }
            return calcs;
        };

        private static double calcWheelSpeed(MotorGroup.Side motorSide, double rotate, double power) {
            double max = Math.max(Math.abs(rotate), Math.abs(power));
            double diff = power - rotate;
            double sum = power + rotate;

            double left;
            double right;

            if (power > 0) {
                if (rotate > 0) {
                    left = max;
                    right = diff;
                } else {
                    left = sum;
                    right = max;
                }
            } else {
                if (rotate > 0) {
                    left = sum;
                    right = -max;
                } else {
                    right = diff;
                    left = -max;
                }
            }

            return motorSide.equals(MotorGroup.Side.LEFT) ? left : right;
        }
    }

    public static class Automobile {
        public static final DriveCalculator CALCULATOR = (CompositeJoystick composite, MotorGroup... groups) -> {
            Map<MotorGroup, ControlDirective> baseCalc = Crab.CALCULATOR.calculate(composite, groups);
            baseCalc.get(MotorGroup.BL).setAngle(90);
            baseCalc.get(MotorGroup.BR).setAngle(90);
            return baseCalc;
        };
    }

    public static class Crab {
        public static final DriveCalculator CALCULATOR = (CompositeJoystick composite, MotorGroup... groups) -> {
            Map<MotorGroup, ControlDirective> calcs = new HashMap<>();
            for (int i = 0; i < groups.length; i++) {
                calcs.put(groups[i], new ControlDirective(calcWheelAngle(groups[i], composite), composite.getY()));
            }
            return calcs;
        };

        public static double calcWheelAngle(MotorGroup group, CompositeJoystick composite) {
            double angle = new Vector2D(composite.getX(), composite.getY()).getAngle();
            if (group.FBSide.equals(MotorGroup.Side.BACK)) {
                angle += 90;
            }
            return angle;
        }
    }
}
