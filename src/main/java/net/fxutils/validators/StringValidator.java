package net.fxutils.validators;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.fxutils.utils.FxStringUtils;

public class StringValidator extends AbstractTextInputControlValidator {

    private StringProperty errMessageRequired = new SimpleStringProperty(this, "errMessageRequired", "This field is mandatory.");

    @Override
    protected String validateText(String text) {
        return getRequired() && FxStringUtils.isBlank(text) ? errMessageRequired.get() : null;
    }

    public String getErrMessageRequired() {
        return errMessageRequired.get();
    }

    public StringProperty errMessageRequiredProperty() {
        return errMessageRequired;
    }

    public void setErrMessageRequired(String errMessageRequired) {
        this.errMessageRequired.set(errMessageRequired);
    }
}
