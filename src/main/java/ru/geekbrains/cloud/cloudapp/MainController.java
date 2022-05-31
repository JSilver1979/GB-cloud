package ru.geekbrains.cloud.cloudapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainController {

    @FXML
    VBox clientPanel;
    @FXML
    VBox serverPanel;

    ClientController clientPC;
    ServerController serverPC;

    public void exitApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void btnCopyAction(ActionEvent actionEvent) throws IOException {
        clientPC = (ClientController) clientPanel.getProperties().get("ctrl");
        serverPC = (ServerController) serverPanel.getProperties().get("ctrl");

        if(clientPC.getSelectedFilename() == null && serverPC.getSelectedFilename() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No files selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }

//        ClientController srcPC = null;
//        ClientController dstPC = null;


        if (clientPC.getSelectedFilename() != null) {
//            ClientController srcPC = clientPC;
//            ServerController dstPC = serverPC;
            serverPC.getNetwork().getOutputStream().writeUTF("#file#");
            String file = Paths.get(clientPC.getSelectedFilename()).toString();
            serverPC.getNetwork().getOutputStream().writeUTF(file);
            File toSend = Paths.get(clientPC.getCurrentPath()).resolve(clientPC.getSelectedFilename()).toFile();
            serverPC.getNetwork().getOutputStream().writeUTF(file);
            try (FileInputStream fis = new FileInputStream(toSend)) {
                while (fis.available() > 0) {
                    int read = fis.read(serverPC.getBuffer());
                    serverPC.getNetwork().getOutputStream().write(serverPC.getBuffer(), 0, read);
                }
            }
            serverPC.getNetwork().getOutputStream().flush();
        }

        if (serverPC.getSelectedFilename() != null) {
//            ServerController srcPC = serverPC;
//            ClientController dstPC = clientPC;
        }

//        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFilename());
//        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPC.getSelectedFilename().toString());

//        try {
//            Files.copy(srcPath, dstPath);
//            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
//        } catch (IOException e) {
//            Alert alert = new Alert(Alert.AlertType.WARNING, "File already exists", ButtonType.OK);
//            alert.showAndWait();
//        }
    }

    public void btnDeleteAction(ActionEvent actionEvent) {
//        clientPC = (ClientController) clientPanel.getProperties().get("ctrl");
//        serverPC = (ServerController) serverPanel.getProperties().get("ctrl");
//
//        if (clientPC.getSelectedFilename() != null) {
//
//            try {
//                Files.deleteIfExists(Paths.get(clientPC.getCurrentPath()).resolve(clientPC.getSelectedFilename().toString()));
//                clientPC.updateList(Paths.get(clientPC.getCurrentPath()));
//            } catch (IOException e) {
//                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete file", ButtonType.OK);
//                alert.showAndWait();
//            }
//        }
//
//        if (serverPC.getSelectedFilename() != null) {
//            try {
//                Files.delete(Paths.get(serverPC.getCurrentPath()).resolve(serverPC.getSelectedFilename().toString()));
//                serverPC.updateList(Paths.get(serverPC.getCurrentPath()));
//            } catch (IOException e) {
//                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete file", ButtonType.OK);
//                alert.showAndWait();
//            }
//        }
    }

    public void btnMoveAction(ActionEvent actionEvent) {
//        btnCopyAction(actionEvent);
//        btnDeleteAction(actionEvent);
    }
}