package edu.ubb.tableeditor.command;

import edu.ubb.tableeditor.controller.MainController;
import edu.ubb.tableeditor.model.field.Position;

public class EditCommand extends PositionBasedCommand<Void, Void> {


    private final String prevValue;
    private final String editValue;

    public EditCommand(MainController mainController, String editValue, Position position) {
        super(mainController, position);
        this.editValue = editValue;
        this.prevValue = mainController.getValueAt(position);
    }

    @Override
    public Void execute() {
        doEdit(editValue);
        return null;
    }

    @Override
    public Void unexecute() {
        doEdit(prevValue);
        return null;
    }

    private void doEdit(String value) {
        mainController.doModifyValueAt(editPosition.getRow(), editPosition.getColumn(), value);
        mainController.doDisplayData();
    }

}
