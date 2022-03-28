module ru.mai.activetest {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    requires java.sql;
    requires ormlite.core;
    requires ormlite.jdbc;
    requires lombok;

    opens ru.mai.activetest to javafx.fxml;
    exports ru.mai.activetest;
    exports ru.mai.activetest.Models;
    opens ru.mai.activetest.Models to javafx.fxml;
}