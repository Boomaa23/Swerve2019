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

package org.rivierarobotics.driver;

import org.rivierarobotics.commands.*;
import org.rivierarobotics.robot.Robot;
import org.rivierarobotics.util.ControlMode;
import org.rivierarobotics.util.FieldPosition;

public class ButtonConfiguration {
    public static void initButtons() {
        createButton(0).onPress(new ChangeControlMode(ControlMode.TANK));
        createButton(1).onPress(new ChangeControlMode(ControlMode.SWERVE));
        createButton(2).onPress(new DriveVector(FieldPosition.FORWARD_ONE_FOOT))
                .onRelease(new DriveVector(FieldPosition.BACKWARD_ONE_FOOT));
    }

    private static JSBChain createButton(int num) {
        return new JSBChain(Robot.runningRobot.controller.buttons, num);
    }
}
