package edu.ubb.tableeditor.service.export.json;

import edu.ubb.tableeditor.model.DecimalField;
import edu.ubb.tableeditor.model.IntegerField;
import edu.ubb.tableeditor.model.TextField;
import edu.ubb.tableeditor.service.export.ExportVisitor;

import java.math.BigDecimal;
import java.util.List;

public class JsonExportVisitor implements ExportVisitor {

    @Override
    public String visit(IntegerField number) {
        return String.format("\"%s\": %s", number.getKey(), number.getValue());
    }

    @Override
    public String visit(TextField text) {
        escapeTextField(text);
        return String.format("\"%s\": \"%s\"", text.getKey(), text.getValue());
    }

    private void escapeTextField(TextField text) {
        final List<String> metaChars = List.of("\\", "\"");
        metaChars.forEach(metaChar -> text.setValue(text.getValue().replace(metaChar, String.format("\\\\\\%s", metaChar))));
    }

    @Override
    public String visit(DecimalField decimal) {
        return String.format("\"%s\": %s", decimal.getKey(), BigDecimal.valueOf(decimal.getValue()).toPlainString());
    }

}
