package ru.mai.activetest;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcDatabaseConnection;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.mai.activetest.Models.Record;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainWindow-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Reference Builder");
        stage.setScene(scene);
        stage.show();
        /*ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:identifier.sqlite");
        Dao<Record, Integer> recordDao =
                DaoManager.createDao(connectionSource, Record.class);
        Record record = new Record();
        record.title_record = "Преступление и наказание";
        record.title_add_data = "Роман";
        record.author_record = "Ф.М. Достоевский";
        record.edition = "5-е изд.";
        record.edition_add_data = "исправленное и дополненное";
        record.publication_place = "Петроград";
        record.publisher_name = "Империя";
        record.publication_date = "1902";
        record.size = "365 с.";
        record.url
        record.url_date
        record.note
        record.identificator = "ISBN 111 111 111";
        record.key_title;
        record.content_type;
        record.access_mean;
        record.content_page;
        record.serial_note;
        recordDao.create(record);
        connectionSource.close();*/
    }

    public static void main(String[] args) { launch(); }
}