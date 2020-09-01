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

package org.rivierarobotics.inject;

import dagger.Component;
import org.rivierarobotics.driver.ButtonConfiguration;
import org.rivierarobotics.driver.CompositeJoystick;
import org.rivierarobotics.driver.ControlsModule;
import org.rivierarobotics.driver.CurrentControlMode;
import org.rivierarobotics.inject.CommandComponent.CCModule;
import org.rivierarobotics.subsystems.PigeonGyro;
import org.rivierarobotics.subsystems.SubsystemModule;
import org.rivierarobotics.subsystems.drivetrain.DriveTrain;
import org.rivierarobotics.util.RobotShuffleboard;

import javax.inject.Singleton;

@Component(modules = {SubsystemModule.class, ControlsModule.class, CCModule.class})
@Singleton
public abstract class GlobalComponent {
    public void robotInit() {
        getDriveTrain();
        getGyro();
        getDriverJoystick();
        getCoDriverJoystick();
        getCurrentControlMode();
        getButtonConfiguration();
        getCommandComponentBuilder();
        getShuffleboard();
    }

    public abstract DriveTrain getDriveTrain();

    public abstract PigeonGyro getGyro();

    @Input.Composite(Input.User.DRIVER)
    public abstract CompositeJoystick getDriverJoystick();

    @Input.Composite(Input.User.CODRIVER)
    public abstract CompositeJoystick getCoDriverJoystick();

    public abstract CurrentControlMode getCurrentControlMode();

    public abstract ButtonConfiguration getButtonConfiguration();

    public abstract CommandComponent.Builder getCommandComponentBuilder();

    public abstract RobotShuffleboard getShuffleboard();
}
