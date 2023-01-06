package edu.ubb.tableeditor.model;

import edu.ubb.tableeditor.service.search.SearchStrategy;
import edu.ubb.tableeditor.service.sort.ComparatorStrategy;
import edu.ubb.tableeditor.service.sort.SortStrategy;

import java.util.List;

public interface Data {

    List<String> getHeaders();

    void setHeaders(List<String> headers);

    List<List<String>> getData();

    void setData(List<List<String>> data);

    default void sort(SortStrategy sortStrategy, ComparatorStrategy comparatorStrategy) {
        sortStrategy.sort(this, comparatorStrategy);
    }

    default Position search(SearchStrategy searchStrategy, String searchWord) {
        return searchStrategy.search(this, searchWord);
    }

}
