package edu.ubb.tableeditor.command;

import edu.ubb.tableeditor.controller.MainController;
import edu.ubb.tableeditor.model.field.Position;

import java.util.Collections;
import java.util.List;

public class AddRowCommand extends PositionBasedCommand<Void, Void> {


    private List<String> lostData = Collections.emptyList();

    public AddRowCommand(MainController mainController, Position position) {
        super(mainController, position);
    }

    @Override
    public Void execute() {
        mainController.doAddNewRow();

        return null;
    }

    @Override
    public Void unexecute() {
        lostData = mainController.doDeleteRowAt(editPosition.getRow());

        return null;
    }

}
