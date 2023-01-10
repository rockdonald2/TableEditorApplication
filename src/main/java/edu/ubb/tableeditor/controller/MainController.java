package edu.ubb.tableeditor.controller;

import edu.ubb.tableeditor.annotation.Flag;
import edu.ubb.tableeditor.annotation.Singleton;
import edu.ubb.tableeditor.command.Command;
import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.model.field.Position;
import edu.ubb.tableeditor.service.exception.ServiceException;
import edu.ubb.tableeditor.service.search.SearchStrategy;
import edu.ubb.tableeditor.service.sort.DefaultSortStrategy;
import edu.ubb.tableeditor.utils.input.IOFile;
import edu.ubb.tableeditor.view.MainPanel;
import edu.ubb.tableeditor.view.diagrams.DiagramStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Singleton
public final class MainController {

    private static MainController instance;

    private final Deque<Command<?, ?>> commands = new ArrayDeque<>();
    private final Deque<Command<?, ?>> undidCommands = new ArrayDeque<>();

    private MainPanel mainPanel;
    private Data data;

    @Flag
    private boolean initialized;

    private MainController() {
    }

    public static synchronized MainController instance() {
        if (instance == null) {
            instance = new MainController();
        }

        return instance;
    }

    public void init() {
        if (initialized) {
            throw new IllegalStateException(String.format("%s already initialized", MainPanel.class.getName()));
        }

        doCreateBlankData();
        this.mainPanel = MainPanel.instance();
        this.mainPanel.init();
        initialized = true;
    }

    public int getColumns() {
        return data.getHeaders().size();
    }

    public int getRows() {
        return data.getData().size();
    }

    public void doImportData(File file) {
        try {
            IOFile ioFile = IOFile.getFileAsIOFile(file.toPath());

            this.data = ioFile.getImporter().importData(ioFile);

            doDisplayData();
            mainPanel.showInfo("Successfully imported data");
        } catch (ServiceException e) {
            mainPanel.showError(e.getMessage());
        }
    }

    public void doDisplayData() {
        mainPanel.displayData(data);
    }

    public void doExportData() {
        try {
            Optional<File> optionalSaveFile = mainPanel.showSavePanelAndGetFile();

            if (optionalSaveFile.isEmpty()) return;

            final Path saveFile = optionalSaveFile.get().toPath();
            final String dataStr = IOFile.getFileAsIOFile(saveFile).getExporter().exportData(data);

            try { // NOSONAR; intentionally
                Files.writeString(saveFile, dataStr);
                mainPanel.showInfo("Successfully exported data");
            } catch (IOException e) {
                throw new ServiceException("Failed to save data", e);
            }
        } catch (ServiceException | IllegalArgumentException e) {
            mainPanel.showError(e.getMessage());
        }
    }

    public void doModifyValueAt(int row, int column, String value) {
        List<List<String>> rowData = data.getData();
        rowData.get(row).set(column, value);
    }

    public void doAddNewRow() {
        final List<String> newRow = new ArrayList<>(data.getHeaders().size());
        IntStream.range(0, data.getHeaders().size()).forEach(idx -> newRow.add(""));

        data.getData().add(newRow);
        doDisplayData();
    }

    public void doAddNewColumn(String columnName) {
        List<List<String>> rowData = data.getData();
        List<String> headers = data.getHeaders();

        rowData.forEach(row -> row.add(""));
        headers.add(columnName);

        doDisplayData();
    }

    public List<String> doDeleteRowAt(int rowIdx) {
        List<String> lostData = data.getData().remove(rowIdx);
        doDisplayData();

        return lostData;
    }

    public List<String> doDeleteColumnAt(int columnIdx) {
        List<String> headers = data.getHeaders();
        List<List<String>> rowData = data.getData();

        List<String> lostData = new ArrayList<>();
        rowData.forEach(row -> lostData.add(row.remove(columnIdx)));
        headers.remove(columnIdx);

        doDisplayData();

        return lostData;
    }

    public void doAddRowAt(int rowIdx, List<String> rowData) {
        this.data.getData().add(rowIdx, rowData);
        doDisplayData();
    }

    public void doAddColumnAt(int colIdx, List<String> colData, String colName) {
        this.data.getHeaders().add(colIdx, colName);

        AtomicInteger idx = new AtomicInteger();
        this.data.getData().forEach(row -> row.add(colIdx, colData.get(idx.getAndIncrement())));
        doDisplayData();
    }

    public void doSearch() {
        final Optional<Map.Entry<SearchStrategy, String>> search = mainPanel.getSearchInput();

        if (search.isEmpty()) {
            return;
        }

        final Position position = data.search(search.get().getKey(), search.get().getValue());

        if (position != null) {
            mainPanel.selectTableCell(position);
        } else {
            mainPanel.clearTableSelection();
        }
    }

    public void doSort(int colIdx) {
        data.sort(new DefaultSortStrategy(), (v1, v2) -> v1.get(colIdx).compareTo(v2.get(colIdx)));
        doDisplayData();
    }

    public String getValueAt(Position position) {
        return data.getData().get(position.getRow()).get(position.getColumn());
    }

    public String getColumnNameAt(int col) {
        return data.getHeaders().get(col);
    }

    public void setHeaders(String[] headers) {
        data.setHeaders(new ArrayList<>(List.of(headers)));
    }

    public void addValueAt(int rowIdx, int colIdx, String value) {
        data.getData().get(rowIdx).add(colIdx, value);
    }

    public void setValueAt(int rowIdx, int colIdx, String value) {
        data.getData().get(rowIdx).set(colIdx, value);
    }

    public void executeCommand(Command<?, ?> command) {
        commands.push(command);
        command.execute();

        if (!undidCommands.isEmpty()) {
            undidCommands.clear();
        }

        toggleUndoRedoBtns();
    }

    public void undoCommand() {
        try {
            Command<?, ?> undo = commands.pop();
            undidCommands.push(undo);
            undo.unexecute();
        } catch (RuntimeException ignored) {
            // ignored
        }

        toggleUndoRedoBtns();
    }

    public void redoCommand() {
        try {
            Command<?, ?> redo = undidCommands.pop();
            commands.push(redo);
            redo.execute();
        } catch (RuntimeException ignored) {
            // ignored
        }

        toggleUndoRedoBtns();
    }

    private void toggleUndoRedoBtns() {
        mainPanel.toggleUndoBtn(!commands.isEmpty());
        mainPanel.toggleRedoBtn(!undidCommands.isEmpty());
    }

    public void showDiagram(DiagramStrategy diagramStrategy, int positionIdx) {
        try {
            mainPanel.showChart(diagramStrategy.createChart(data, positionIdx));
        } catch (ServiceException e) {
            mainPanel.showError("Cannot create the selected chart based on the values");
        }
    }

    public void doCreateBlankData() {
        this.data = Data.get();
    }

    public void doSetValueRestrictions() {
        mainPanel.showValueRestrictionsPanel(data);
    }

    public void addValueRestriction(String header, List<String> valueRestriction) {
        removeValueRestriction(header);
        this.data.getValueRestrictions().add(Map.entry(header, valueRestriction));
        doDisplayData();
    }

    public void removeValueRestriction(String header) {
        final Stream<Map.Entry<String, List<String>>> newValueRestrictions = this.data.getValueRestrictions()
                .stream().filter(restriction -> !restriction.getKey().equals(header));

        this.data.setValueRestrictions(new ArrayList<>(newValueRestrictions.toList()));
        doDisplayData();
    }

}
