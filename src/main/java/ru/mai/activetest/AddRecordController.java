package ru.mai.activetest;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
    private Button deleteAuthorButton;

    @FXML
    private Label engTitleLabel;

    @FXML
    private Button deleteTitleButton;

    @FXML
    private HBox typeBox;

    @FXML
    private HBox authorBox;

    @FXML
    private HBox titleBox;

    @FXML
    private HBox addDataBox;

    @FXML
    private HBox serialTitleBox;

    @FXML
    private HBox publisherBox;

    @FXML
    private HBox numberBox;

    @FXML
    private HBox cityBox;

    @FXML
    private HBox yearBox;

    @FXML
    private HBox pagesBox;

    @FXML
    private HBox urlBox;

    @FXML
    private HBox accessDateBox;

    @FXML
    private HBox contentBox;

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

    Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    void initialize() throws SQLException {
        for (Object box : titleAuthorPane.getChildren().toArray()) {
            ((HBox) box).managedProperty().bind(((HBox) box).visibleProperty());
            for (Object field : ((HBox) box).getChildren().toArray()) {
                if (field.getClass() == ComboBox.class && field != resourceTypeField) {
                    ((ComboBox) field).managedProperty().bind(((ComboBox) field).visibleProperty());
                }
                if (field.getClass() == TextField.class) {
                    ((TextField) field).managedProperty().bind(((TextField) field).visibleProperty());
                }
            }
        }
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
        for (int y = 2000; y<2022; y++) {
            years.add(String.valueOf(y));
        }
        yearField.setItems(years);
        new AutoCompleteComboBoxListener<>(yearField);

        ObservableList<String> serialTitles = FXCollections.observableArrayList();
        for (Record record : recordDao) {
            if (record.serial_note != null)
                serialTitles.add(record.serial_note);
        }
        serialTitleField.setItems(serialTitles);
        new AutoCompleteComboBoxListener<>(serialTitleField);
        typeChange();
    }

    Boolean inCheck ()
    {
        alert.setTitle("Ошибка ввода!");

        if (authorField4.isVisible())
        {
            if (authorField4.getValue()!=null && !authorField4.getValue().isEmpty()) {
                if (!authorField4.getValue().matches("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]+\\s?[a-zA-Zа-яА-Я]*") && !authorField4.getValue().matches("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]\\.\\s?[a-zA-Zа-яА-Я]?\\.?")) {
                    alert.setHeaderText("Некорректные данные первого автора!");
                    alert.setContentText("Допустимый формат (без кавычек): \\\"Фамилия Имя Отчество\\\" или \\\"Фамилия И. О.\\\"");
                    alert.showAndWait();
                    return false;
                }
            }
            else
            {
                alert.setHeaderText("Некорректные данные первого автора!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            }
        }

        if (authorField3.isVisible())
        {
            if (authorField3.getValue()!=null && !authorField3.getValue().isEmpty()) {
                if (!authorField3.getValue().matches("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]+\\s?[a-zA-Zа-яА-Я]*") && !authorField3.getValue().matches("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]\\.\\s?[a-zA-Zа-яА-Я]?\\.?")) {
                    alert.setHeaderText("Некорректные данные первого автора!");
                    alert.setContentText("Допустимый формат (без кавычек): \\\"Фамилия Имя Отчество\\\" или \\\"Фамилия И. О.\\\"");
                    alert.showAndWait();
                    return false;
                }
            }
            else
            {
                alert.setHeaderText("Некорректные данные первого автора!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            }
        }

        if (authorField2.isVisible())
        {
            if (authorField2.getValue()!=null && !authorField2.getValue().isEmpty()) {
                if (!authorField2.getValue().matches("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]+\\s?[a-zA-Zа-яА-Я]*") && !authorField2.getValue().matches("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]\\.\\s?[a-zA-Zа-яА-Я]?\\.?")) {
                    alert.setHeaderText("Некорректные данные первого автора!");
                    alert.setContentText("Допустимый формат (без кавычек): \\\"Фамилия Имя Отчество\\\" или \\\"Фамилия И. О.\\\"");
                    alert.showAndWait();
                    return false;
                }
            }
            else
            {
                alert.setHeaderText("Некорректные данные первого автора!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            }
        }

        if (authorField1.isVisible())
        {
            if (authorField1.getValue()!=null && !authorField1.getValue().isEmpty()) {
                if (!authorField1.getValue().matches("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]+\\s?[a-zA-Zа-яА-Я]*") && !authorField1.getValue().matches("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]\\.\\s?[a-zA-Zа-яА-Я]?\\.?")) {
                    alert.setHeaderText("Некорректные данные первого автора!");
                    alert.setContentText("Допустимый формат (без кавычек): \\\"Фамилия Имя Отчество\\\" или \\\"Фамилия И. О.\\\"");
                    alert.showAndWait();
                    return false;
                }
            }
            else
            {
                alert.setHeaderText("Некорректные данные первого автора!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            }
        }

        if ((addDataField.getText() == null || addDataField.getText().isEmpty()) && Objects.equals(resourceTypeField.getValue(), "Автореферат/диссертация"))
        {
            alert.setHeaderText("Некорректные данные поля \\\"На соискание\\\"!");
            alert.setContentText("Поле не может быть пустым!");
            alert.showAndWait();
            return false;
        }

        if ((serialTitleField.getValue() == null || serialTitleField.getValue().isEmpty()) && Objects.equals(resourceTypeField.getValue(), "Статья"))
        {
            alert.setHeaderText("Некорректное название сборника/журнала!");
            alert.setContentText("Поле не может быть пустым!");
            alert.showAndWait();
            return false;
        }

        if (Objects.equals(resourceTypeField.getValue(), "Статья"))
        {
            if (numberFieldJournal.getText() == null || numberFieldJournal.getText().isEmpty())
            {
                alert.setHeaderText("Некорректный номер журнала!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            }
            else if (!numberFieldJournal.getText().matches("\\d+"))
            {
                alert.setHeaderText("Некорректный номер журнала!");
                alert.setContentText("Введите только номер цифрами.");
                alert.showAndWait();
                return false;
            }
        }

        if (Objects.equals(resourceTypeField.getValue(), "Автореферат/диссертация"))
        {
            if (publisherField.getValue() == null || publisherField.getValue().isEmpty())
            {
                alert.setHeaderText("Некорректный ввод ВУЗа!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            }

            if (numberFieldJournal.getText() == null || numberFieldJournal.getText().isEmpty())
            {
                alert.setHeaderText("Некорректный номер направления!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            } else if (!numberFieldJournal.getText().matches("\\d+\\.\\d+\\.\\d+"))
            {
                alert.setHeaderText("Некорректный номер направления!");
                alert.setContentText("Допустимый формат: ччч.чччч.ччч");
                alert.showAndWait();
                return false;
            }
            if (dissSpecField.getText() == null || dissSpecField.getText().isEmpty())
            {
                alert.setHeaderText("Некорректное название специализации!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            }
        }

        if (Objects.equals(resourceTypeField.getValue(), "Автореферат/диссертация") || Objects.equals(resourceTypeField.getValue(), "Статья"))
        {
            if (yearField.getValue() == null || yearField.getValue().isEmpty())
            {
                alert.setHeaderText("Некорректный год публикации!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            }
            else if (!yearField.getValue().matches("\\d{4}"))
            {
                alert.setHeaderText("Некорректный номер направления!");
                alert.setContentText("Допустимый формат: гггг");
                alert.showAndWait();
                return false;
            }
            if (cityField.getText() == null || cityField.getText().isEmpty())
            {
                alert.setHeaderText("Некорректный ввод места публикации!");
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    @FXML
    void submitButtonClick(ActionEvent event)   throws SQLException, IOException {
        if (!inCheck())
            return;
        Record record = new Record();
        record.publicationType = publicationTypeDao.queryForId(publicationTypeIndex);
        record.title_add_data = addDataField.getText();
        record.edition = numberField.getText();
        if (numberFieldJournal.isVisible())
            record.edition_add_data = numberFieldJournal.getText();
        if (dissSpecField.isVisible())
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
        deleteTitleButton.setVisible(true);
        titleField2.setVisible(true);
    }

    public void deleteTitleClick(ActionEvent actionEvent) {
        deleteTitleButton.setVisible(false);
        titleField2.setVisible(false);
        titleField2.setText(null);
    }

    public void addAuthorButtonClick(ActionEvent actionEvent)
    {
        deleteAuthorButton.setVisible(true);
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

    public void deleteAuthorClick(ActionEvent actionEvent) {
        if (authorField4.isVisible())
        {
            authorField4.setVisible(false);
            authorField4.setValue(null);
        }
        else
        {
            if (authorField3.isVisible())
            {
                authorField3.setVisible(false);
                authorField3.setValue(null);
            }
            else
            {
                authorField2.setVisible(false);
                authorField2.setValue(null);
                deleteAuthorButton.setVisible(false);
            }
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

    public void typeChange()
    {
        for (Object box : titleAuthorPane.getChildren().toArray()) {
            for (Object field : ((HBox) box).getChildren().toArray()) {
                if (field.getClass() == ComboBox.class && field != resourceTypeField) {
                    ((ComboBox) field).setValue(null);
                }
                if (field.getClass() == TextField.class) {
                    ((TextField) field).setText(null);
                }
            }
        }
        switch (resourceTypeField.getValue()) {
            case  ("Книга"):
                fromSiteCheck.setVisible (false);
                isElectronicCheck.setVisible(true);
                submitButton.setVisible(true);

                authorBox.setVisible (true);
                addAuthorButton.setVisible(true);
                authorLabel.setVisible (true);
                authorField1.setVisible (true);
                authorField2.setVisible (false);
                authorField3.setVisible (false);
                authorField4.setVisible (false);

                titleBox.setVisible (true);
                titleLabel.setVisible (true);
                titleField1.setVisible (true);
                titleField2.setVisible (false);

                addDataBox.setVisible (true);
                addDataLabel.setVisible (true);
                addDataLabel.setText("Доп. сведения");
                addDataField.setVisible (true);

                serialTitleBox.setVisible (false);
                serialTitleLabel.setVisible (false);
                serialTitleField.setVisible (false);

                publisherBox.setVisible (true);
                publisherLabel.setVisible (true);
                publisherLabel.setText("Издательство");
                publisherField.setVisible (true);

                numberBox.setVisible (true);
                editionLabel.setVisible (true);
                editionLabel.setText("Издание");
                numberField.setVisible (true);
                numberFieldJournal.setVisible (false);
                dissSpecField.setVisible (false);

                cityBox.setVisible (false);
                cityLabel.setVisible (false);
                cityField.setVisible (false);

                yearBox.setVisible (false);
                yearLabel.setVisible (false);
                yearField.setVisible (false);

                pagesBox.setVisible (true);
                pagesLabel.setVisible (true);
                pagesField.setVisible (true);

                urlBox.setVisible (false);
                urlLabel.setVisible (false);
                urlField.setVisible (false);

                accessDateBox.setVisible (false);
                urlDateLabel.setVisible (false);
                urlDateField.setVisible (false);

                contentBox.setVisible (false);
                contentPagesLabel.setVisible (false);
                contentPagesField.setVisible (false);

                if (isElectronicCheck.isSelected())
                {
                    isElectronicCheck.setSelected(false);
                }
                publicationTypeIndex = 1;
                break;

            case ("Статья"):
                fromSiteCheck.setVisible (true);
                isElectronicCheck.setVisible(true);
                submitButton.setVisible(true);

                authorBox.setVisible (true);
                addAuthorButton.setVisible(true);
                authorLabel.setVisible (true);
                authorField1.setVisible (true);
                authorField2.setVisible (false);
                authorField3.setVisible (false);
                authorField4.setVisible (false);

                titleBox.setVisible (true);
                titleLabel.setVisible (true);
                titleField1.setVisible (true);
                titleField2.setVisible (false);

                addDataBox.setVisible (true);
                addDataLabel.setVisible (true);
                addDataLabel.setText("Доп. сведения");
                addDataField.setVisible (true);

                serialTitleBox.setVisible (true);
                serialTitleLabel.setVisible (true);
                serialTitleField.setVisible (true);

                publisherBox.setVisible (false);
                publisherLabel.setVisible (false);
                publisherField.setVisible (false);

                numberBox.setVisible (true);
                editionLabel.setVisible (true);
                editionLabel.setText("Номер");
                numberField.setVisible (false);
                numberFieldJournal.setVisible (true);
                dissSpecField.setVisible (false);

                cityBox.setVisible (true);
                cityLabel.setVisible (true);
                cityField.setVisible (true);

                yearBox.setVisible (true);
                yearLabel.setVisible (true);
                yearField.setVisible (true);

                pagesBox.setVisible (true);
                pagesLabel.setVisible (true);
                pagesField.setVisible (true);

                urlBox.setVisible (false);
                urlLabel.setVisible (false);
                urlField.setVisible (false);

                accessDateBox.setVisible (false);
                urlDateLabel.setVisible (false);
                urlDateField.setVisible (false);

                contentBox.setVisible (false);
                contentPagesLabel.setVisible (false);
                contentPagesField.setVisible (false);

                if (isElectronicCheck.isSelected())
                {
                    isElectronicCheck.setSelected(false);
                }
                publicationTypeIndex = 2;
                break;

            case ("URL"):
                fromSiteCheck.setVisible (false);
                isElectronicCheck.setVisible(false);
                isElectronicCheck.setSelected(true);
                submitButton.setVisible(true);

                authorBox.setVisible (false);
                authorLabel.setVisible (false);
                authorField1.setVisible (false);
                authorField2.setVisible (false);
                authorField3.setVisible (false);
                authorField4.setVisible (false);

                titleBox.setVisible (true);
                titleLabel.setVisible (true);
                titleField1.setVisible (true);
                titleField2.setVisible (false);

                addDataBox.setVisible (true);
                addDataLabel.setVisible (true);
                addDataField.setVisible (true);

                serialTitleBox.setVisible (false);
                serialTitleLabel.setVisible (false);
                serialTitleField.setVisible (false);

                publisherBox.setVisible (false);
                publisherLabel.setVisible (false);
                publisherField.setVisible (false);

                numberBox.setVisible (false);
                editionLabel.setVisible (false);
                numberField.setVisible (false);
                numberFieldJournal.setVisible (false);
                dissSpecField.setVisible (false);

                cityBox.setVisible (false);
                cityLabel.setVisible (false);
                cityField.setVisible (false);

                yearBox.setVisible (false);
                yearLabel.setVisible (false);
                yearField.setVisible (false);

                pagesBox.setVisible (false);
                pagesLabel.setVisible (false);
                pagesField.setVisible (false);

                urlBox.setVisible (true);
                urlLabel.setVisible (true);
                urlField.setVisible (true);

                accessDateBox.setVisible (true);
                urlDateLabel.setVisible (true);
                urlDateField.setVisible (true);

                contentBox.setVisible (false);
                contentPagesLabel.setVisible (false);
                contentPagesField.setVisible (false);

                publicationTypeIndex = 3;
                break;

            case ("Автореферат/диссертация"):
                fromSiteCheck.setVisible (false);
                isElectronicCheck.setVisible(false);
                submitButton.setVisible(true);

                authorBox.setVisible (true);
                addAuthorButton.setVisible(false);
                authorLabel.setVisible (true);
                authorField1.setVisible (true);
                authorField2.setVisible (false);
                authorField3.setVisible (false);
                authorField4.setVisible (false);

                titleBox.setVisible (true);
                titleLabel.setVisible (true);
                titleField1.setVisible (true);
                titleField2.setVisible (false);

                addDataBox.setVisible (true);
                addDataLabel.setVisible (true);
                addDataLabel.setText("На соискание");
                addDataField.setVisible (true);

                serialTitleBox.setVisible (false);
                serialTitleLabel.setVisible (false);
                serialTitleField.setVisible (false);

                publisherBox.setVisible (true);
                publisherLabel.setVisible (true);
                publisherLabel.setText("ВУЗ");
                publisherField.setVisible (true);

                numberBox.setVisible (true);
                editionLabel.setVisible (true);
                editionLabel.setText("Специальность");
                numberField.setVisible (false);
                numberFieldJournal.setVisible (true);
                dissSpecField.setVisible (true);

                cityBox.setVisible (true);
                cityLabel.setVisible (true);
                cityField.setVisible (true);

                yearBox.setVisible (true);
                yearLabel.setVisible (true);
                yearField.setVisible (true);

                pagesBox.setVisible (true);
                pagesLabel.setVisible (true);
                pagesField.setVisible (true);

                urlBox.setVisible (false);
                urlLabel.setVisible (false);
                urlField.setVisible (false);

                accessDateBox.setVisible (false);
                urlDateLabel.setVisible (false);
                urlDateField.setVisible (false);

                contentBox.setVisible (true);
                contentPagesLabel.setVisible (true);
                contentPagesField.setVisible (true);

                publicationTypeIndex = 4;
                break;
        }
        Integer c = 0;
    }
}
