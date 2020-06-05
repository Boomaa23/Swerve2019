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

package org.rivierarobotics.commands;

import org.rivierarobotics.util.FieldPosition;
import org.rivierarobotics.util.Vector2D;

import javax.inject.Inject;

public class DriveCommands {
    private final DriveVectorCreator driveVectorCreator;
    private final RotateInPlaceCreator rotateInPlaceCreator;
    private final DriveToFieldPositionCreator driveToFieldPositionCreator;

    @Inject
    public DriveCommands(DriveVectorCreator driveVectorCreator,
                         RotateInPlaceCreator rotateInPlaceCreator,
                         DriveToFieldPositionCreator driveToFieldPositionCreator) {
        this.driveVectorCreator = driveVectorCreator;
        this.rotateInPlaceCreator = rotateInPlaceCreator;
        this.driveToFieldPositionCreator = driveToFieldPositionCreator;
    }

    public DriveVector driveVector(double distance, double angle) {
        return driveVectorCreator.create(distance, angle);
    }

    public DriveVector driveVector(Vector2D vector) {
        return driveVectorCreator.create(vector.getMagnitude(), vector.getAngle());
    }

    public DriveVector driveVector(FieldPosition position) {
        return driveVector(position.vector);
    }

    public RotateInPlace rotateInPlace(double degreesTargetAngle) {
        return rotateInPlaceCreator.create(degreesTargetAngle);
    }

    public RotateInPlace rotateInPlace(FieldPosition position) {
        return rotateInPlaceCreator.create(position.endRotation);
    }

    public DriveToFieldPosition driveToFieldPosition(FieldPosition position) {
        return driveToFieldPositionCreator.create(position);
    }
}
