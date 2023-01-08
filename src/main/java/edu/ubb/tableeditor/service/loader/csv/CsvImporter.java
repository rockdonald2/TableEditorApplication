package edu.ubb.tableeditor.service.loader.csv;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.service.exception.ServiceException;
import edu.ubb.tableeditor.service.iterator.CsvIterator;
import edu.ubb.tableeditor.service.iterator.Iterator;
import edu.ubb.tableeditor.service.loader.Importer;
import edu.ubb.tableeditor.utils.input.IOFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter implements Importer {

    @Override
    public Data importData(IOFile ioFile) throws ServiceException {
        final Data data = Data.get();
        final Path file = ioFile.getPath();

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
