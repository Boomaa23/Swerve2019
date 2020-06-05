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

import edu.wpi.first.wpilibj.command.Command;
import net.octyl.aptcreator.GenerateCreator;
import net.octyl.aptcreator.Provided;
import org.rivierarobotics.subsystems.DriveTrain;
import org.rivierarobotics.subsystems.PigeonGyro;
import org.rivierarobotics.util.Dimensions;
import org.rivierarobotics.util.MathUtil;

@GenerateCreator
public class RotateInPlace extends Command {
    private final DriveTrain driveTrain;
    private final PigeonGyro gyro;
    private final double targetAngle;
    private double pwr = 0.25;

    public RotateInPlace(@Provided DriveTrain driveTrain, @Provided PigeonGyro gyro, double degreesTargetAngle) {
        this.driveTrain = driveTrain;
        this.gyro = gyro;
        this.targetAngle = MathUtil.fitToDegCircle(degreesTargetAngle);
        requires(driveTrain);
    }

    @Override
    protected void initialize() {
        double currentAngle = gyro.getAngle();
        double wheelAngle = Math.atan(Dimensions.TRACKWIDTH / Dimensions.WHEELBASE);
        driveTrain.setOrderedAngles(wheelAngle, 180 - wheelAngle, wheelAngle, 180 - wheelAngle);
        pwr = (targetAngle - currentAngle) > (currentAngle + (360 - targetAngle)) ? pwr * -1 : pwr;
    }

    @Override
    protected void execute() {
        driveTrain.setAllPowers(pwr);
    }

    @Override
    protected void end() {
        driveTrain.setAllPowers(0.0);
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(gyro.getAngle() - targetAngle) <= 5;
    }
}
