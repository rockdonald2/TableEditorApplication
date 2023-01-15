package edu.ubb.tableeditor.service.search;

import java.util.List;

public class MatchCaseSearchHandler implements Handler<List<List<String>>, List<List<String>>> {

    private final String word;

    public MatchCaseSearchHandler(String word) {
        this.word = word;
    }

    @Override
    public List<List<String>> process(List<List<String>> input) {
        return input.stream()
                .map(i -> i.stream().map(ii -> {
                    if (ii.contains(word)) {
                        return ii;
                    }

                    return "";
                }).filter(ii -> !ii.isBlank()).toList())
                .filter(i -> !i.isEmpty())
                .toList();
    }

}
