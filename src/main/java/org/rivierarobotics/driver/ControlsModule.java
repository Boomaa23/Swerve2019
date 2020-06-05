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

import dagger.Module;
import dagger.Provides;
import net.octyl.aptcreator.Provided;
import org.rivierarobotics.inject.Input;

import javax.inject.Singleton;

@Module
public class ControlsModule {
    private static final int DRIVER_LEFT_JS = 0;
    private static final int DRIVER_RIGHT_JS = 1;
    private static final int CODRIVER_LEFT_JS = 2;
    private static final int CODRIVER_RIGHT_JS = 3;

    private static final int DRIVER_BUTTONS = 4;
    private static final int CODRIVER_BUTTONS = 5;

    private ControlsModule() {
    }

    @Provides
    @Singleton
    @Input.Composite(Input.User.DRIVER)
    public static CompositeJoystick provideDriverCompositeJoystick(
            @Provided @Input(user = Input.User.DRIVER, type = Input.Type.LEFT_JS) BoundedJoystick joystick,
            @Provided @Input(user = Input.User.DRIVER, type = Input.Type.BUTTONS) BoundedJoystick buttons) {
        return new CompositeJoystick(joystick, joystick, joystick, buttons);
    }

    @Provides
    @Singleton
    @Input.Composite(Input.User.CODRIVER)
    public static CompositeJoystick provideCoDriverCompositeJoystick(
            @Provided @Input(user = Input.User.CODRIVER, type = Input.Type.LEFT_JS) BoundedJoystick joystick,
            @Provided @Input(user = Input.User.CODRIVER, type = Input.Type.BUTTONS) BoundedJoystick buttons) {
        return new CompositeJoystick(joystick, joystick, joystick, buttons);
    }

    @Provides
    @Singleton
    @Input(user = Input.User.DRIVER, type = Input.Type.LEFT_JS)
    public static BoundedJoystick provideDriverJoystickLeft() {
        return new BoundedJoystick(DRIVER_LEFT_JS);
    }

    @Provides
    @Singleton
    @Input(user = Input.User.DRIVER, type = Input.Type.RIGHT_JS)
    public static BoundedJoystick provideDriverJoystickRight() {
        return new BoundedJoystick(DRIVER_RIGHT_JS);
    }

    @Provides
    @Singleton
    @Input(user = Input.User.CODRIVER, type = Input.Type.LEFT_JS)
    public static BoundedJoystick provideCoDriverJoystickLeft() {
        return new BoundedJoystick(CODRIVER_LEFT_JS);
    }

    @Provides
    @Singleton
    @Input(user = Input.User.CODRIVER, type = Input.Type.RIGHT_JS)
    public static BoundedJoystick provideCoDriverJoystickRight() {
        return new BoundedJoystick(CODRIVER_RIGHT_JS);
    }

    @Provides
    @Singleton
    @Input(user = Input.User.DRIVER, type = Input.Type.BUTTONS)
    public static BoundedJoystick provideDriverButtons() {
        return new BoundedJoystick(DRIVER_BUTTONS);
    }

    @Provides
    @Singleton
    @Input(user = Input.User.CODRIVER, type = Input.Type.BUTTONS)
    public static BoundedJoystick provideCoDriverButtons() {
        return new BoundedJoystick(CODRIVER_BUTTONS);
    }
}