package ru.mai.activetest;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.mai.activetest.Models.Author;
import ru.mai.activetest.Models.AuthorRecord;
import ru.mai.activetest.Models.Record;
import ru.mai.activetest.Models.Title;

public class AddRecordController {

    public TextArea authorField;
    public TextArea titleField;
    public Button nextButton;
    public AnchorPane titleAuthorPane;
    public AnchorPane dataPane;
    Dao<Record, Integer> recordDao;
    Dao<Title, Integer> titleDao;
    Dao<Author, Integer> authorDao;
    Dao<AuthorRecord, Integer> authorRecordDao;
    private MainWindowController parentController;

    @FXML
    public Label errorLabel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField contentTypeBox;

    @FXML
    private TextField dateBox;

    @FXML
    private TextField editionBox;

    @FXML
    private TextField editionDataBox;

    @FXML
    private TextField identificatorBox;

    @FXML
    private TextField keyTitleBox;

    @FXML
    private TextField meanBox;

    @FXML
    private TextField noteBox;

    @FXML
    private TextField pagesBox;

    @FXML
    private TextField placeBox;

    @FXML
    private TextField publisherBox;

    @FXML
    private TextField serialMarkBox;

    @FXML
    private TextField sizeBox;

    @FXML
    private Button submitButton;

    @FXML
    private TextField titleDataBox;

    @FXML
    private TextField touchDateBox;

    @FXML
    private TextField urlBox;

    @FXML
    void initialize() {
        //TODO
    }

    @FXML
    public void nextButtonClick(ActionEvent actionEvent) {
        titleAuthorPane.setVisible(false);
        dataPane.setVisible(true);
    }

    @FXML
    private void submitButtonClick () throws SQLException, IOException {
        if (titleField.getText().isEmpty() || authorField.getText().isEmpty()){
            errorLabel.setText("INCORRECT DATA!!!");
        }
        else {
            ConnectionSource connectionSource = dbConnect();
            Record record = new Record();
            //record.title_record = titileBox.getText();
            record.title_add_data = titleDataBox.getText();
            record.edition = editionBox.getText();
            record.edition_add_data = editionDataBox.getText();
            record.publication_place = placeBox.getText();
            record.publisher_name = publisherBox.getText();
            record.publication_date = dateBox.getText();
            record.size = sizeBox.getText();
            record.url = urlBox.getText();
            record.url_date = touchDateBox.getText();
            record.note = noteBox.getText();
            record.identificator = identificatorBox.getText();
            record.key_title = keyTitleBox.getText();
            record.content_type = contentTypeBox.getText();
            record.access_mean = meanBox.getText();
            record.content_page = pagesBox.getText();
            record.serial_note = serialMarkBox.getText();
            recordDao.create(record);
            Title title = new Title();
            Scanner sc = new Scanner(String.valueOf(titleField.getText()));
            while (sc.hasNext())
            {
                title.title= sc.nextLine();
                title.record = record;
                titleDao.create(title);
            }
            Author author = new Author();
            sc = new Scanner(String.valueOf(authorField.getText()));
            while (sc.hasNext())
            {
                author.author= sc.nextLine();
                authorDao.create(author);
                AuthorRecord authorRecord = new AuthorRecord();
                authorRecord.author = author;
                authorRecord.record = record;
                authorRecordDao.create(authorRecord);
            }
            connectionSource.close();
            parentController.fillTable();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }

    private ConnectionSource dbConnect() throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:identifier.sqlite");
        recordDao = DaoManager.createDao(connectionSource, Record.class);
        titleDao = DaoManager.createDao(connectionSource, Title.class);
        authorDao = DaoManager.createDao(connectionSource, Author.class);
        authorRecordDao = DaoManager.createDao(connectionSource, AuthorRecord.class);
        //TableUtils.createTable(connectionSource, Record.class);
        //TableUtils.createTable(connectionSource, Title.class);
        //TableUtils.createTable(connectionSource, Author.class);
        //TableUtils.createTable(connectionSource, AuthorRecord.class);
        return connectionSource;
    }

    protected void setParentController(MainWindowController controller)
    {
        this.parentController = controller;
    }
}
