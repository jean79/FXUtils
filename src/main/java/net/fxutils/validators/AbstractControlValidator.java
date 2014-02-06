package net.fxutils.validators;

import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Control;
import javafx.scene.control.PopupControl;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Abstract implementation of ValidatorI interface.
 * It is designed to be attached to a JavaFx javafx.scene.control.Control UI element.
 * It set "fxutil-validators-error" CSS on a control if content of control is not valid.
 * CSS class is only set after the first focus out to prevent highlighting control on screen intitalization.
 * On component is focused and is not valid (after first focus out) then a red box with error description is show.
 *
 * Classes extending this abstract parent must implement method 'controlChanged' to register change listeners
 * on an injected control. It is then their responsible to call 'revalidate()' method of the parent if content
 * changes. Also it must implement method 'validate' which should return null (no error) or error message.
 *
 * Extended classes must call constructor!
 *
 * Exposed bindable properties:
 * - target&lt;Control&gt;
 * - valid&lt;Boolean&gt;
 *
 * @param <T>
 */
public abstract class AbstractControlValidator<T extends Control> implements ValidatorI {

    public static final String ERROR_CLASS = "fxutil-validators-error";
    public static final int ARROW_SIZE = 6;

    private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);

    private StringProperty errorMessage = new SimpleStringProperty(this, "errorMessage", null);

    private ObjectProperty<T> target = new SimpleObjectProperty<>(true, "target", null);

    /**
     * if set to true then control is highlighted
     * it is set to true on first focus out
     */
    private boolean focusedOutAlready = false;

    private PopupControl popOver;

    /**
     * Constructor must be called!
     *
     * It:
     * 1. register listeners on target property.
     * 2. create error popup
     * 3. register on error property to populate error popup
     *
     */
    public AbstractControlValidator() {
        target.addListener((val, o, n) -> targetChanged(n));

        popOver = new PopupControl();
        popOver.setStyle("-fx-border-color: red");
        errorMessage.addListener((observableValue, o, n) -> {
            if (n != null) {
                Group root = new Group();
                root.getChildren().add(createCanvas(n));
                popOver.getScene().setRoot(root);
                positionChanged();
            }
        });
    }

    /**
     *
     * @param errorString An error to be displayed in popup
     * @return Constructs error popup
     */
    private Node createCanvas(String errorString) {

        int h = 24;

        final Text text = new Text(errorString);
        text.snapshot(null, null);
        int w = (int) text.getLayoutBounds().getWidth() + 12;

        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(new Color(1, 0, 0, 1));
        gc.setStroke(new Color(1, 0, 0, 1));
        gc.setLineWidth(1);

        gc.fillPolygon(
                new double[]{0, w / 2 - ARROW_SIZE, w / 2, w / 2 + ARROW_SIZE, w, w, 0},
                new double[]{ARROW_SIZE, ARROW_SIZE, 0, ARROW_SIZE, ARROW_SIZE, h, h},
                7
        );
        gc.strokePolygon(
                new double[]{0, w / 2 - ARROW_SIZE, w / 2, w / 2 + ARROW_SIZE, w, w, 0},
                new double[]{ARROW_SIZE, ARROW_SIZE, 0, ARROW_SIZE, ARROW_SIZE, h, h},
                7
        );
        gc.setFill(new Color(1, 1, 1, 1));
        gc.setStroke(new Color(1, 1, 1, 1));

        gc.setTextBaseline(VPos.TOP);
        gc.fillText(errorString, 6, 6);

        return canvas;
    }

    /**
     * Implement this method:
     * You should register to listen on content changes (if TextField or TextArea for example)
     */
    protected abstract void controlChanged();

    private void targetChanged(T control) {
        if (control != null) {
            focusedOutAlready = false;
            controlChanged();
            control.focusedProperty().addListener((observableValue, o, n) ->  changedFocus(n));
            control.sceneProperty().addListener((v, o, n) -> targetAddedToScene());
        }
        revalidate();
    }

    private void changedFocus(boolean focused) {
        if (!focused) {
            focusedOutAlready = true;
            revalidate();
        }
        showHide();
    }

    private void showHide() {
        if (!valid.get() && target.get().isFocused() && focusedOutAlready) {
            showErrPopup();
        } else {
            popOver.hide();
        }
    }

    private void targetAddedToScene() {
        if (target.get() != null && target.get().getScene() != null) {
            target.get().getScene().windowProperty().addListener((v, o, n) -> addedWindow());
            target.get().getScene().xProperty().addListener((v, o, n) -> positionChanged());
            target.get().getScene().yProperty().addListener((v, o, n) -> positionChanged());
        }
    }

    private void addedWindow() {
        if (target.get().getScene().getWindow() != null) {
            target.get().getScene().getWindow().xProperty().addListener((v, o, n) -> positionChanged());
            target.get().getScene().getWindow().yProperty().addListener((v, o, n) -> positionChanged());
        }
    }

    private void positionChanged() {
        if (target.get() != null && target.get().getScene() != null) {
            Point2D c = getCoordinates();
            popOver.setX(c.getX());
            popOver.setY(c.getY());
        }
    }

    private Point2D getCoordinates() {
        final Scene scene = target.get().getScene();
        final Point2D windowCoord = new Point2D(scene.getWindow().getX(), scene.getWindow().getY());
        final Point2D sceneCoord = new Point2D(scene.getX(), scene.getY());
        final Point2D nodeCoord = target.get().localToScene(target.get().getWidth()/2, target.get().getHeight());
        final double x = Math.round(windowCoord.getX() + sceneCoord.getX() + nodeCoord.getX() - target.get().getWidth()/2);
        final double y = Math.round(windowCoord.getY() + sceneCoord.getY() + nodeCoord.getY());
        return new Point2D(x, y);
    }

    protected abstract String validate();

    protected final void revalidate() {
        if (target == null) {
            valid.set(true);
        } else {
            errorMessage.set(validate());
            valid.set(errorMessage.get() == null);
            if (focusedOutAlready) {
                if (valid.get()) {
                    target.get().getStyleClass().remove(ERROR_CLASS);
                    if (popOver.isShowing()) {
                        popOver.hide();
                    }
                } else {
                    if (!target.get().getStyleClass().contains(ERROR_CLASS)) {
                        target.get().getStyleClass().add(ERROR_CLASS);
                    }
                    if (!popOver.isShowing()) {
                        showErrPopup();
                    }
                }
            }
        }
    }

    private void showErrPopup() {
        Point2D cord = getCoordinates();
        popOver.show(target.get(), cord.getX(), cord.getY());
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

    public T getTarget() {
        return target.get();
    }

    public ObjectProperty<T> targetProperty() {
        return target;
    }

    public void setTarget(T target) {
        this.target.set(target);
    }
}
