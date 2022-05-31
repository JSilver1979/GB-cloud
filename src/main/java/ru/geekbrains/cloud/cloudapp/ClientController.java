package ru.geekbrains.cloud.cloudapp;

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

public class ClientController implements Initializable {
    @FXML
    TableView<FileInfo> filesTable;

    @FXML
    ComboBox<String> disksBox;

    @FXML
    TextField pathField;

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
        disksBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    Path path = Paths.get(pathField.getText()).resolve(filesTable.getSelectionModel().getSelectedItem().getFileName());
                    if (Files.isDirectory(path)) {
                        updateList(path);
                    }
                }
            }
        });
    }

    public void updateList(Path path) {
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot create list of files", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void btnPathUp(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null) {
            updateList(upperPath);
        }
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    public String getSelectedFilename() {
        if (!filesTable.isFocused()) {
            return null;
        }
        return filesTable.getSelectionModel().getSelectedItem().getFileName();
    }

    public String getCurrentPath() {
        return pathField.getText();
    }
}
