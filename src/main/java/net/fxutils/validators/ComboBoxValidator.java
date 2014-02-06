package net.fxutils.validators;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import net.fxutils.bindings.FxBindings;

public class ComboBoxValidator<T extends ComboBox> extends AbstractControlValidator<T> {

    private BooleanProperty required = new SimpleBooleanProperty(this, "required", true);
    private StringProperty errMessageRequired = new SimpleStringProperty(this, "errMessageRequired", "This field is mandatory.");

    public ComboBoxValidator() {
        super();
        required.addListener((val, o, n) -> revalidate());
    }

    @Override
    protected void controlChanged() {
        FxBindings.itemSelected(getTarget()).addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean o, Boolean n) {
                revalidate();
            }
        });
    }

    @Override
    protected String validate() {
        if (required.get() && getTarget().getSelectionModel().getSelectedItem() == null) {
            return errMessageRequired.get();
        } else {
            return null;
        }
    }

    public boolean getRequired() {
        return required.get();
    }

    public BooleanProperty requiredProperty() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required.set(required);
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
