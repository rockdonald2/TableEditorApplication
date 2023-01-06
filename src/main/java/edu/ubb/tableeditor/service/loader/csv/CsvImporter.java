package edu.ubb.tableeditor.service.loader.csv;

import edu.ubb.tableeditor.model.CsvData;
import edu.ubb.tableeditor.model.Data;
import edu.ubb.tableeditor.service.exception.ServiceException;
import edu.ubb.tableeditor.service.iterator.CsvIterator;
import edu.ubb.tableeditor.service.iterator.Iterator;
import edu.ubb.tableeditor.service.loader.Importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter implements Importer {

    @Override
    public Data importData(String fileName) throws ServiceException {
        final Data data = new CsvData();
        final Path file = Path.of(fileName);

        if (Files.notExists(file)) {
            throw new ServiceException("Given file for import does not exists");
        }

        Iterator<List<String>> iterator;
        try {
            iterator = new CsvIterator(file);
        } catch (IOException e) {
            throw new ServiceException("Exception occurred while processing import file", e);
        }

        List<String> headers;
        List<List<String>> rowData = new ArrayList<>();

        headers = iterator.next();

        while (iterator.hasNext()) {
            rowData.add(iterator.next());
        }

        data.setData(rowData);
        data.setHeaders(headers);

        return data;
    }

}
