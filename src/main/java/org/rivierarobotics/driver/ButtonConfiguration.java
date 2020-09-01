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

package org.rivierarobotics.driver;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.rivierarobotics.inject.CommandComponent;
import org.rivierarobotics.inject.Input;
import org.rivierarobotics.util.FieldPosition;

import javax.inject.Inject;

public class ButtonConfiguration {
    private final BoundedJoystick driverLeft;
    private final BoundedJoystick driverRight;
    private final BoundedJoystick driverButtons;
    private final BoundedJoystick coDriverLeft;
    private final BoundedJoystick coDriverRight;
    private final BoundedJoystick coDriverButtons;
    private final CommandComponent cmds;

    @Inject
    public ButtonConfiguration(@Input(user = Input.User.DRIVER, type = Input.Type.LEFT_JS) BoundedJoystick driverLeft,
                               @Input(user = Input.User.DRIVER, type = Input.Type.RIGHT_JS) BoundedJoystick driverRight,
                               @Input(user = Input.User.DRIVER, type = Input.Type.BUTTONS) BoundedJoystick driverButtons,
                               @Input(user = Input.User.CODRIVER, type = Input.Type.LEFT_JS) BoundedJoystick coDriverLeft,
                               @Input(user = Input.User.CODRIVER, type = Input.Type.RIGHT_JS) BoundedJoystick coDriverRight,
                               @Input(user = Input.User.CODRIVER, type = Input.Type.BUTTONS) BoundedJoystick coDriverButtons,
                               CommandComponent.Builder component) {
        this.driverLeft = driverLeft;
        this.driverRight = driverRight;
        this.driverButtons = driverButtons;
        this.coDriverLeft = coDriverLeft;
        this.coDriverRight = coDriverRight;
        this.coDriverButtons = coDriverButtons;
        this.cmds = component.build();
    }

    public void initTeleop() {
        new JoystickButton(driverButtons, 12)
                .whenPressed(cmds.drive().driveVector(FieldPosition.FORWARD_ONE_FOOT));
        new JoystickButton(driverButtons, 11)
                .whenPressed(cmds.drive().driveVector(FieldPosition.BACKWARD_ONE_FOOT));
        new JoystickButton(driverButtons, 10)
                .whenPressed(cmds.drive().driveToFieldPosition(FieldPosition.DIAGONAL_ONE_FOOT));
        new JoystickButton(driverButtons, 9)
                .whenPressed(cmds.drive().driveToFieldPosition(FieldPosition.ORIGIN));
    }
}
