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

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import org.rivierarobotics.robot.Robot;
import org.rivierarobotics.util.ControlMode;
import org.rivierarobotics.util.FieldPosition;

import java.lang.reflect.InvocationTargetException;

@Deprecated
public enum ReflectButtonConfig {
    FORWARD_ONE_FOOT(1, "DriveToFieldPosition",
                             new Object[]{ FieldPosition.FORWARD_ONE_FOOT }),
    SKEW_DIAGONAL_ONE_FOOT(2, "DriveToFieldPosition",
                                   new Object[]{ FieldPosition.DIAGONAL_ONE_FOOT }),
    CONTROL_MODE_SWERVE(3, "ChangeControlMode", new Object[]{ ControlMode.SWERVE }),
    CONTROL_MODE_AUTOMOBILE(4, "ChangeControlMode", new Object[]{ ControlMode.AUTOMOBILE }),
    CONTROL_MODE_TANK(5, "ChangeControlMode", new Object[]{ ControlMode.TANK }),
    CONTROL_MODE_CRAB(6, "ChangeControlMode", new Object[]{ ControlMode.CRAB });

    public boolean include = true;
    public int buttonNum;
    public Command whenPressedCommand, whenReleasedCommand;
    ReflectButtonConfig(int buttonNum, String whenPressedCommandName, String whenReleasedCommandName,
                        Object[] whenPressedParameters, Object[] whenReleasedParameters) {
        this.buttonNum = buttonNum;
        this.whenPressedCommand = getCommand(whenPressedCommandName, whenPressedParameters);
        if (whenReleasedParameters != null && whenReleasedParameters.length != 0) {
            this.whenReleasedCommand = getCommand(whenReleasedCommandName, whenReleasedParameters);
        } else {
            this.whenReleasedCommand = null;
        }
    }

    ReflectButtonConfig(int buttonNum, String whenPressedCommandName, String whenReleasedCommandName,
                        Object[] whenPressedParameters, Object[] whenReleasedParameters, boolean include) {
        this(buttonNum, whenPressedCommandName, whenReleasedCommandName, whenPressedParameters, whenReleasedParameters);
        this.include = include;
    }

    ReflectButtonConfig(int buttonNum, String whenPressedCommandName, Object[] whenPressedParameters) {
        this(buttonNum, whenPressedCommandName, "", whenPressedParameters, new Object[]{});
    }

    public static void initButtons() {
        for (ReflectButtonConfig button : ReflectButtonConfig.class.getEnumConstants()) {
            JoystickButton jsb = new JoystickButton(Robot.runningRobot.controller.buttons, button.buttonNum);
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
