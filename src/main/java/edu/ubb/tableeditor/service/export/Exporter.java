package edu.ubb.tableeditor.service.export;

import edu.ubb.tableeditor.model.converter.Converter;
import edu.ubb.tableeditor.model.converter.DecimalConverter;
import edu.ubb.tableeditor.model.converter.IntegerConverter;
import edu.ubb.tableeditor.model.converter.PhoneNumberConverter;
import edu.ubb.tableeditor.model.data.Data;

public abstract class Exporter {

    protected Converter converter;

    protected Exporter() {
        this.converter = Converter.link(new PhoneNumberConverter(), new IntegerConverter(), new DecimalConverter());
    }

    public final String exportData(Data data) {
        StringBuilder exportedData = new StringBuilder();
        exportLogic(exportedData, data, exportVisitor());
        return exportedData.toString();
    }

    public abstract ExportVisitor exportVisitor();

    public abstract void exportLogic(StringBuilder exportedData, Data data, ExportVisitor exporterVisitor);

}
