package edu.ubb.tableeditor.utils.input;

import edu.ubb.tableeditor.service.export.Exporter;
import edu.ubb.tableeditor.service.export.json.JsonExporter;
import edu.ubb.tableeditor.service.loader.Importer;
import edu.ubb.tableeditor.service.loader.json.JsonImporter;

import java.nio.file.Path;

public class JsonIOFile extends IOFile {

    public JsonIOFile(Path file) {
        super(file);
    }

    @Override
    public Exporter getExporter() {
        return new JsonExporter();
    }

    @Override
    public Importer getImporter() {
        return new JsonImporter();
    }

}
