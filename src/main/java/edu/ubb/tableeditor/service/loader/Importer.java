package edu.ubb.tableeditor.service.loader;

import edu.ubb.tableeditor.model.Data;
import edu.ubb.tableeditor.service.exception.ServiceException;

public interface Importer {

    Data importData(String fileName) throws ServiceException;

}
