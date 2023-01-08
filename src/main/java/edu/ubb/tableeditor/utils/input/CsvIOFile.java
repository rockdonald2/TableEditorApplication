package edu.ubb.tableeditor.utils.input;

import edu.ubb.tableeditor.service.export.Exporter;
import edu.ubb.tableeditor.service.loader.csv.CsvImporter;
import edu.ubb.tableeditor.service.loader.Importer;

import java.nio.file.Path;

public class CsvIOFile extends IOFile {

    public CsvIOFile(Path file) {
        super(file);
    }

    @Override
    public Exporter getExporter() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Importer getImporter() {
        return new CsvImporter();
    }

}
