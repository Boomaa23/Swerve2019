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

package org.rivierarobotics.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class MotorMapped<T> extends LinkedHashMap<MotorGroup, T> {
    private static final long serialVersionUID = 1L;

    public MotorMapped() {
        super();
    }

    @SafeVarargs
    public MotorMapped(T... orderedValues) {
        super();
        MotorGroup[] motors = MotorGroup.values();
        for (int i = 0; i < motors.length; i++) {
            this.put(motors[i], orderedValues[i]);
        }
    }

    public double[] toDoubleArray() {
        double[] array = new double[this.size()];
        int i = 0;
        for (Map.Entry<MotorGroup, T> entry : super.entrySet()) {
            array[i] = (double) entry.getValue();
            i++;
        }
        return array;
    }

    public static MotorMapped<Double> fromDouble(double value) {
        Double[] arr = new Double[MotorGroup.values().length];
        Arrays.fill(arr, value);
        return new MotorMapped<>(arr);
    }
}
