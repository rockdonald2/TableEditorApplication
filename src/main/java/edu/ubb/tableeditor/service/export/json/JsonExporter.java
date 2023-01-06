package edu.ubb.tableeditor.service.export.json;

import edu.ubb.tableeditor.model.Field;
import edu.ubb.tableeditor.service.export.ExportVisitor;
import edu.ubb.tableeditor.service.export.Exporter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonExporter extends Exporter {

    @Override
    public ExportVisitor exportVisitor() {
        return new JsonExportVisitor();
    }

    @Override
    public void exportLogic(StringBuilder exportedData, List<String> headers, List<List<String>> rowData, ExportVisitor exporterVisitor) {
        AtomicInteger fieldIdx = new AtomicInteger(0);
        AtomicInteger elemIdx = new AtomicInteger(0);

        exportedData.append("[");
        rowData.forEach(row -> {
            exportedData.append("{");
            fieldIdx.set(0);

            row.forEach(value -> {
                String key = headers.get(fieldIdx.get());
                Field field = converter.convert(key, value).orElseThrow();

                exportedData.append(field.accept(exporterVisitor));

                if (fieldIdx.get() < (headers.size() - 1)) {
                    exportedData.append(",");
                }

                fieldIdx.getAndIncrement();
            });

            exportedData.append("}");

            if (elemIdx.get() < (rowData.size() - 1)) {
                exportedData.append(",");
            }

            elemIdx.incrementAndGet();
        });

        exportedData.append("]");
    }

}
