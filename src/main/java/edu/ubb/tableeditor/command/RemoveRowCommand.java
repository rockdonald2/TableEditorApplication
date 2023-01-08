package edu.ubb.tableeditor.command;

import edu.ubb.tableeditor.controller.MainController;
import edu.ubb.tableeditor.model.field.Position;

import java.util.Collections;
import java.util.List;

public class RemoveRowCommand extends PositionBasedCommand<Void, Void> {

    private List<String> lostData = Collections.emptyList();

    public RemoveRowCommand(MainController mainController, Position position) {
        super(mainController, position);
    }

    @Override
    public Void execute() {
        lostData = mainController.doDeleteRowAt(editPosition.getRow());

        return null;
    }

    @Override
    public Void unexecute() {
        mainController.doAddRowAt(editPosition.getRow(), lostData);

        return null;
    }

}
