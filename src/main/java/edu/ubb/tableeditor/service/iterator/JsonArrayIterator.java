package edu.ubb.tableeditor.service.iterator;

import edu.ubb.tableeditor.service.exception.ServiceException;
import edu.ubb.tableeditor.utils.json.JsonArray;
import edu.ubb.tableeditor.utils.json.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class JsonArrayIterator implements Iterator<JsonObject> {

    private final JsonArray array;
    private int currIdx = 0;


    public JsonArrayIterator(Path inputFile) throws IOException, IllegalArgumentException {
        Objects.requireNonNull(inputFile);

        final String content = Files.readString(inputFile);

        array = new JsonArray(content);
    }

    public JsonArrayIterator(JsonArray array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return currIdx < array.length();
    }

    @Override
    public JsonObject next() throws ServiceException {
        JsonObject parsed;

        Optional<JsonArray> nextArr;
        Optional<JsonObject> nextObj;

        nextArr = array.getJsonArray(currIdx);
        if (nextArr.isEmpty()) {
            nextObj = array.getJsonObject(currIdx);
            parsed = nextObj.orElseThrow(() -> new ServiceException("JSONArrayIterator only support iterating over JSONObject/JSONArray"));
        } else {
            parsed = nextArr.get();
        }

        currIdx++;
        return parsed;
    }

}
