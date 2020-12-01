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

package org.rivierarobotics.util;

import edu.wpi.first.wpilibj.controller.PIDController;

public class MultiPID {
    private final PIDController posCtrl;
    private final PIDController velCtrl;
    private final PIDConfig posConfig;
    private final PIDConfig velConfig;
    private Mode selectedMode;

    public MultiPID(PIDConfig posConfig, PIDConfig velConfig) {
        this.posCtrl = new PIDController(posConfig.getP(), posConfig.getI(), posConfig.getD());
        this.velCtrl = new PIDController(velConfig.getP(), velConfig.getI(), velConfig.getD());
        this.posConfig = posConfig;
        this.velConfig = velConfig;
        posCtrl.setTolerance(posConfig.getTolerance());
        velCtrl.setTolerance(velConfig.getTolerance());
        this.selectedMode = Mode.NONE;
    }

    public Mode getSelectedMode() {
        return selectedMode;
    }

    public void selectMode(Mode mode) {
        if (mode != selectedMode) {
            this.selectedMode = mode;
        }
    }

    public void setSetpoint(double setpoint) {
        if (selectedMode == Mode.POSITION) {
            posCtrl.setSetpoint(setpoint);
        } else if (selectedMode == Mode.VELOCITY) {
            velCtrl.setSetpoint(setpoint);
        }
    }

    public double calculatePos(double currentPos) {
        return MathUtil.limit(posCtrl.calculate(currentPos), posConfig.getRange());
    }

    public double calculateVel(double currentVel) {
        return MathUtil.limit(velCtrl.calculate(currentVel), velConfig.getRange());
    }

    public enum Mode {
        POSITION, VELOCITY, NONE
    }
}
