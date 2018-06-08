package sample.view;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

final class CONNECTION {
    static String NOT_CONNECTED = "NOT CONNECTED";
    static String CONNECTED = "CONNECTED";
    static String CONNECTING = "CONNECTING";
    static Map<String, String> CONNECTION_STYLES;

    static {
        CONNECTION_STYLES = new HashMap<>();
        CONNECTION_STYLES.put(NOT_CONNECTED, "status-not-connected");
        CONNECTION_STYLES.put(CONNECTED, "status-connected");
        CONNECTION_STYLES.put(CONNECTING, "status-connecting");
    }
}


public class Controller {

    private static HttpURLConnection con;

    public Label statusLabel;

    StringProperty statusText = new SimpleStringProperty();



    @FXML
    private void initialize() {
        statusLabelBindings();
        statusText.setValue(CONNECTION.CONNECTING);
        try {
            Socket t = new Socket("127.0.0.1", 8080);
            DataInputStream dis = new DataInputStream(t.getInputStream());
            PrintStream ps = new PrintStream(t.getOutputStream());
            dis.close();
            ps.close();
            t.close();
            statusText.setValue(CONNECTION.CONNECTED);
        } catch (Exception e) {
            e.printStackTrace();
            statusText.setValue(CONNECTION.NOT_CONNECTED);
        }


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

    public void sendPostRequest(ActionEvent actionEvent) {
        try {
            sendPOST();
        } catch (IOException e) {
            e.printStackTrace();
            statusText.setValue(CONNECTION.NOT_CONNECTED);
        }
    }

    private static void sendPOST() throws IOException {

        try {
            URL url = new URL("http://localhost:8080/greeting/post");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");
            con.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            Greeting fag = new Greeting(4235,"Barnie");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            writer.write(gson.toJson(fag));
            writer.flush();


            String line;
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(con.getInputStream()));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            writer.close();
            reader.close();
        } finally {
            con.disconnect();
        }
    }
}
