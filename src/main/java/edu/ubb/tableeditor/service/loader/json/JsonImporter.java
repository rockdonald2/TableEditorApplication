package edu.ubb.tableeditor.service.loader.json;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.service.exception.ServiceException;
import edu.ubb.tableeditor.service.iterator.Iterator;
import edu.ubb.tableeditor.service.iterator.JsonArrayIterator;
import edu.ubb.tableeditor.service.loader.Importer;
import edu.ubb.tableeditor.utils.input.IOFile;
import edu.ubb.tableeditor.utils.json.JsonArray;
import edu.ubb.tableeditor.utils.json.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class JsonImporter implements Importer {

    @Override
    public Data importData(IOFile ioFile) throws ServiceException {
        final Data data = Data.get();
        final Path file = ioFile.getPath();

        if (Files.notExists(file)) {
            throw new ServiceException("Given file for import does not exists");
        }

        JsonObject object;
        try {
            object = new JsonObject(Files.readString(file));
        } catch (IOException e) {
            throw new ServiceException("Given file for import does not exists", e);
        }

        JsonArray jsonHeaders = object.getJsonArray("headers").orElseThrow(() -> new ServiceException("Missing headers field from input JSON"));
        JsonArray jsonData = object.getJsonArray("data").orElseThrow(() -> new ServiceException("Missing data field from input JSON"));

        List<String> headers = new ArrayList<>();
        List<List<String>> rowData = new ArrayList<>();
        List<Map.Entry<String, List<String>>> restrictions = new ArrayList<>();

        IntStream.range(0, jsonHeaders.length()).forEach(idx -> {
            headers.add(jsonHeaders.getRawValue(idx).get());
        });

        Iterator<JsonObject> iterator = new JsonArrayIterator(jsonData);

        while (iterator.hasNext()) {
            JsonObject currObj = iterator.next();
            List<String> currData = new ArrayList<>();

            for (int i = 0; i < headers.size(); ++i) {
                final int finalI = i;

                currData.add(
                        currObj
                                .getRawValue(headers.get(i))
                                .orElseThrow(
                                        () -> new ServiceException("Missing value for " + headers.get(finalI) + " column at object of index " + finalI)
                                )
                );
            }

            rowData.add(currData);
        }

        data.setHeaders(headers);
        data.setData(rowData);

        Optional<JsonObject> jsonRestrictions = object.getJsonObject("valueRestrictions");
        jsonRestrictions.ifPresent(jsonObject -> headers.forEach(header -> {
            jsonObject.getJsonArray(header).ifPresent(arr -> {
                List<String> restrictionsForColumn = new ArrayList<>(arr.length());

                IntStream.range(0, arr.length())
                        .forEach(idx -> restrictionsForColumn.add(arr.getRawValue(idx).get()));

                restrictions.add(Map.entry(header, restrictionsForColumn));
            });
        }));

        data.setValueRestrictions(restrictions);

        return data;
    }

}
