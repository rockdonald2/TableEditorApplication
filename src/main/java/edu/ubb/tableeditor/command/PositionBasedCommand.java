package edu.ubb.tableeditor.command;

import edu.ubb.tableeditor.controller.MainController;
import edu.ubb.tableeditor.model.field.Position;

public abstract class PositionBasedCommand<ExecReturn, UnexecReturn> extends Command<ExecReturn, UnexecReturn> {

    protected final Position editPosition;

    public Position getEditPosition() {
        return editPosition;
    }

    protected PositionBasedCommand(MainController mainController, Position editPosition) {
        super(mainController);
        this.editPosition = editPosition;
    }

    public enum Orientation {
        DECREASE,
        INCREASE
    }

    public enum Which {
        COLUMN,
        ROW
    }

}
