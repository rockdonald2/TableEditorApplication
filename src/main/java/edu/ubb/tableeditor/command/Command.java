package edu.ubb.tableeditor.command;

import edu.ubb.tableeditor.controller.MainController;

public abstract class Command<ExecReturn, UnexecReturn> {

    protected final MainController mainController;

    protected Command(MainController mainController) {
        this.mainController = mainController;
    }

    public abstract ExecReturn execute();

    public abstract UnexecReturn unexecute();

}
