package edu.ubb.tableeditor.service.iterator;

import edu.ubb.tableeditor.service.exception.ServiceException;

public interface Iterator<T> {

    boolean hasNext();

    T next() throws ServiceException;

}
