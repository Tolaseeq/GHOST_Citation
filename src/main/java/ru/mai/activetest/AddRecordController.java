package ru.mai.activetest;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.mai.activetest.Models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AddRecordController {

    Dao<Record, Integer> recordDao;
    Dao<Title, Integer> titleDao;
    Dao<Author, Integer> authorDao;
    Dao<AuthorRecord, Integer> authorRecordDao;
    Dao<PublicationType, Integer> publicationTypeDao;
    private MainWindowController parentController;
    ConnectionSource connectionSource;
    int publicationTypeIndex = 1;

    @FXML
    private Label serialTitleLabel;

    @FXML
    private TextField dissSpecField;

    @FXML
    private CheckBox fromSiteCheck;

    @FXML
    private TextField contentPagesField;

    @FXML
    private Label contentPagesLabel;

    @FXML
    private TextField numberFieldJournal;

    @FXML
    private TextField addDataField;

    @FXML
    private Label addDataLabel;

    @FXML
    private TextField cityField;

    @FXML
    private Label cityLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label editionLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private Label pagesLabel;

    @FXML
    private Label urlLabel;

    @FXML
    private Label urlDateLabel;

    @FXML
    private Button addAuthorButton;

    @FXML
    private Button addTitleButton;

    @FXML
    private ComboBox<String> authorField1;

    @FXML
    private ComboBox<String> authorField2;

    @FXML
    private ComboBox<String> authorField3;

    @FXML
    private ComboBox<String> authorField4;

    @FXML
    private CheckBox isElectronicCheck;

    @FXML
    private Button submitButton;

    @FXML
    private TextField numberField;

    @FXML
    private TextField pagesField;

    @FXML
    private ComboBox<String> publisherField;

    @FXML
    private ComboBox<String> resourceTypeField;

    @FXML
    private ComboBox<String> serialTitleField;

    @FXML
    private VBox titleAuthorPane;

    @FXML
    private TextField titleField1;

    @FXML
    private TextField titleField2;

    @FXML
    private TextField urlDateField;

    @FXML
    private TextField urlField;

    @FXML
    private ComboBox<String> yearField;

    @FXML
    void initialize() throws SQLException {
        dbConnect();
        ObservableList<String> typeList = FXCollections.observableArrayList();
        for (PublicationType type : publicationTypeDao) {
            typeList.add(type.getPublication_type());
        }
        resourceTypeField.setItems(typeList);
        resourceTypeField.setValue(typeList.get(0));

        ObservableList<String> authorList = FXCollections.observableArrayList();
        for (Author author : authorDao) {
            authorList.add(author.getAuthor());
        }
        authorField1.setItems(authorList);
        new AutoCompleteComboBoxListener<>(authorField1);
        authorField2.setItems(authorList);
        new AutoCompleteComboBoxListener<>(authorField2);
        authorField3.setItems(authorList);
        new AutoCompleteComboBoxListener<>(authorField3);
        authorField4.setItems(authorList);
        new AutoCompleteComboBoxListener<>(authorField4);

        ObservableList<String> publisherList = FXCollections.observableArrayList();
        for (Record record : recordDao) {
            publisherList.add(record.getPublisher_name());
        }
        publisherField.setItems(publisherList);
        new AutoCompleteComboBoxListener<>(publisherField);

        ObservableList<String> years = FXCollections.observableArrayList();
        for (int y = 1600; y<2022; y++) {
            years.add(String.valueOf(y));
        }
        yearField.setItems(years);
        new AutoCompleteComboBoxListener<>(yearField);

        ObservableList<String> serialTitles = FXCollections.observableArrayList();
        for (Record record : recordDao) {
            serialTitles.add(record.serial_note);
        }
        serialTitleField.setItems(serialTitles);
        new AutoCompleteComboBoxListener<>(serialTitleField);
    }
    @FXML
    void submitButtonClick(ActionEvent event) throws SQLException, IOException {
        Record record = new Record();
        record.publicationType = publicationTypeDao.queryForId(publicationTypeIndex);
        record.title_add_data = addDataField.getText();
        record.edition = numberField.getText();
        if (Objects.equals(publisherField.getValue(), "Статья"))
            record.edition_add_data = numberFieldJournal.getText();
        else
            record.edition_add_data = dissSpecField.getText();
        record.content_page = contentPagesField.getText();
        record.publication_place = cityField.getText();
        if (publisherField.getValue() == null)
            record.publisher_name = "[б. и.]";
        else
            record.publisher_name = publisherField.getValue();
        record.publication_date = yearField.getValue();
        record.size = pagesField.getText();
        record.url = urlField.getText();
        record.url_date = urlDateField.getText();
        record.serial_note = serialTitleField.getValue();
        if (isElectronicCheck.isSelected())
            record.content_type = "электронный";
        else
            record.content_type = "непосредственный";
        recordDao.create(record);
        if (authorField1.isVisible() && !authorField1.getValue().isEmpty()) {
            Author author = createAuthor(authorField1.getValue());
            AuthorRecord authorRecord = new AuthorRecord();
            authorRecord.record = record;
            authorRecord.author = author;
            authorRecordDao.create(authorRecord);

            if (authorField2.isVisible() && !authorField2.getValue().isEmpty()) {
                author = createAuthor(authorField2.getValue());
                authorRecord.record = record;
                authorRecord.author = author;
                authorRecordDao.create(authorRecord);

                if (authorField3.isVisible() && !authorField3.getValue().isEmpty()) {
                    author = createAuthor(authorField3.getValue());
                    authorRecord.record = record;
                    authorRecord.author = author;
                    authorRecordDao.create(authorRecord);

                    if (authorField4.isVisible() && !authorField4.getValue().isEmpty()) {
                        author = createAuthor(authorField4.getValue());
                        authorRecord.record = record;
                        authorRecord.author = author;
                        authorRecordDao.create(authorRecord);
                    }
                }
            }
        }

        Title title = new Title();
        title.title = titleField1.getText();
        title.record = record;
        titleDao.create(title);
        if (titleField2.isVisible() && !titleField2.getText().isEmpty())
        {
            title.title = titleField1.getText();
            title.record = record;
            titleDao.create(title);
        }

        connectionSource.close();
        parentController.fillTable();
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
        /*int recordIndex = 0;
        for (Record iterator : recordDao) {
            recordIndex = iterator.getRecord_id();
        }
        Author author = new Author();
        author.author = authorField1.getValue();
        int authorIndex = 0;
        for (Author iterator : authorDao) {
            authorIndex = iterator.getAuthor_id();
        }
        AuthorRecord authorRecord = new AuthorRecord();
        authorRecord.record = */
    }

    Author createAuthor(String authorString) throws SQLException {
        Author _author = new Author();
        if (authorString == null)
        {
            return _author;
        }
        if (!authorDao.queryForEq("author", authorString).isEmpty())
        {
            _author = authorDao.queryForEq("author", authorString).get(0);
        }
        else
        {
            _author.author = authorString;
            authorDao.create(_author);
        }
        return _author;
    }

    private void dbConnect() throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:sqlite:identifier.sqlite");
        recordDao = DaoManager.createDao(connectionSource, Record.class);
        titleDao = DaoManager.createDao(connectionSource, Title.class);
        authorDao = DaoManager.createDao(connectionSource, Author.class);
        authorRecordDao = DaoManager.createDao(connectionSource, AuthorRecord.class);
        publicationTypeDao = DaoManager.createDao(connectionSource, PublicationType.class);
        //TableUtils.createTable(connectionSource, Record.class);
        //TableUtils.createTable(connectionSource, Title.class);
        //TableUtils.createTable(connectionSource, Author.class);
        //TableUtils.createTable(connectionSource, AuthorRecord.class);
    }

    protected void setParentController(MainWindowController controller)
    {
        this.parentController = controller;
    }

    public void addTitleButtonClick(ActionEvent actionEvent)
    {
        titleField2.setVisible(true);
    }

    public void addAuthorButtonClick(ActionEvent actionEvent)
    {
        if (authorField2.isVisible())
        {
            if (authorField3.isVisible())
            {
                authorField4.setVisible(true);
            }
            else
            {
                authorField3.setVisible(true);
            }
        }
        else
        {
            authorField2.setVisible(true);
        }
    }

    public void electronicCheck(ActionEvent actionEvent) {
        if (!isElectronicCheck.isSelected())
        {
            urlField.setVisible(false);
            urlLabel.setVisible(false);
            urlDateField.setVisible(false);
            urlDateLabel.setVisible(false);
            return;
        }
        urlField.setVisible(true);
        urlLabel.setVisible(true);
        urlDateField.setVisible(true);
        urlDateLabel.setVisible(true);
    }

    public void typeChange(ActionEvent actionEvent)
    {
        for (Object field : titleAuthorPane.getChildren().toArray()) {
            if (field.getClass() == ComboBox.class && field != resourceTypeField)
            {
                ((ComboBox) field).setValue(null);
                ((ComboBox) field).managedProperty().bind(((ComboBox) field).visibleProperty());
            }
            if (field.getClass() == TextField.class)
            {
                ((TextField) field).setText(null);
                ((TextField) field).managedProperty().bind(((TextField) field).visibleProperty());
            }
            if (field.getClass() == Label.class)
            {
                ((Label) field).managedProperty().bind(((Label) field).visibleProperty());
            }
        }
        switch (resourceTypeField.getValue()) {
            case  ("Книга"):
                fromSiteCheck.setVisible (false);
                addAuthorButton.setVisible(true);
                addTitleButton.setVisible(true);
                authorField1.setVisible(true);
                authorField2.setVisible(false);
                authorField3.setVisible(false);
                authorField4.setVisible(false);
                authorLabel.setVisible(true);
                editionLabel.setText("Издание");
                editionLabel.setVisible(true);
                isElectronicCheck.setVisible(true);
                submitButton.setVisible(true);
                numberField.setVisible(true);
                numberFieldJournal.setVisible(false);
                pagesField.setVisible(true);
                pagesLabel.setVisible(true);
                publisherField.setVisible(true);
                publisherLabel.setVisible(true);
                publisherLabel.setText("Издательство");
                serialTitleField.setVisible(false);
                serialTitleLabel.setVisible(false);
                titleField1.setVisible(true);
                titleField2.setVisible(false);
                titleLabel.setVisible(true);
                urlDateField.setVisible(false);
                urlDateLabel.setVisible(false);
                urlField.setVisible(false);
                urlLabel.setVisible(false);
                yearField.setVisible(true);
                yearLabel.setVisible(true);
                cityLabel.setVisible(true);
                cityField.setVisible(true);
                addDataLabel.setText("Доп. сведения");
                addDataField.setVisible(true);
                addDataLabel.setVisible(true);
                contentPagesField.setVisible(false);
                contentPagesLabel.setVisible(false);
                dissSpecField.setVisible(false);
                if (isElectronicCheck.isSelected())
                {
                    isElectronicCheck.setSelected(false);
                }
                publicationTypeIndex = 1;
                break;

            case ("Статья"):
                fromSiteCheck.setVisible (true);
                addAuthorButton.setVisible(true);
                addTitleButton.setVisible(true);
                authorField1.setVisible(true);
                authorField2.setVisible(false);
                authorField3.setVisible(false);
                authorField4.setVisible(false);
                authorLabel.setVisible(true);
                editionLabel.setText("Номер");
                editionLabel.setVisible(true);
                isElectronicCheck.setVisible(true);
                submitButton.setVisible(true);
                numberField.setVisible(false);
                numberFieldJournal.setVisible(true);
                pagesField.setVisible(true);
                pagesLabel.setVisible(true);
                publisherField.setVisible(false);
                publisherLabel.setVisible(false);
                serialTitleField.setVisible(true);
                serialTitleLabel.setVisible(true);
                titleField1.setVisible(true);
                titleField2.setVisible(false);
                titleLabel.setVisible(true);
                urlDateField.setVisible(false);
                urlDateLabel.setVisible(false);
                urlField.setVisible(false);
                urlLabel.setVisible(false);
                yearField.setVisible(true);
                yearLabel.setVisible(true);
                cityLabel.setVisible(true);
                cityField.setVisible(true);
                addDataLabel.setText("Доп. сведения");
                addDataField.setVisible(true);
                addDataLabel.setVisible(true);
                contentPagesField.setVisible(false);
                contentPagesLabel.setVisible(false);
                dissSpecField.setVisible(false);
                if (isElectronicCheck.isSelected())
                {
                    isElectronicCheck.setSelected(false);
                }
                publicationTypeIndex = 2;
                break;

            case ("URL"):
                isElectronicCheck.setSelected(true);
                isElectronicCheck.setVisible(false);
                fromSiteCheck.setVisible (false);
                addAuthorButton.setVisible(false);
                addTitleButton.setVisible(false);
                authorField1.setVisible(false);
                authorField2.setVisible(false);
                authorField3.setVisible(false);
                authorField4.setVisible(false);
                authorLabel.setVisible(false);
                editionLabel.setVisible(false);
                submitButton.setVisible(true);
                numberField.setVisible(false);
                numberFieldJournal.setVisible(false);
                pagesField.setVisible(false);
                pagesLabel.setVisible(false);
                publisherField.setVisible(false);
                publisherLabel.setVisible(false);
                serialTitleField.setVisible(false);
                serialTitleLabel.setVisible(false);
                titleField1.setVisible(true);
                titleField2.setVisible(false);
                titleLabel.setVisible(true);
                urlDateField.setVisible(true);
                urlDateLabel.setVisible(true);
                urlField.setVisible(true);
                urlLabel.setVisible(true);
                yearField.setVisible(false);
                yearLabel.setVisible(false);
                cityLabel.setVisible(false);
                cityField.setVisible(false);
                addDataField.setVisible(false);
                addDataLabel.setVisible(false);
                contentPagesField.setVisible(false);
                contentPagesLabel.setVisible(false);
                dissSpecField.setVisible(false);
                publicationTypeIndex = 3;
                break;

            case ("Автореферат/диссертация"):
                isElectronicCheck.setSelected(false);
                isElectronicCheck.setVisible(false);
                fromSiteCheck.setVisible (false);
                addAuthorButton.setVisible(false);
                addTitleButton.setVisible(false);
                authorField1.setVisible(true);
                authorField2.setVisible(false);
                authorField3.setVisible(false);
                authorField4.setVisible(false);
                authorLabel.setVisible(true);
                editionLabel.setVisible(true);
                editionLabel.setText("Специальность");
                submitButton.setVisible(true);
                numberField.setVisible(true);
                numberFieldJournal.setVisible(false);
                pagesField.setVisible(true);
                pagesLabel.setVisible(true);
                publisherField.setVisible(true);
                publisherLabel.setVisible(true);
                publisherLabel.setText("ВУЗ");
                serialTitleField.setVisible(false);
                serialTitleLabel.setVisible(false);
                titleField1.setVisible(true);
                titleField2.setVisible(false);
                titleLabel.setVisible(true);
                urlDateField.setVisible(false);
                urlDateLabel.setVisible(false);
                urlField.setVisible(false);
                urlLabel.setVisible(false);
                yearField.setVisible(true);
                yearLabel.setVisible(true);
                cityLabel.setVisible(true);
                cityField.setVisible(true);
                addDataLabel.setText("На соискание");
                addDataField.setVisible(true);
                addDataLabel.setVisible(true);
                contentPagesField.setVisible(true);
                contentPagesLabel.setVisible(true);
                dissSpecField.setVisible(true);
                publicationTypeIndex = 4;
                break;
        }

    }
}
