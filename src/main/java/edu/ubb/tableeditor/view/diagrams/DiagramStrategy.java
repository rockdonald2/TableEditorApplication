package edu.ubb.tableeditor.view.diagrams;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.service.exception.ServiceException;
import org.jfree.chart.JFreeChart;

public interface DiagramStrategy {

    JFreeChart createChart(Data data, int positionIdx) throws ServiceException;

}
