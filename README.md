# Goal of project
To develop set of components for easier development of JavaFX 8 applications using FXML.
- Simplify constructions of FX Nodes in Spring framework (to support Spring DI)
- Validators (mimic Adobe Flex validators)

**This is just the prototype! Take inspiration. Code is not ready for production deployment yet!**
**Any suggestions and comments are WELCOME!**

Please write comments to: jan.kovar79@gmail.com.

# Components
## Validators
You can define validators directly in FXML page in <fx:define> block and enable/disable *Save* button based on the current validation state.

In css file please define following:
``` css
.fxutil-validators-error {
    -fx-background-color:
        #ff1a00,
        #FFFFFF;
}
```

``` xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.VBox?>
<?import net.fxutils.ui.form.FormItem?>
<?import net.fxutils.validators.*?>

<HBox xmlns:fx="http://javafx.com/fxml" xmlns="http://javafx.com/javafx/8"
      fx:controller="net.jankovar.pmanager.view.instrument.InstrumentEditor" stylesheets="/stylesheet.css"
      styleClass="appTab">
    <fx:define>
        <Validators fx:id="validators">
            <StringValidator required="true" target="${fieldUserName}"/>
            <ComboBoxValidator required="true" target="${fieldCountry}" />
            <NumberValidator required="true" target="${fieldWeight}" domain="REAL"/>
        </Validators>
    </fx:define>
    <children>
        <VBox styleClass="decentSpacing">
            <FormItem flabel="User Name">
                <TextField fx:id="fieldUserName" prefWidth="100"/>
            </FormItem>
            <FormItem flabel="Country">
                <ComboBox fx:id="fieldCountry" editable="true" />
            </FormItem>
            <FormItem flabel="Weight">
                <TextField fx:id="fieldWeight" prefWidth="100"/>
            </FormItem>
            <HBox styleClass="decentSpacing">
                <Button mnemonicParsing="false" fx:id="saveButton" text="Save" disable="${!(validators.valid)}"/>
                <Button mnemonicParsing="false" text="Cancel" onAction="#closeDialog"/>
            </HBox>
        </VBox>
    </children>
</HBox>
```

## FX Control parent
Example below is a Java part of the fxml above.
Notice we extend from FxDialog (which extends FxController).
@PostConstruct method is called when FXML is loaded and Spring bean initialized (dependencies injected)

File: UserEditor.fxml
``` java
package net.jankovar.pmanager.view.instrument;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.fxutils.controls.FxDialog;
import net.fxutils.utils.FxFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
@Scope("prototype")
public class UserEditor extends FxDialog {

    @FXML private ComboBox<Country> fieldCountry;
    @FXML private TextField fieldUsername;
    @FXML private TextField fieldWeight;
    @FXML private Button saveButton;

    @Autowired
    private CountryService countryService;

    private ItemUpdated<User> handler;

    @Override
    protected String getDialogTitle() {
        return "User Editor";
    }

    @PostConstruct
    public void init() {
        saveButton.setOnAction(this::save);
        
        //these are some helper classes to save us some code monkey work
        fieldCountry.setItems(FXCollections.observableList(countryService.getAll()));
        fieldCountry.setCellFactory(FxFactories.createPropertyCellFactory(Country::getTitle));
        fieldCountry.setConverter(FxFactories.createStringConverter(Country::getTitle, countryService.getAll()));
    }

    public void setUser(User user) {
        fieldUsername.setText(user.getName());
        fieldWeight.setText(String.valueOf(user.getWeight()));
        if (user.getCountry() != null) {
            fieldCountry.getSelectionModel().select(user.getCountry());
        }
    }

    public void save(ActionEvent actionEvent) {

        User user= new User ();
        user.setName(fieldBondName.getText());
        user.setWeight(new BigDecimal(fieldCoupon.getText()));
        user.setCountry(fieldCountry.getSelectionModel().getSelectedItem());
        handler.itemUpdated(user);
        closeDialog();
    }

    public void setHandler(ItemUpdated<User> handler) {
        this.handler = handler;
    }

}
```

## And how to use it in another View
``` java
UserEditor userEditor = beanFactory.getBean(UserEditor .class);
userEditor.setUser(new User());
userEditor.setHandler(this::itemCreated);
userEditor.showDialog(this.getView());

```