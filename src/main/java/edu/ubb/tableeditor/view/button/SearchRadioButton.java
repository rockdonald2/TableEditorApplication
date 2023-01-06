package edu.ubb.tableeditor.view.button;

import edu.ubb.tableeditor.service.search.SearchStrategy;

import javax.swing.*;

public class SearchRadioButton extends JRadioButton {

    private final SearchStrategy searchStrategy;

    public SearchRadioButton(String text, SearchStrategy searchStrategy) {
        super(text);
        this.searchStrategy = searchStrategy;
    }

    public SearchRadioButton(String text, SearchStrategy searchStrategy, boolean selected) {
        super(text, selected);
        this.searchStrategy = searchStrategy;
    }

    public SearchStrategy getSearchStrategy() {
        return searchStrategy;
    }

}
