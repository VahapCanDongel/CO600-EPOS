module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires google.maps.services;
    requires okhttp;

    opens org.example to javafx.fxml;
    exports org.example;
}