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

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DriveCalculation {
    public enum CalcType {
        SPEED, ANGLE
    }

    public static class Swerve {
        private static double A = 0, B = 0, C = 0, D = 0;

        public static Map<MotorGroup, Double> calculate(CalcType type, CompositeJoystick composite, MotorGroup... groups) {
            control(Robot.runningRobot.driveTrain.getGyroAngle(), composite);
            Map<MotorGroup, Double> calcs = new HashMap<>();
            for (int i = 0; i < groups.length; i++) {
                calcs.put(groups[i], type.equals(CalcType.SPEED) ?
                        calcWheelSpeed(groups[i]) : calcWheelAngle(groups[i]));
            }
            return calcs;
        }

        private static void control(double robotAngle, CompositeJoystick composite) {
            double fwd = composite.getY() * Math.cos(robotAngle) + composite.getX() * Math.sin(robotAngle);
            double str = composite.getY() * Math.sin(robotAngle) + composite.getX() * Math.cos(robotAngle);
            double r = Math.sqrt(Math.pow(Dimensions.WHEELBASE, 2) / Math.pow(Dimensions.TRACKWIDTH, 2));
            A = str - composite.getZ() * (Dimensions.WHEELBASE / Dimensions.TRACKWIDTH);
            B = str + composite.getZ() * (Dimensions.WHEELBASE / Dimensions.TRACKWIDTH);
            C = fwd - composite.getZ() * (Dimensions.WHEELBASE / r);
            D = fwd + composite.getZ() * (Dimensions.WHEELBASE / r);
        }

        private static double calcWheelSpeed(MotorGroup group) {
            double[] calcIndexes = getCalcIndexes(group);
            return Math.sqrt(Math.pow(calcIndexes[0], 2) + Math.pow(calcIndexes[1], 2));
        }

        private static double calcWheelAngle(MotorGroup group) {
            double[] calcIndexes = getCalcIndexes(group);
            return Math.toDegrees(Math.atan2(calcIndexes[0], calcIndexes[1]));
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
        public static Map<MotorGroup, Double> calculate(CalcType type, CompositeJoystick composite, MotorGroup... groups) {
            Map<MotorGroup, Double> calcs = new HashMap<>();
            for (int i = 0; i < groups.length; i++) {
                calcs.put(groups[i], type.equals(CalcType.SPEED) ?
                        calcWheelSpeed(groups[i].LRSide, composite.getX(), composite.getY()) : 0.0);
            }
            return calcs;
        }

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
        public static Map<MotorGroup, Double> calculate(CalcType type, CompositeJoystick composite, MotorGroup... groups) {
            Map<MotorGroup, Double> baseCalc = Crab.calculate(type, composite, groups);
            baseCalc.replace(MotorGroup.BL, baseCalc.get(MotorGroup.FL) + 90);
            baseCalc.replace(MotorGroup.BR, baseCalc.get(MotorGroup.FR) + 90);
            return baseCalc;
        }
    }

    public static class Crab {
        public static Map<MotorGroup, Double> calculate(CalcType type, CompositeJoystick composite, MotorGroup... groups) {
            Map<MotorGroup, Double> calcs = new HashMap<>();
            for (int i = 0; i < groups.length; i++) {
                calcs.put(groups[i], type.equals(CalcType.SPEED) ?
                        composite.getY() : calcWheelAngle(groups[i], composite));
            }
            return calcs;
        }

        public static double calcWheelAngle(MotorGroup group, CompositeJoystick composite) {
            if(group.FBSide.equals(MotorGroup.Side.FRONT)) {
                return new Vector2D(composite.getX(), composite.getY()).getAngle();
            } else {
                return 90.0;
            }
        }
    }
}
