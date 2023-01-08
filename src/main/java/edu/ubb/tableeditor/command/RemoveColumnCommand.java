package edu.ubb.tableeditor.command;

import edu.ubb.tableeditor.controller.MainController;
import edu.ubb.tableeditor.model.field.Position;

import java.util.Collections;
import java.util.List;

public class RemoveColumnCommand extends PositionBasedCommand<Void, Void> {

    private List<String> lostData = Collections.emptyList();
    private String lostCol;

    public RemoveColumnCommand(MainController mainController, Position position) {
        super(mainController, position);
    }

    @Override
    public Void execute() {
        lostCol = mainController.getColumnNameAt(editPosition.getColumn());
        lostData = mainController.doDeleteColumnAt(editPosition.getColumn());

        return null;
    }

    @Override
    public Void unexecute() {
        mainController.doAddColumnAt(editPosition.getColumn(), lostData, lostCol);

        return null;
    }

}
