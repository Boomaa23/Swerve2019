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
import org.rivierarobotics.util.RobotMap.Dimensions;

public class SwerveUtil {
    private static double A = 0, B = 0, C = 0, D = 0;

    public static void swerveControl(double robotAngle, CompositeJoystick composite) {
        double FWD = composite.getY() * Math.cos(robotAngle) + composite.getX() * Math.sin(robotAngle);
        double STR = composite.getY() * Math.sin(robotAngle) + composite.getX() * Math.cos(robotAngle);
        double R = Math.sqrt(Math.pow(Dimensions.WHEELBASE, 2) / Math.pow(Dimensions.TRACKWIDTH, 2));
        A = STR - composite.getZ() * (Dimensions.WHEELBASE / Dimensions.TRACKWIDTH);
        B = STR + composite.getZ() * (Dimensions.WHEELBASE / Dimensions.TRACKWIDTH);
        C = FWD - composite.getZ() * (Dimensions.WHEELBASE / R);
        D = FWD + composite.getZ() * (Dimensions.WHEELBASE / R);
    }

    public static double calcWheelSpeed(RobotMap.MotorGroup group) {
        double[] calcIndexes = getCalcIndexes(group);
        return Math.sqrt(Math.pow(calcIndexes[0], 2) + Math.pow(calcIndexes[1], 2));
    }

    public static double calcWheelAngle(RobotMap.MotorGroup group) {
        double[] calcIndexes = getCalcIndexes(group);
        return Math.toDegrees(Math.atan2(calcIndexes[0], calcIndexes[1]));
    }

    private static double[] getCalcIndexes(RobotMap.MotorGroup group) {
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
}
