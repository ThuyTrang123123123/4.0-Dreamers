module core {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires javafx.media;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires jdk.httpserver;

    // Cho phép JavaFX truy cập các class controller
    opens core to javafx.fxml;
    //opens ui to javafx.fxml;
    //opens entities to javafx.fxml;

    // Cho phép các package được sử dụng từ bên ngoài module
    exports core;


exports data;
exports engine;

exports entities;
exports entities.bricks;
exports entities.powerups;

exports net;
exports systems;
exports util;
}
