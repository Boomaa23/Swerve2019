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

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import org.rivierarobotics.robot.Robot;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ButtonConfiguration {
    private static final Joystick buttons = Robot.runningRobot.controller.buttons;

    public static void initButtons() {
        for (ButtonMap button : ButtonMap.class.getEnumConstants()) {
            JoystickButton jsb = new JoystickButton(buttons, button.port);
            jsb.whenPressed(button.whenPressedCommand);
            if (button.whenReleasedCommand != null) {
                jsb.whenReleased(button.whenReleasedCommand);
            }
        }
    }

    public static Command getCommand(String commandName, Object[] parameters) {
        try {
            Class<?>[] argArr = new Class<?>[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                argArr[i] = parameters[i].getClass();
            }
            return (Command) Class.forName("org.rivierarobotics.commands." + commandName)
                    .getConstructor(argArr).newInstance(parameters);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
