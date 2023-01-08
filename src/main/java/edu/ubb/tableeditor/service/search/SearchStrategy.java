package edu.ubb.tableeditor.service.search;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.model.field.Position;

public interface SearchStrategy {

    Position search(Data data, String searchWord);

}
