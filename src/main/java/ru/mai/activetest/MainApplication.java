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
import ru.mai.activetest.Models.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApplication extends Application {
    static ConnectionSource connectionSource;
    static Dao<Record, Integer> recordDao;
    static Dao<Title, Integer> titleDao;
    static Dao<Author, Integer> authorDao;
    static Dao<AuthorRecord, Integer> authorRecordDao;
    static Dao<PublicationType, Integer> publicationTypeDao;
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:sqlite:identifier.sqlite");
        recordDao = DaoManager.createDao(connectionSource, Record.class);
        titleDao = DaoManager.createDao(connectionSource, Title.class);
        authorDao = DaoManager.createDao(connectionSource, Author.class);
        authorRecordDao = DaoManager.createDao(connectionSource, AuthorRecord.class);
        publicationTypeDao = DaoManager.createDao(connectionSource, PublicationType.class);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainWindow-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("BibDesk");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}