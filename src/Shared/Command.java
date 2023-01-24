package Shared;

import java.io.Serializable;

public class Command implements Serializable {
    private DataBox dataCommand;
    private CommandType commandType;

    public Command() {

    }

    public DataBox getDataCommand() {
        return dataCommand;
    }

    public void setDataCommand(DataBox dataCommand) {
        this.dataCommand = dataCommand;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
}
