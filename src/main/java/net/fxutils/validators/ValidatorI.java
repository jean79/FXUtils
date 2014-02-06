package net.fxutils.validators;

import javafx.beans.property.BooleanProperty;

/**
 * Should be implemented by all validators.
 * It is consumed by Validators class
 * @see Validators
 */
public interface ValidatorI {

    /**
     *
     * @return a BooleanProperty, property is set to true if control content is valid.
     */
    BooleanProperty validProperty();

}
