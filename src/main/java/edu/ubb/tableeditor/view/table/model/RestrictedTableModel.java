package edu.ubb.tableeditor.view.table.model;

import edu.ubb.tableeditor.model.data.Data;

import java.util.List;
import java.util.Map;

public class RestrictedTableModel extends BasicTableModel {

    private List<Map.Entry<String, List<String>>> valueRestrictions;

    public RestrictedTableModel(Data data) {
        super(data);
        this.setValueRestrictions(data.getValueRestrictions());
    }

    public List<Map.Entry<String, List<String>>> getValueRestrictions() {
        return valueRestrictions;
    }

    public void setValueRestrictions(List<Map.Entry<String, List<String>>> valueRestrictions) {
        this.valueRestrictions = valueRestrictions;
    }

}
