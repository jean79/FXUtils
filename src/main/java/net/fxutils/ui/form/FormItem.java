package net.fxutils.ui.form;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.fxutils.controls.FXUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Simple component, basicaly it works like HBox where on the left is a form label
 */
@DefaultProperty(value = "content")
public class FormItem extends AnchorPane implements Initializable {

    private ObjectProperty<Node> content = new SimpleObjectProperty<>();

    private StringProperty flabel = new SimpleStringProperty();

    @FXML
    private Label labelField;

    @FXML
    private HBox cont;

    public FormItem() {
        FXUtils.loadFXML(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelField.textProperty().bind(flabel);
        content.addListener((observableValue, node, node2) -> {
            System.out.println("Changed node value.");
            if (cont.getChildren().size() > 1) {
                cont.getChildren().remove(1);
            }
            if (node2 != null) {
                cont.getChildren().add(node2);
                //cont.setMinWidth(100);
            }
        });
    }

    public Node getContent() {
        return content.get();
    }

    public ObjectProperty contentProperty() {
        return content;
    }

    public void setContent(Node content) {
        this.content.set(content);
    }

    public String getFlabel() {
        return flabel.get();
    }

    public StringProperty flabelProperty() {
        return flabel;
    }

    public void setFlabel(String flabel) {
        this.flabel.set(flabel);
    }

}
