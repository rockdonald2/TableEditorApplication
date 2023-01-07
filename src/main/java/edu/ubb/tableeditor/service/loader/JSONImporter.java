package edu.ubb.tableeditor.service.loader;

import edu.ubb.tableeditor.model.BaseData;
import edu.ubb.tableeditor.model.Data;
import edu.ubb.tableeditor.service.exception.ServiceException;
import edu.ubb.tableeditor.service.iterator.Iterator;
import edu.ubb.tableeditor.service.iterator.JSONArrayIterator;
import edu.ubb.tableeditor.utils.json.JSONArray;
import edu.ubb.tableeditor.utils.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class JSONImporter implements Importer {

    @Override
    public Data importData(String fileName) throws ServiceException {
        final BaseData data = new BaseData();
        final Path file = Path.of(fileName);

        if (Files.notExists(file)) {
            throw new ServiceException("Given file for import does not exists");
        }

        JSONObject object;
        try {
            object = new JSONObject(Files.readString(file));
        } catch (IOException e) {
            throw new ServiceException("Given file for import does not exists", e);
        }

        JSONArray jsonHeaders = object.getJSONArray("headers").orElseThrow(() -> new ServiceException("Missing headers field from input JSON"));
        JSONArray jsonData = object.getJSONArray("data").orElseThrow(() -> new ServiceException("Missing data field from input JSON"));

        List<String> headers = new ArrayList<>();
        List<List<String>> rowData = new ArrayList<>();

        IntStream.range(0, jsonHeaders.length()).forEach(idx -> {
            headers.add(jsonHeaders.getRawValue(idx).get());
        });

        Iterator<JSONObject> iterator = new JSONArrayIterator(jsonData);

        while (iterator.hasNext()) {
            JSONObject currObj = iterator.next();
            List<String> currData = new ArrayList<>();

            for (int i = 0; i < headers.size(); ++i) {
                final int finalI = i;
                currData.add(currObj.getRawValue(headers.get(i)).orElseThrow(() -> new ServiceException("Missing value for " + headers.get(finalI) + " column at index " + finalI)));
            }

            rowData.add(currData);
        }

        data.setHeaders(headers);
        data.setData(rowData);

        return data;
    }

}
