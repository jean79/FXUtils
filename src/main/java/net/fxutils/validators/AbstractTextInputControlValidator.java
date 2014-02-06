package net.fxutils.validators;

import javafx.beans.property.*;
import javafx.scene.control.TextInputControl;

/**
 * This implementation listens on changes of text in TextInputControl.
 * Implementing classes must implement validateText method.
 *
 * @param <T>
 */
public abstract class AbstractTextInputControlValidator<T extends TextInputControl> extends AbstractControlValidator<T> {

    private BooleanProperty required = new SimpleBooleanProperty(this, "required", true);

    public AbstractTextInputControlValidator() {
        super();
        required.addListener((val, o, n) -> revalidate());
    }

    @Override
    protected final void controlChanged() {
        getTarget().textProperty().addListener((observableValue, s, s2) -> revalidate());
    }

    @Override
    protected final String validate() {
        String value = getTarget().textProperty().get();
        return validateText(value);
    }

    /**
     *
     * @param text text to be validated
     * @return null if text is ok or error message
     */
    protected abstract String validateText(String text);

    public final boolean getRequired() {
        return required.get();
    }

    public final BooleanProperty requiredProperty() {
        return required;
    }

    public final void setRequired(boolean required) {
        this.required.set(required);
    }

}
