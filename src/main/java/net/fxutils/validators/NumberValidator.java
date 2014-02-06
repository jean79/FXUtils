package net.fxutils.validators;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.fxutils.utils.FxStringUtils;

public class NumberValidator extends AbstractTextInputControlValidator {

    public static enum Domain {REAL, WHOLE}

    private ObjectProperty<Domain> domain = new SimpleObjectProperty<>(Domain.REAL);

    private StringProperty errMessageRequired = new SimpleStringProperty(this, "errMessageRequired", "This field is mandatory.");
    private StringProperty errMessageInvalidFormat = new SimpleStringProperty(this, "errMessageInvalidFormat", "Invalid format.");

    @Override
    protected String validateText(String text) {
        if (FxStringUtils.isBlank(text)) {
            return getRequired() ? errMessageRequired.get() : null;
        } else {
            if (domain.get() == Domain.REAL) {
                try {
                    Double.parseDouble(text.trim());
                    return null;
                } catch (NumberFormatException nfe) {
                    return errMessageInvalidFormat.get();
                }
            } else {
                try {
                    Long.parseLong(text.trim());
                    return null;
                } catch (NumberFormatException nfe) {
                    return errMessageInvalidFormat.get();
                }
            }
        }
    }

    public Domain getDomain() {
        return domain.get();
    }

    public ObjectProperty<Domain> domainProperty() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain.set(domain);
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

    public String getErrMessageInvalidFormat() {
        return errMessageInvalidFormat.get();
    }

    public StringProperty errMessageInvalidFormatProperty() {
        return errMessageInvalidFormat;
    }

    public void setErrMessageInvalidFormat(String errMessageInvalidFormat) {
        this.errMessageInvalidFormat.set(errMessageInvalidFormat);
    }
}
