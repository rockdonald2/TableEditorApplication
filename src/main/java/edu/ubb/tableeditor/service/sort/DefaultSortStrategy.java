package edu.ubb.tableeditor.service.sort;

import edu.ubb.tableeditor.model.data.Data;

public class DefaultSortStrategy implements SortStrategy {

    @Override
    public void sort(Data data, ComparatorStrategy comparatorStrategy) {
        data.getData().sort(comparatorStrategy);
    }

}
