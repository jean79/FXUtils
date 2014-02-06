package net.fxutils.validators;

import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;

@DefaultProperty("validators")
public class Validators {

    private ObservableList<ValidatorI> validators = FXCollections.observableList(new ArrayList<>());

    private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);

    public ObservableList<ValidatorI> getValidators() {
        return validators;
    }

    public Validators() {
        validators.addListener((ListChangeListener<ValidatorI>)this::listenOnChange);
    }

    private ChangeListener<Boolean> changeListener = (observableValue, aBoolean, aBoolean2) -> {
        boolean allValid = true;
        for (ValidatorI validator : validators) {
            allValid = allValid && validator.validProperty().get();
            if (!allValid) { break; }
        }
        valid.set(allValid);
    };

    private void listenOnChange(ListChangeListener.Change<? extends ValidatorI> change) {
        while (change.next()) {
            for (ValidatorI validator : change.getAddedSubList()) {
                validator.validProperty().addListener(changeListener);
            }
            for (ValidatorI validatorI : change.getRemoved()) {
                validatorI.validProperty().removeListener(changeListener);
            }
        }
    }

    public boolean getValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid.set(valid);
    }
}
