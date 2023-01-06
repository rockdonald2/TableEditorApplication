package edu.ubb.tableeditor;

import edu.ubb.tableeditor.controller.MainController;

import javax.swing.*;

public class ApplicationMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MainController.instance().init());
    }

}
