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

import edu.wpi.first.wpilibj.command.Command;

public enum ButtonMap {
    TEST(0, "", "", new Object[]{}, new Object[]{});

    public boolean include = true;
    public int port;
    public Command whenPressedCommand, whenReleasedCommand;

    ButtonMap(int port, String whenPressedCommandName, String whenReleasedCommandName,
              Object[] whenPressedParameters, Object[] whenReleasedParameters) {
        this.port = port;
        this.whenPressedCommand = ButtonConfiguration.getCommand(whenPressedCommandName, whenPressedParameters);
        if (!whenPressedParameters.equals("") && whenReleasedParameters.length != 0) {
            this.whenReleasedCommand = ButtonConfiguration.getCommand(whenReleasedCommandName, whenReleasedParameters);
        } else {
            this.whenReleasedCommand = null;
        }
    }

    ButtonMap(int port, String whenPressedCommandName, String whenReleasedCommandName,
              Object[] whenPressedParameters, Object[] whenReleasedParameters, boolean include) {
        this(port, whenPressedCommandName, whenReleasedCommandName, whenPressedParameters, whenReleasedParameters);
        this.include = include;
    }

    ButtonMap(int port, String whenPressedCommandName, Object[] whenPressedParameters) {
        this(port, whenPressedCommandName, "", whenPressedParameters, new Object[]{});
    }
}
