package org.rivierarobotics.driver;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.rivierarobotics.robot.Robot;

public class ButtonConfiguration {
    private static final Joystick buttons = Robot.runningRobot.controller.buttons;

    public static void initButtons() {
       for (ButtonMap button : ButtonMap.class.getEnumConstants()) {
           JoystickButton jsb = new JoystickButton(buttons, button.port);
           jsb.whenPressed(button.whenPressedCommand);
           if (button.whenReleasedCommand != null) {
               jsb.whenReleased(button.whenReleasedCommand);
           }
       }
    }
}
