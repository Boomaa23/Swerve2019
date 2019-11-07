package org.rivierarobotics.driver;

import edu.wpi.first.wpilibj.command.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public enum ButtonMap {
    TEST(0, "", "", new ArrayList<>(), new ArrayList<>());

    public boolean include = true;
    public int port;
    public Command whenPressedCommand, whenReleasedCommand;

    ButtonMap(int port, String whenPressedCommandName, String whenReleasedCommandName,
              ArrayList whenPressedParameters, ArrayList whenReleasedParameters) {
        this.port = port;
        this.whenPressedCommand = getCommand(whenPressedCommandName, whenPressedParameters);
        if(!whenPressedParameters.equals("") && whenReleasedParameters.size() != 0) {
            this.whenReleasedCommand = getCommand(whenReleasedCommandName, whenReleasedParameters);
        } else {
            this.whenReleasedCommand = null;
        }
    }

    ButtonMap(int port, String whenPressedCommandName, String whenReleasedCommandName,
              ArrayList whenPressedParameters, ArrayList whenReleasedParameters, boolean include) {
        this(port, whenPressedCommandName, whenReleasedCommandName, whenPressedParameters, whenReleasedParameters);
        this.include = include;
    }

    ButtonMap(int port, String whenPressedCommandName, ArrayList whenPressedParameters) {
        this(port, whenPressedCommandName, "", whenPressedParameters, new ArrayList());
    }

    private static Command getCommand(String commandName, ArrayList parameters) {
        try {
            Class[] argArr = new Class[parameters.size()];
            for(int i = 0;i < parameters.size();i++) {
                argArr[i] = parameters.get(i).getClass();
            }
            return (Command) Class.forName("org.rivierarobotics.commands." + commandName)
                .getConstructor(argArr).newInstance(parameters.toArray());
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
