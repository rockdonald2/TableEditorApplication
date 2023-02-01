package edu.ubb.tableeditor.service.export.json;

import edu.ubb.tableeditor.model.data.Data;
import edu.ubb.tableeditor.model.field.Field;
import edu.ubb.tableeditor.model.field.Position;
import edu.ubb.tableeditor.service.export.ExportVisitor;
import edu.ubb.tableeditor.service.export.Exporter;
import edu.ubb.tableeditor.utils.json.JsonConstants;
import edu.ubb.tableeditor.view.table.decorator.StyleCapableTableDecorator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonExporter extends Exporter {

    public static final String QUOTE_WRAPPER = "\"%s\"";

    @Override
    public ExportVisitor exportVisitor() {
        return new JsonExportVisitor();
    }

    @Override
    public void exportLogic(StringBuilder exportedData, Data data, ExportVisitor exporterVisitor) {
        final var headers = data.getHeaders();
        final var rowData = data.getData();

        exportedData.append(JsonConstants.CURLY_OPEN_BRACKETS);
        exportedData.append("\"headers\"")
                .append(JsonConstants.COLON)
                .append(headers.stream().map(header -> String.format(QUOTE_WRAPPER, header)).toList())
                .append(JsonConstants.COMMA);

        exportedData.append("\"data\"")
                .append(JsonConstants.COLON)
                .append(JsonConstants.SQUARE_OPEN_BRACKETS);
        AtomicInteger fieldIdx = new AtomicInteger(0);
        AtomicInteger elemIdx = new AtomicInteger(0);
        rowData.forEach(row -> {
            exportedData.append(JsonConstants.CURLY_OPEN_BRACKETS);
            fieldIdx.set(0);

            row.forEach(value -> {
                String key = headers.get(fieldIdx.get());
                Field field = converter.convert(key, value).orElseThrow();

                exportedData.append(field.accept(exporterVisitor));

                if (fieldIdx.get() < (headers.size() - 1)) {
                    exportedData.append(JsonConstants.COMMA);
                }

                fieldIdx.getAndIncrement();
            });

            exportedData.append(JsonConstants.CURLY_CLOSE_BRACKETS);

            if (elemIdx.get() < (rowData.size() - 1)) {
                exportedData.append(JsonConstants.COMMA);
            }

            elemIdx.incrementAndGet();
        });

        exportedData.append(JsonConstants.SQUARE_CLOSE_BRACKETS)
                .append(JsonConstants.COMMA)
                .append("\"valueRestrictions\"")
                .append(JsonConstants.COLON)
                .append(JsonConstants.CURLY_OPEN_BRACKETS);

        final List<Map.Entry<String, List<String>>> valueRestrictions = data.getValueRestrictions();
        AtomicInteger restrictionIdx = new AtomicInteger(0);
        valueRestrictions.forEach(restriction -> {
            exportedData.append(String.format(QUOTE_WRAPPER, restriction.getKey()))
                    .append(JsonConstants.COLON)
                    .append(restriction.getValue().stream().map(value -> String.format(QUOTE_WRAPPER, value)).toList());

            if (restrictionIdx.get() < (valueRestrictions.size() - 1)) {
                exportedData.append(JsonConstants.COMMA);
            }

            restrictionIdx.incrementAndGet();
        });

        exportedData.append(JsonConstants.CURLY_CLOSE_BRACKETS);

        Map<Position, List<StyleCapableTableDecorator.Style>> tmpStyles = (Map<Position, List<StyleCapableTableDecorator.Style>>) data.getAugmentation().get("style");

        if (tmpStyles == null) {
            tmpStyles = new HashMap<>();
        }

        final Map<Position, List<StyleCapableTableDecorator.Style>> styles = tmpStyles;

        exportedData
                .append(JsonConstants.COMMA)
                .append("\"styles\"")
                .append(JsonConstants.COLON)
                .append(JsonConstants.CURLY_OPEN_BRACKETS);

        AtomicInteger stylesIdx = new AtomicInteger(0);
        styles.keySet()
                .forEach(position -> {
                    if (styles.get(position) != null && !styles.get(position).isEmpty()) {
                        exportedData
                                .append("\"")
                                .append(position.getRow())
                                .append(Position.SEPARATOR)
                                .append(position.getColumn())
                                .append("\"")
                                .append(JsonConstants.COLON);

                        exportedData
                                .append(styles.get(position).stream().map(style -> String.format(QUOTE_WRAPPER, style.toString())).toList());

                        if (stylesIdx.get() < (styles.size() - 1)) {
                            exportedData.append(JsonConstants.COMMA);
                        }

                        stylesIdx.incrementAndGet();
                    }
                });

        exportedData
                .append(JsonConstants.CURLY_CLOSE_BRACKETS)
                .append(JsonConstants.CURLY_CLOSE_BRACKETS);
    }

}
