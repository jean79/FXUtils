package net.fxutils.bindings;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class FxBindings {

    public static BooleanBinding itemsSelected(TableView tableView) {
        return Bindings.size(tableView.getSelectionModel().getSelectedItems()).isNotEqualTo(0);
    }

    public static BooleanBinding itemSelected(ComboBox comboBox) {
        return comboBox.getSelectionModel().selectedItemProperty().isNotNull();
    }

}
