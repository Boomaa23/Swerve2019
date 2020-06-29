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

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import org.rivierarobotics.commands.DriveControl;
import org.rivierarobotics.inject.Corner;
import org.rivierarobotics.subsystems.PigeonGyro;
import org.rivierarobotics.util.ControlDirective;
import org.rivierarobotics.util.Dimensions;
import org.rivierarobotics.util.MotorGroup;
import org.rivierarobotics.util.MotorMapped;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class DriveTrain extends Subsystem {
    private final Provider<DriveControl> command;
    private final PigeonGyro gyro;
    private final MotorMapped<SwerveModule> allModules;
    private final SwerveDriveOdometry odometry;

    @Inject
    public DriveTrain(@Corner(MotorGroup.FR) SwerveModule fr,
                      @Corner(MotorGroup.FL) SwerveModule fl,
                      @Corner(MotorGroup.BL) SwerveModule bl,
                      @Corner(MotorGroup.BR) SwerveModule br,
                      Provider<DriveControl> command,
                      PigeonGyro gyro) {
        this.command = command;
        this.gyro = gyro;
        this.allModules = new MotorMapped<>();
        this.odometry = createOdometry();

        allModules.put(MotorGroup.FR, fr);
        allModules.put(MotorGroup.FL, fl);
        allModules.put(MotorGroup.BL, bl);
        allModules.put(MotorGroup.BR, br);
    }

    private SwerveDriveOdometry createOdometry() {
        double loc = Dimensions.WHEELBASE / 2;
        SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
                new Translation2d(loc, loc),
                new Translation2d(loc, -loc),
                new Translation2d(-loc, loc),
                new Translation2d(-loc, -loc)
        );
        return new SwerveDriveOdometry(kinematics, gyro.getRotation2d());
    }

    public void setMappedControlDirective(MotorMapped<ControlDirective> powerMap) {
        for (Map.Entry<MotorGroup, ControlDirective> pwrMap : powerMap.entrySet()) {
            allModules.get(pwrMap.getKey()).getDrive().setPower(pwrMap.getValue().getPower());
            allModules.get(pwrMap.getKey()).getSteer().setAngle(pwrMap.getValue().getAngle());
        }
    }

    public MotorMapped<SpeedController> getAllMotors(SwerveData.Submodule submodule) {
        MotorMapped<SpeedController> map = new MotorMapped<>();
        for (Map.Entry<MotorGroup, SwerveModule> moduleEntry : allModules.entrySet()) {
            SwerveModule module = moduleEntry.getValue();
            map.put(moduleEntry.getKey(), (submodule == SwerveData.Submodule.DRIVE ? module.getDrive() : module.getSteer()).getMotor());
        }
        return map;
    }

    public MotorMapped<Double> getAll(SwerveData dataType) {
        checkTypes(dataType, SwerveData.Action.GET);
        MotorMapped<Double> values = new MotorMapped<>();

        for (Map.Entry<MotorGroup, SwerveModule> moduleEntry : allModules.entrySet()) {
            MotorGroup group = moduleEntry.getKey();
            SwerveModule module = moduleEntry.getValue();
            switch (dataType) {
                case ANGLE:
                    values.put(group, module.getSteer().getAngle());
                    break;
                case ANGLE_TICKS:
                    values.put(group, (double) module.getSteer().getAngleTicks());
                    break;
                case DISTANCE:
                    values.put(group, module.getDrive().getDistance());
                    break;
                case DISTANCE_TICKS:
                    values.put(group, (double) module.getDrive().getDistanceTicks());
                    break;
                case VELOCITY:
                    values.put(group, module.getDrive().getVelocity());
                    break;
                default:
                    throw new IllegalArgumentException("Type " + dataType + " could not be mapped correctly");
            }
        }
        return values;
    }

    public void setAll(SwerveData dataType, MotorMapped<Double> values, SwerveData.Submodule... optSubmodule) {
        checkTypes(dataType, SwerveData.Action.SET);
        if (values.size() != 4 || values.size() != allModules.size()) {
            throw new IllegalArgumentException(values.size() + " values were passed when expecting 4");
        }

        for (Map.Entry<MotorGroup, SwerveModule> moduleEntry : allModules.entrySet()) {
            SwerveModule module = moduleEntry.getValue();
            double value = values.get(moduleEntry.getKey());
            switch (dataType) {
                case POWER:
                    if (optSubmodule.length == 0) {
                        module.getDrive().setPower(value);
                        module.getSteer().setPower(value);
                    } else if (optSubmodule[0] == SwerveData.Submodule.DRIVE) {
                        module.getSteer().setPower(value);
                    } else if (optSubmodule[0] == SwerveData.Submodule.STEER) {
                        module.getDrive().setPower(value);
                    }
                    break;
                case ANGLE:
                    module.getSteer().setAngle(value);
                    break;
                case DISTANCE:
                    module.getDrive().setDistance(value);
                    break;
                case DISTANCE_RELATIVE:
                    module.getDrive().setRelativeDistance(value);
                    break;
                default:
                    throw new IllegalArgumentException("Type " + dataType + " could not be mapped correctly");
            }
        }
    }

    private void checkTypes(SwerveData dataType, SwerveData.Action action) {
        if (!dataType.isActionAllowed(action)) {
            throw new IllegalArgumentException("The data type " + dataType + " cannot be retrieved");
        }
    }

    public SwerveModule getSwerveModule(MotorGroup group) {
        return allModules.get(group);
    }

    public Pose2d getCurrentPosition() {
        return odometry.getPoseMeters();
    }

    public void resetZeroPosition() {
        gyro.doReset();
        setFieldPosition(new Pose2d(), gyro.getRotation2d());
    }

    public void setFieldPosition(Pose2d pos, Rotation2d angle) {
        odometry.resetPosition(pos, angle);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(command.get());
    }

    @Override
    public void periodic() {
        SwerveModuleState[] states = new SwerveModuleState[allModules.size()];
        int i = 0;
        for (SwerveModule module : allModules.values()) {
            states[i] = module.getState();
            i++;
        }
        if (gyro.needsOdometryReset()) {
            odometry.resetPosition(odometry.getPoseMeters(), gyro.getRotation2d());
        }
        odometry.update(gyro.getRotation2d(), states);
    }
}
