package edu.ubb.tableeditor.service.sort;

import edu.ubb.tableeditor.model.data.Data;

public interface SortStrategy {

    void sort(Data data, ComparatorStrategy comparatorStrategy);

}
