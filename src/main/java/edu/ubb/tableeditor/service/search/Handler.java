package edu.ubb.tableeditor.service.search;

public interface Handler<I, O> {

    O process(I input);

}
