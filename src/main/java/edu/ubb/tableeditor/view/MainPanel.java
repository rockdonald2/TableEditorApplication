package edu.ubb.tableeditor.view;

import com.formdev.flatlaf.FlatLightLaf;
import edu.ubb.tableeditor.annotation.Flag;
import edu.ubb.tableeditor.annotation.Singleton;
import edu.ubb.tableeditor.command.AddColumnCommand;
import edu.ubb.tableeditor.command.AddRowCommand;
import edu.ubb.tableeditor.controller.MainController;
import edu.ubb.tableeditor.model.Data;
import edu.ubb.tableeditor.model.Position;
import edu.ubb.tableeditor.service.search.MatchCaseSearchStrategy;
import edu.ubb.tableeditor.service.search.SearchStrategy;
import edu.ubb.tableeditor.service.search.SubStringSearchStrategy;
import edu.ubb.tableeditor.service.search.WholeCellSearchStrategy;
import edu.ubb.tableeditor.utils.PropertiesContext;
import edu.ubb.tableeditor.view.button.SearchRadioButton;
import edu.ubb.tableeditor.view.exception.ViewException;
import edu.ubb.tableeditor.view.menu.MenuBar;
import edu.ubb.tableeditor.view.table.SimpleTable;
import edu.ubb.tableeditor.view.table.Table;
import edu.ubb.tableeditor.view.table.decorator.FormulaCapableTableDecorator;
import edu.ubb.tableeditor.view.table.decorator.RowNumberTableDecorator;
import edu.ubb.tableeditor.view.table.decorator.TableDecorator;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@Singleton
public final class MainPanel extends JFrame {

    private static MainPanel instance;

    private final MainController mainController;
    private final int windowHeight;
    private final int windowWidth;
    private Table table;
    private JMenuItem addRowBtn;
    private JMenuItem addColumnBtn;
    private JMenuItem exportItem;
    private JCheckBoxMenuItem rowDecoratorBtn;
    private JCheckBoxMenuItem formulaDecoratorBtn;
    private JMenuItem findBtn;
    @Flag
    private boolean initialized;

    private MainPanel(MainController mainController) {
        this.mainController = mainController;

        windowWidth = PropertiesContext.getIntProperty("window.size.width");
        windowHeight = PropertiesContext.getIntProperty("window.size.height");
    }

    public static synchronized MainPanel instance() {
        if (instance == null) {
            instance = new MainPanel(MainController.instance());
        }

        return instance;
    }

    public void init() {
        FlatLightLaf.setup(); // look-and-feel

        if (initialized) {
            throw new IllegalStateException(String.format("%s already initialized", MainPanel.class.getName()));
        }

        createMenuBar();
        createTable();
        baseConfig();
        initialized = true;
    }

    private void addNewRow(ActionEvent e) {
        mainController.executeCommand(new AddRowCommand(mainController, new Position(mainController.getRows(), 0)));
    }

    private void addNewColumn(ActionEvent e) {
        String columnName = JOptionPane.showInputDialog(this, "Specify the name of the new column");

        if (columnName == null || columnName.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty column name specified");
            return;
        }

        mainController.executeCommand(new AddColumnCommand(mainController, new Position(0, mainController.getColumns()), columnName));
    }

    private void toggleRowNumbering(ActionEvent e) {
        if (((AbstractButton) e.getSource()).isSelected()) {
            MainPanel.this.table = new RowNumberTableDecorator(MainPanel.this.table);
            MainPanel.this.mainController.doDisplayData();
        } else {
            if (MainPanel.this.table instanceof final TableDecorator decorator) {
                decorator.reset();
            }

            MainPanel.this.table = (Table) MainPanel.this.table.getTable();
            MainPanel.this.mainController.doDisplayData();
        }
    }

    private void toggleFormulas(ActionEvent e) {
        if (((AbstractButton) e.getSource()).isSelected()) {
            MainPanel.this.table = new FormulaCapableTableDecorator(MainPanel.this.table);
            MainPanel.this.mainController.doDisplayData();
        } else {
            if (MainPanel.this.table instanceof final TableDecorator decorator) {
                decorator.reset();
            }

            MainPanel.this.table = (Table) MainPanel.this.table.getTable();
            MainPanel.this.mainController.doDisplayData();
        }
    }

    private void createTable() {
        this.table = new SimpleTable();
        this.add(this.table.getContainer());
    }

