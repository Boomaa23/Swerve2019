package org.rivierarobotics.driver;

import org.rivierarobotics.commands.*;
import org.rivierarobotics.robot.Robot;
import org.rivierarobotics.util.ControlMode;
import org.rivierarobotics.util.FieldPosition;

public class ButtonConfiguration {
    public static void initButtons() {
        createButton(0).onPress(new ChangeControlMode(ControlMode.TANK));
        createButton(1).onPress(new ChangeControlMode(ControlMode.SWERVE));
        createButton(2).onPress(new DriveVector(FieldPosition.FORWARD_ONE_FOOT))
                .onRelease(new DriveVector(FieldPosition.BACKWARD_ONE_FOOT));
    }

    private static JSBChain createButton(int num) {
        return new JSBChain(Robot.runningRobot.controller.buttons, num);
    }
}
