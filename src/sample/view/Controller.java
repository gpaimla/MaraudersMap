package sample.view;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

final class CONNECTION {
    static String NOT_CONNECTED = "NOT CONNECTED";
    static String CONNECTED = "CONNECTED";
    static String CONNECTING = "CONNECTING";
    static Map<String, String> CONNECTION_STYLES;

    static {
        CONNECTION_STYLES = new HashMap<>();
        CONNECTION_STYLES.put("NOT CONNECTED", "status-not-connected");
        CONNECTION_STYLES.put("CONNECTED", "status-connected");
        CONNECTION_STYLES.put("CONNECTING", "status-connecting");
    }
}

public class Controller {

    public Label statusLabel;

    StringProperty statusText = new SimpleStringProperty();

    @FXML
    private void initialize() {
        statusLabelBindings();
        statusText.setValue(CONNECTION.CONNECTING);

    }

    void statusLabelBindings() {
        statusLabel.textProperty().bind(statusText);
        statusLabel.textProperty().addListener(event -> {
            if (event instanceof SimpleStringProperty) {
                //set style of the text based on what is currently displayed
                statusLabel.getStyleClass().setAll(CONNECTION.CONNECTION_STYLES.get(((SimpleStringProperty) event).getValue()));
            }
        });
    }

}