    private void createMenuBar() {
        final var menuBar = new MenuBar();

        final JMenu mainMenu = menuBar.addMenu("Main Menu", KeyEvent.VK_M);
        final JMenu fileMenu = menuBar.addMenu("File", KeyEvent.VK_F);
        final JMenu othersMenu = menuBar.addMenu("Others", KeyEvent.VK_O);
        final JMenu helpMenu = menuBar.addMenu("Help", KeyEvent.VK_H);

        menuBar.addItemToMenu(mainMenu.getText(), "Open Document", this::importData, true);
        menuBar.addItemToMenu(mainMenu.getText(), "Open Blank Document", this::createBlankData, true);
        exportItem = menuBar.addItemToMenu(mainMenu.getText(), "Export Table", e -> mainController.doExportData(), false);
        addRowBtn = menuBar.addItemToMenu(fileMenu.getText(), "Add Row", this::addNewRow, false);
        addColumnBtn = menuBar.addItemToMenu(fileMenu.getText(), "Add Column", this::addNewColumn, false);
        findBtn = menuBar.addItemToMenu(fileMenu.getText(), "Find Cell", e -> mainController.doSearch(), false);
        rowDecoratorBtn = menuBar.addToggleItemToMenu(othersMenu.getText(), "Add Row Numbering", this::toggleRowNumbering, false);
        formulaDecoratorBtn = menuBar.addToggleItemToMenu(othersMenu.getText(), "Toggle Formulas", this::toggleFormulas, false);

        menuBar.addItemToMenu(helpMenu.getText(), "Help", e -> showInfo("Keyboard shortcuts:\n\nCTRL-F: to search within the table\nCTRL-S: save the table\nCTRL-Z: undo changes\nCTRL-R: redo changes"), true);

        this.setJMenuBar(menuBar);
    }

    private void baseConfig() {
        registerKeyShortcuts();

        this.setTitle("Table Editor");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setSize(windowWidth, windowHeight);
    }

    private void registerKeyShortcuts() {
        this.getRootPane().registerKeyboardAction(e -> exportItem.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.getRootPane().registerKeyboardAction(e -> mainController.undoCommand(), KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.getRootPane().registerKeyboardAction(e -> mainController.redoCommand(), KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.getRootPane().registerKeyboardAction(e -> mainController.doSearch(), KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayData(Data data) {
        List<String> headers = data.getHeaders();
        List<List<String>> rowData = data.getData();

        String[][] tableData = new String[rowData.size()][headers.size()];
        IntStream.range(0, rowData.size())
                .forEach(idx -> tableData[idx] = rowData.get(idx).toArray(new String[]{}));

        table.displayData(table.constructModel(tableData, headers.toArray(new String[]{})));

        addRowBtn.setEnabled(true);
        addColumnBtn.setEnabled(true);
        exportItem.setEnabled(true);
        rowDecoratorBtn.setEnabled(true);
        formulaDecoratorBtn.setEnabled(true);
        findBtn.setEnabled(true);
    }

    private void importData(ActionEvent e) {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV (*.csv)", "csv"));
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int hasOpened = chooser.showOpenDialog(MainPanel.this);

        if (hasOpened != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final File selectedFile = chooser.getSelectedFile();
        mainController.doImportData(selectedFile.getAbsolutePath());
    }

    private void createBlankData(ActionEvent e) {
        mainController.doCreateBlankData();
    }

    public Optional<File> saveData() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setFileFilter(new FileNameExtensionFilter("JSON (*.json)", "json"));
        int hasOpened = chooser.showSaveDialog(this);

        if (hasOpened != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }

        return Optional.ofNullable(chooser.getSelectedFile());
    }

    public Optional<Map.Entry<SearchStrategy, String>> getSearchInput() {
        final JPanel mainSearchPanel = new JPanel(new GridLayout(2, 1));
        mainSearchPanel.add(new JLabel("Find cell"));

        final JPanel subSearchPanel = new JPanel();

        final SearchRadioButton subStringBtn = new SearchRadioButton("Match substring", new SubStringSearchStrategy(), true);
        subSearchPanel.add(subStringBtn);
        final SearchRadioButton wholeCellBtn = new SearchRadioButton("Match entire cell content", new WholeCellSearchStrategy());
        subSearchPanel.add(wholeCellBtn);
        final SearchRadioButton matchCaseBtn = new SearchRadioButton("Match case", new MatchCaseSearchStrategy());
        subSearchPanel.add(matchCaseBtn);

        ButtonGroup radioBtns = new ButtonGroup();
        radioBtns.add(subStringBtn);
        radioBtns.add(wholeCellBtn);
        radioBtns.add(matchCaseBtn);

        mainSearchPanel.add(subSearchPanel);

        String searchWord = JOptionPane.showInputDialog(null, mainSearchPanel);

        if (searchWord == null || searchWord.isBlank()) {
            return Optional.empty();
        }

        if (subStringBtn.isSelected()) {
            return Optional.of(Map.entry(subStringBtn.getSearchStrategy(), searchWord));
        } else if (wholeCellBtn.isSelected()) {
            return Optional.of(Map.entry(wholeCellBtn.getSearchStrategy(), searchWord));
        } else if (matchCaseBtn.isSelected()) {
            return Optional.of(Map.entry(matchCaseBtn.getSearchStrategy(), searchWord));
        }

        throw new ViewException("No search radio button selected");
    }

    public void selectCell(Position position) {
        table.getTable().setRowSelectionInterval(position.getRow(), position.getRow());
        table.getTable().setColumnSelectionInterval(position.getColumn(), position.getColumn());
    }

    public void clearSelection() {
        table.getTable().clearSelection();
    }

    public void showChart(JFreeChart chart) {
        final JFrame chartFrame = new JFrame();
        chartFrame.setContentPane(new ChartPanel(chart));
        chartFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setSize(windowWidth, windowHeight);
        chartFrame.setVisible(true);
    }

}
