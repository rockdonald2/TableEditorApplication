package edu.ubb.tableeditor.service.sort;

import edu.ubb.tableeditor.model.Data;

public interface SortStrategy {

    void sort(Data data, ComparatorStrategy comparatorStrategy);

}
