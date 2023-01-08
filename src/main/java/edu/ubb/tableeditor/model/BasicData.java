package edu.ubb.tableeditor.model;

import java.util.ArrayList;
import java.util.List;

public class BasicData implements Data {

    private List<String> headers;
    private List<List<String>> data;

    public BasicData() {
        this.headers = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    @Override
    public List<String> getHeaders() {
        return headers;
    }

    @Override
    public List<List<String>> getData() {
        return data;
    }

    @Override
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    @Override
    public void setData(List<List<String>> data) {
        this.data = data;
    }

}
