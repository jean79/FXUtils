package net.fxutils.controls;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class FxDialog extends FxController {

    private Stage dialog;

    public final synchronized void showDialog(Node parentWindow) {
        if (dialog == null) {
            dialog = new Stage();
            dialog.initStyle(StageStyle.UTILITY);
            Scene scene = new Scene((Parent)this.getView());
            dialog.setTitle(getDialogTitle());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(parentWindow.getScene().getWindow());
            dialog.setScene(scene);
        }
        dialog.show();
    }

    protected abstract String getDialogTitle();

    public final synchronized void closeDialog() {
        dialog.hide();
    }

}
