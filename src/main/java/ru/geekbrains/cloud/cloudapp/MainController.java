package ru.geekbrains.cloud.cloudapp;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    @FXML
    TableView<FileInfo> filesTable;

    public void exitApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(fileObject -> new SimpleStringProperty(fileObject.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Наименование");
        fileNameColumn.setCellValueFactory(fileObject -> new SimpleStringProperty(fileObject.getValue().getFileName()));
        fileNameColumn.setPrefWidth(200);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(fileObject -> new SimpleObjectProperty<>(fileObject.getValue().getFileSize()));
        fileSizeColumn.setPrefWidth(100);
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long aLong, boolean empty) {
                    super.updateItem(aLong, empty);
                    if (aLong == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", aLong);
                        if (aLong == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });

        DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        TableColumn<FileInfo, String> fileDateModColumn = new TableColumn<>("Дата изменения");
        fileDateModColumn.setCellValueFactory(filObject -> new SimpleStringProperty(filObject.getValue().getFileModifiedAt().format(dtf)));
        fileDateModColumn.setPrefWidth(150);


        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn,  fileSizeColumn, fileDateModColumn);

        updateList(Paths.get("."));
        filesTable.getSortOrder().add(fileTypeColumn);
    }

    public void updateList(Path path) {
        try {
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot create list of files", ButtonType.OK);
            alert.showAndWait();
        }
    }
}