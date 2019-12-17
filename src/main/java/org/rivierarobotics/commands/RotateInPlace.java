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
import org.rivierarobotics.robot.Robot;
import org.rivierarobotics.subsystems.DriveTrain;
import org.rivierarobotics.util.FieldPosition;
import org.rivierarobotics.util.MathUtil;
import org.rivierarobotics.util.RobotMap;

public class RotateInPlace extends Command {
    private final DriveTrain driveTrain = Robot.runningRobot.driveTrain;
    private final double targetAngle;
    private double pwr = 0.25;

    public RotateInPlace(FieldPosition position) {
        this(position.endRotation);
    }

    public RotateInPlace(double targetAngle) {
        this.targetAngle = MathUtil.fitToDegCircle(targetAngle);
        requires(driveTrain);
    }

    @Override
    protected void initialize() {
        double wheelAngle = Math.atan(RobotMap.Dimensions.TRACKWIDTH / RobotMap.Dimensions.WHEELBASE);
        driveTrain.setOrderedAngles(wheelAngle, 180 - wheelAngle, wheelAngle, 180 - wheelAngle);
        double currentAngle = driveTrain.getGyroAngle();
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
        return Math.abs(driveTrain.getGyroAngle() - targetAngle) <= 5;
    }
}
