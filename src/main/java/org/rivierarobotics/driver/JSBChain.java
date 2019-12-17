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

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class JSBChain extends JoystickButton {
    public JSBChain(GenericHID joystick, int buttonNumber) {
        super(joystick, buttonNumber);
    }

    public JSBChain onPress(Command cmd) {
        this.whenPressed(cmd);
        return this;
    }

    public JSBChain onRelease(Command cmd) {
        this.whenReleased(cmd);
        return this;
    }

    public JSBChain onHold(Command cmd) {
        this.whileHeld(cmd);
        return this;
    }
}
