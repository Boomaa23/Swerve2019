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

public enum FieldPosition {
    ORIGIN(0, 0, 0, CentricMode.FIELD),
    FORWARD_ONE_FOOT(0, 12, 270, CentricMode.ROBOT),
    BACKWARD_ONE_FOOT(0, -12, 90, CentricMode.ROBOT),
    DIAGONAL_ONE_FOOT(Math.sqrt(2) / 2, Math.sqrt(2) / 2, 135, CentricMode.ROBOT);

    public final double endRotation;
    public final Vector2D vector;
    public final CentricMode centric;

    FieldPosition(double x, double y, double endRotation, CentricMode centric) {
        this.vector = new Vector2D(x, y);
        this.endRotation = endRotation;
        this.centric = centric;
    }

    public enum CentricMode {
        FIELD, ROBOT
    }
}
