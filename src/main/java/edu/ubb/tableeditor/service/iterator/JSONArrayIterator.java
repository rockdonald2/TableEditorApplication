package edu.ubb.tableeditor.service.iterator;

import edu.ubb.tableeditor.service.exception.ServiceException;
import edu.ubb.tableeditor.utils.json.JSONArray;
import edu.ubb.tableeditor.utils.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class JSONArrayIterator implements Iterator<JSONObject> {

    private final JSONArray array;
    private int currIdx = 0;


    public JSONArrayIterator(Path inputFile) throws IOException, IllegalArgumentException {
        Objects.requireNonNull(inputFile);

        final String content = Files.readString(inputFile);

        array = new JSONArray(content);
    }

    public JSONArrayIterator(JSONArray array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return currIdx < array.length();
    }

    @Override
    public JSONObject next() throws ServiceException {
        JSONObject parsed;

        Optional<JSONArray> nextArr;
        Optional<JSONObject> nextObj;

        nextArr = array.getJSONArray(currIdx);
        if (nextArr.isEmpty()) {
            nextObj = array.getJSONObject(currIdx);
            parsed = nextObj.orElseThrow(() -> new ServiceException("JSONArrayIterator only support iterating over JSONObject/JSONArray"));
        } else {
            parsed = nextArr.get();
        }

        currIdx++;
        return parsed;
    }

}
