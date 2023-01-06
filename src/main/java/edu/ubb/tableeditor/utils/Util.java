package edu.ubb.tableeditor.utils;

import edu.ubb.tableeditor.annotation.Singleton;

import java.util.Optional;

@Singleton
public final class Util {

    private Util() {
    }

    public static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

}
