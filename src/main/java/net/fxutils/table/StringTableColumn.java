package net.fxutils.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class StringTableColumn<S, T> extends TableColumn<S, T> {

    private StringProperty property = new SimpleStringProperty(this, "property", "");

    public StringTableColumn() {
        property.addListener((observableValue, n, o) ->
            this.setCellValueFactory(new PropertyValueFactory<>(property.get())));
    }

    public String getProperty() {
        return property.get();
    }

    public StringProperty propertyProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property.set(property);
    }

}
