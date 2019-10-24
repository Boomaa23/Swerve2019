package org.rivierarobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.rivierarobotics.util.Vector2D;
import org.rivierarobotics.robot.Robot;
import org.rivierarobotics.subsystems.DriveTrain;

public class DriveVector extends InstantCommand {
    private final DriveTrain driveTrain;
    private final double magnitude, angle;

    public DriveVector(double magnitude, double angle) {
        this.driveTrain = Robot.runningRobot.driveTrain;
        this.magnitude = magnitude;
        this.angle = angle;
        requires(driveTrain);
    }

    public DriveVector(Vector2D vector) {
        this.driveTrain = Robot.runningRobot.driveTrain;
        this.angle = vector.getAngle();
        this.magnitude = vector.getMagnitude();
        requires(driveTrain);
    }

    @Override
    protected void execute() {
        driveTrain.setAllSteerAngle(angle);
        driveTrain.setAllDriveDistance(magnitude);
    }
}
