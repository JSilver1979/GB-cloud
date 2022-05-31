package ru.geekbrains.cloud.cloudapp;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ServerController implements Initializable {

    private String serverHomeDir;

    private byte[] buffer;

    private Network network;

    @FXML
    ListView serverList;

    public Network getNetwork() {
        return network;
    }

    private void readLoop() {
        try {
            while (true) {
                String command = network.readString();
                if (command.equals("#list#")) {
                    Platform.runLater(() -> serverList.getItems().clear());
                    int length = network.readInt();
                    for (int i = 0; i < length; i++) {
                        String file = network.readString();
                        Platform.runLater(() -> serverList.getItems().add(file));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Connection lost");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            buffer = new byte[256];
            network = new Network(8189);
            Thread readThread = new Thread(this::readLoop);
            readThread.setDaemon(true);
            readThread.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String getSelectedFilename() {
        if (!serverList.isFocused()) {
            return null;
        }
        return serverList.getSelectionModel().getSelectedItem().toString();
    }

    public byte[] getBuffer() {
        return buffer;
    }
}
