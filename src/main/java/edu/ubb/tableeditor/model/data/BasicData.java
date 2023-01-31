package edu.ubb.tableeditor.model.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BasicData implements Data {

    protected List<String> headers;
    protected List<List<String>> data;

    public BasicData() {
        this.headers = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    @Override
    public List<String> getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    @Override
    public List<List<String>> getData() {
        return data;
    }

    @Override
    public void setData(List<List<String>> data) {
        this.data = data;
    }

    @Override
    public List<Map.Entry<String, List<String>>> getValueRestrictions() {
        return Collections.emptyList();
    }

    @Override
    public void setValueRestrictions(List<Map.Entry<String, List<String>>> valueRestrictions) {
        // do nothing
    }

    @Override
    public Map<String, Object> getAugmentation() {
        return Collections.emptyMap();
    }

    @Override
    public void setAugmentation(Map<String, Object> augmentation) {
        // do nothing
    }

}
