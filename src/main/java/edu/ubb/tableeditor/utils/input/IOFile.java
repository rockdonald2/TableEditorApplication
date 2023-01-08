package edu.ubb.tableeditor.utils.input;

import edu.ubb.tableeditor.service.export.Exporter;
import edu.ubb.tableeditor.service.loader.Importer;
import edu.ubb.tableeditor.utils.Utility;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public abstract class IOFile {

    protected final Path file;

    protected IOFile(Path file) {
        Objects.requireNonNull(file);
        this.file = file;
    }

    public static IOFile getFileAsIOFile(Path file) throws IllegalArgumentException {
        final String extension = Utility.getExtensionByStringHandling(file.toFile().getName())
                .orElseThrow(() -> new IllegalArgumentException("Input file does not have a proper extension"));

        switch (extension) {
            case "json" -> {
                return new JsonIOFile(file);
            }
            case "csv" -> {
                return new CsvIOFile(file);
            }
            default -> throw new IllegalArgumentException("No suitable input file format found");
        }
    }

    public static List<FileNameExtensionFilter> getInputExtensions() {
        return List.of(
                new FileNameExtensionFilter("JSON (*.json)", "json"),
                new FileNameExtensionFilter("CSV (*.csv)", "csv")
        );
    }

    public static List<FileNameExtensionFilter> getOutputExtensions() {
        return List.of(
                new FileNameExtensionFilter("JSON (*.json)", "json")
        );
    }

    public abstract Exporter getExporter();

    public abstract Importer getImporter();

    public Path getPath() {
        return this.file;
    }

}
