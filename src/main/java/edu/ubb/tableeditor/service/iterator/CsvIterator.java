package edu.ubb.tableeditor.service.iterator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvIterator implements Iterator<List<String>> {

    private static final String SEPARATOR = ",";

    private final java.util.Iterator<String> iterator;

    public CsvIterator(Path inputFile) throws IOException {
        Objects.requireNonNull(inputFile);

        try (BufferedReader tmp = new BufferedReader(new FileReader(inputFile.toFile()))) {
            this.iterator = tmp.lines().toList().iterator();
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public List<String> next() {
        return new ArrayList<>(List.of(iterator.next().split(SEPARATOR)));
    }

}
