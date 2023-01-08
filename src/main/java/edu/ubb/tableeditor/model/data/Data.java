package edu.ubb.tableeditor.model.data;

import edu.ubb.tableeditor.model.field.Position;
import edu.ubb.tableeditor.service.search.SearchStrategy;
import edu.ubb.tableeditor.service.sort.ComparatorStrategy;
import edu.ubb.tableeditor.service.sort.SortStrategy;
import edu.ubb.tableeditor.utils.PropertiesContext;

import java.util.List;
import java.util.Map;

public interface Data {

    static Data get() {
        final String dataFormat = PropertiesContext.getStringProperty("data.format");

        if ("basic".equalsIgnoreCase(dataFormat)) {
            return new BasicData();
        } else if ("valuerestricted".equalsIgnoreCase(dataFormat)) {
            return new RestrictedData();
        }

        throw new IllegalStateException("No date format specified in properties");
    }

    static DataFormat getFormat() {
        final String dataFormat = PropertiesContext.getStringProperty("data.format");

        if ("basic".equalsIgnoreCase(dataFormat)) {
            return DataFormat.BASIC;
        } else if ("valuerestricted".equalsIgnoreCase(dataFormat)) {
            return DataFormat.VALUERESTRICTED;
        }

        throw new IllegalStateException("No date format specified in properties");
    }

    List<String> getHeaders();

    void setHeaders(List<String> headers);

    List<List<String>> getData();

    void setData(List<List<String>> data);

    List<Map.Entry<String, List<String>>> getValueRestrictions();

    void setValueRestrictions(List<Map.Entry<String, List<String>>> valueRestrictions);

    default void sort(SortStrategy sortStrategy, ComparatorStrategy comparatorStrategy) {
        sortStrategy.sort(this, comparatorStrategy);
    }

    default Position search(SearchStrategy searchStrategy, String searchWord) {
        return searchStrategy.search(this, searchWord);
    }

    enum DataFormat {
        BASIC,
        VALUERESTRICTED;
    }

}
