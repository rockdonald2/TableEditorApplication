package edu.ubb.tableeditor.service.export;

import edu.ubb.tableeditor.model.Data;
import edu.ubb.tableeditor.service.converter.Converter;
import edu.ubb.tableeditor.service.converter.DecimalConverter;
import edu.ubb.tableeditor.service.converter.IntegerConverter;
import edu.ubb.tableeditor.service.converter.PhoneNumberConverter;

import java.util.List;

public abstract class Exporter {

    protected Converter converter;

    protected Exporter() {
        this.converter = Converter.link(new PhoneNumberConverter(), new IntegerConverter(), new DecimalConverter());
    }

    public final String exportData(Data data) {
        StringBuilder exportedData = new StringBuilder();
        exportLogic(exportedData, data.getHeaders(), data.getData(), exportVisitor());
        return exportedData.toString();
    }

    public abstract ExportVisitor exportVisitor();

    public abstract void exportLogic(StringBuilder exportedData, List<String> headers, List<List<String>> rowData, ExportVisitor exporterVisitor);

}
