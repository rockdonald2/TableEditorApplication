package edu.ubb.tableeditor.service.loader.json;

import edu.ubb.tableeditor.model.data.AugmentedData;
import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.model.data.RestrictedData;
import edu.ubb.tableeditor.model.field.Position;
import edu.ubb.tableeditor.service.exception.ServiceException;
import edu.ubb.tableeditor.service.iterator.Iterator;
import edu.ubb.tableeditor.service.iterator.JsonArrayIterator;
import edu.ubb.tableeditor.service.loader.Importer;
import edu.ubb.tableeditor.utils.input.IOFile;
import edu.ubb.tableeditor.utils.json.JsonArray;
import edu.ubb.tableeditor.utils.json.JsonObject;
import edu.ubb.tableeditor.view.table.decorator.StyleCapableTableDecorator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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

        IntStream.range(0, jsonHeaders.length()).forEach(idx -> {
            headers.add(jsonHeaders.getRawValue(idx).get());
        });

        Iterator<JsonObject> iterator = new JsonArrayIterator(jsonData);

        while (iterator.hasNext()) {
            JsonObject currObj = iterator.next();
            List<String> currData = new ArrayList<>();

            for (int i = 0; i < headers.size(); ++i) {
                final int finalI = i;

                currData.add(currObj.getRawValue(headers.get(i)).orElseThrow(() -> new ServiceException("Missing value for " + headers.get(finalI) + " column at object of index " + finalI)));
            }

            rowData.add(currData);
        }

        data.setHeaders(headers);
        data.setData(rowData);

        if (data instanceof RestrictedData) {
            List<Map.Entry<String, List<String>>> restrictions = new ArrayList<>();

            Optional<JsonObject> jsonRestrictions = object.getJsonObject("valueRestrictions");
            jsonRestrictions.ifPresent(jsonObject -> headers.forEach(header -> {
                jsonObject.getJsonArray(header).ifPresent(arr -> {
                    List<String> restrictionsForColumn = new ArrayList<>(arr.length());

                    IntStream.range(0, arr.length()).forEach(idx -> restrictionsForColumn.add(arr.getRawValue(idx).get()));

                    restrictions.add(Map.entry(header, restrictionsForColumn));
                });
            }));

            data.setValueRestrictions(restrictions);
        }

        if (data instanceof AugmentedData) {
            Optional<JsonObject> jsonStyles = object.getJsonObject("styles");
            int nOfCols = headers.size();
            int nOfRows = rowData.size();

            jsonStyles.ifPresent(jsonObject -> {
                Map<Position, List<StyleCapableTableDecorator.Style>> styles = new HashMap<>();

                for (int i = 0; i < nOfRows; ++i) {
                    for (int j = 0; j < nOfCols; ++j) {
                        String posString = String.format("%s%s%s", i, Position.SEPARATOR, j);
                        List<StyleCapableTableDecorator.Style> appliedStyles = new ArrayList<>();

                        jsonObject.getJsonArray(posString).ifPresent(styleArr -> IntStream.range(0, styleArr.length()).forEach(idx -> appliedStyles.add(StyleCapableTableDecorator.Style.of(styleArr.getRawValue(idx).get()))));

                        if (!appliedStyles.isEmpty()) {
                            styles.put(Position.of(posString), appliedStyles);
                        }
                    }
                }

                data.getAugmentation().put("style", styles);
            });
        }

        return data;
    }

}
