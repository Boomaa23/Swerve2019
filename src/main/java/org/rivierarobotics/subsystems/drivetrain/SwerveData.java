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

package org.rivierarobotics.subsystems.drivetrain;

public enum SwerveData {
    POWER(Action.GET, Action.SET),
    ANGLE(Submodule.STEER),
    ANGLE_TICKS(Action.GET, Submodule.STEER),
    DISTANCE(Submodule.DRIVE),
    DISTANCE_TICKS(Action.GET, Submodule.DRIVE),
    DISTANCE_RELATIVE(Action.SET, Submodule.DRIVE),
    VELOCITY(Action.GET, Submodule.DRIVE);

    private final Action[] allowedActions;
    private final Submodule[] allowedSubmodules;

    SwerveData(Action allowedAction, Submodule allowedSubmodule) {
        this.allowedActions = new Action[]{ allowedAction };
        this.allowedSubmodules = new Submodule[]{ allowedSubmodule };
    }

    SwerveData(Submodule... allowedSubmodules) {
        this.allowedActions = Action.values();
        this.allowedSubmodules = allowedSubmodules;
    }

    SwerveData(Action... allowedActions) {
        this.allowedActions = allowedActions;
        this.allowedSubmodules = Submodule.values();
    }

    public boolean isActionAllowed(Action action) {
        for (Action tempAction : allowedActions) {
            if (tempAction == action) {
                return true;
            }
        }
        return false;
    }

    public boolean isSubmoduleAllowed(Submodule submodule) {
        for (Submodule tempSubmodule : allowedSubmodules) {
            if (tempSubmodule == submodule) {
                return true;
            }
        }
        return false;
    }

    public enum Action {
        GET, SET
    }

    public enum Submodule {
        DRIVE, STEER
    }
}
