package net.fxutils.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class FXUtils {

    private static final String resourcePath = "%s.fxml";

    private static String getViewPath(Class clazz) {
        return String.format(resourcePath, clazz.getSimpleName());
    }

    public static Parent loadFXML(Object controller) {

        FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass().getResource(getViewPath(controller.getClass())));
        fxmlLoader.setControllerFactory((c) -> controller );
        fxmlLoader.setRoot(controller);

        try {
            fxmlLoader.load();
            return fxmlLoader.getRoot();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public static Stage createDialog(Node parentWindow, Parent node, String title) {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        Scene scene = new Scene(node);
        dialog.setTitle(title);
        dialog.setScene(scene);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parentWindow.getScene().getWindow());
        dialog.show();
        return dialog;
    }

}
