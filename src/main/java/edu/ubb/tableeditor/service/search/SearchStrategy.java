package edu.ubb.tableeditor.service.search;

import edu.ubb.tableeditor.model.Data;
import edu.ubb.tableeditor.model.Position;

public interface SearchStrategy {

    Position search(Data data, String searchWord);

}
