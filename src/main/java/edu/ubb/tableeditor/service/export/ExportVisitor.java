package edu.ubb.tableeditor.service.export;

import edu.ubb.tableeditor.model.DecimalField;
import edu.ubb.tableeditor.model.IntegerField;
import edu.ubb.tableeditor.model.PhoneNumberField;
import edu.ubb.tableeditor.model.TextField;

public interface ExportVisitor {

    String visit(IntegerField number);

    String visit(TextField text);

    String visit(DecimalField decimal);

    String visit(PhoneNumberField phoneNumber);

}
