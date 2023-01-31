package edu.ubb.tableeditor.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestrictedData extends BasicData {

    private List<Map.Entry<String, List<String>>> valueRestrictions;

    public RestrictedData() {
        super();
        this.valueRestrictions = new ArrayList<>();
    }

    @Override
    public List<Map.Entry<String, List<String>>> getValueRestrictions() {
        return valueRestrictions;
    }

    @Override
    public void setValueRestrictions(List<Map.Entry<String, List<String>>> valueRestrictions) {
        this.valueRestrictions = valueRestrictions;
    }

}
