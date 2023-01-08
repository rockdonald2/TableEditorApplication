package edu.ubb.tableeditor.service.export;

import edu.ubb.tableeditor.model.field.DecimalField;
import edu.ubb.tableeditor.model.field.IntegerField;
import edu.ubb.tableeditor.model.field.PhoneNumberField;
import edu.ubb.tableeditor.model.field.TextField;

public interface ExportVisitor {

    String visit(IntegerField number);

    String visit(TextField text);

    String visit(DecimalField decimal);

    String visit(PhoneNumberField phoneNumber);

}
