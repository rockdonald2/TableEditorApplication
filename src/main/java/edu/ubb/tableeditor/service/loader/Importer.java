package edu.ubb.tableeditor.service.loader;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.service.exception.ServiceException;
import edu.ubb.tableeditor.utils.input.IOFile;

public interface Importer {

    Data importData(IOFile ioFile) throws ServiceException;

}
