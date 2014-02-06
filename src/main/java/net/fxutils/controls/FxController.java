package net.fxutils.controls;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class FxController implements Initializable {

    private final String resourcePath = "%s.fxml";

    private Node view;

    public FxController() {
        getView();
    }

    private String getViewPath() {
        return String.format(resourcePath, this.getClass().getSimpleName());
    }

    protected URL getViewURL() {
        return this.getClass().getResource(this.getViewPath());
    }

    public final synchronized Node getView() {
        if (view == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getViewURL());
                fxmlLoader.setControllerFactory((c) -> FxController.this );
                fxmlLoader.load();
                view = fxmlLoader.getRoot();
            } catch (IOException e) {
                throw new RuntimeException("Problem intializing.", e);
            }
        }
        return view;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onConstruct();
    }

    protected void onConstruct() { }
}
