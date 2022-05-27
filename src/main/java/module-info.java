module ru.geekbrains.cloud.cloudapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.geekbrains.cloud.cloudapp to javafx.fxml;
    exports ru.geekbrains.cloud.cloudapp;
}