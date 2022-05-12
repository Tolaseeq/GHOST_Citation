package ru.mai.activetest;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.mai.activetest.Models.*;

public class MainWindowController implements Initializable {

    public Label searchErrorLabel;
    public Text test_text;
    public Button exportButton;
    Dao<Record, Integer> recordDao;
    Dao<Title, Integer> titleDao;
    Dao<Author, Integer> authorDao;
    Dao<AuthorRecord, Integer> authorRecordDao;
    Dao<PublicationType, Integer> publicationTypeDao;
    ObservableList<Record> records = FXCollections.observableArrayList();
    ConnectionSource connectionSource;

    Record choice;

    @FXML
    private ImageView imageView;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Record> mainTable;

    @FXML
    private TableColumn<Record, String> titleColumn;

    @FXML
    private TableColumn<Record, String> authorColumn;

    @FXML
    private TableColumn<Record, CheckBox> checkColumn;

    @FXML
    private TableColumn<Record, String> yearColumn;

    @FXML
    private Button searchButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField searchField;

    ArrayList<Record> exportList = new ArrayList<>();

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        try {
            dbConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            fillTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImage(new Image("file:4888526.jpg"));
        checkColumn.setCellValueFactory(arg0 -> {
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(false);
            checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
                if (!checkBox.isSelected())
                    exportList.remove(arg0.getValue());
                else
                    exportList.add(arg0.getValue());
            });
            return new SimpleObjectProperty<>(checkBox);
        });
        titleColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("mainTitle"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("authorsString"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("publication_date"));
    }

    @FXML
    public void clickItem(MouseEvent mouseEvent) throws IOException {
        mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (mainTable.getSelectionModel().getSelectedItem() != null) {
                    for (Record record : recordDao) {
                        if (record.record_id == mainTable.getSelectionModel().getSelectedItem().record_id)
                            choice = record;
                    }
                    try {
                        Stage stage = newWindow("AddRecord-view.fxml", "Редактирование");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    public void exportButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (exportList.isEmpty()) {
            alert.setTitle("Внимание!");
            alert.setHeaderText("Ошибка экспорта:");
            alert.setContentText("Ничего не выбрано! Выберите ресурсы для экспорта.");
            alert.showAndWait();
            return;
        }
        Stage stage = newWindow("SingleExport-view.fxml", "Экспорт");
    }

    @FXML
    private void addButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = newWindow("AddRecord-view.fxml", "Добавить новый ресурс");
        //stage.onCloseRequestProperty().set((WindowEvent event) -> fillTable());
        //Controller controller = stage.get
    }

    @FXML
    public void searchButtonClick(ActionEvent actionEvent) throws IOException { //поиск
        if (searchField.getText().isEmpty()) {
            fillTable();
        }
        String search = searchField.getText();
        records.clear();
        for (Record record : recordDao) {
            String buffer = " ";
            record.setMainTitle(record.getTitles().iterator().next().title);
            Boolean firstRun = true;
            for (AuthorRecord authorRecord : record.getAuthorRecords()) {
                buffer = buffer + authorRecord.author.author;
                if (firstRun) {
                    record.setAuthorsString(record.getAuthorRecords().iterator().next().author.author);
                    firstRun = false;
                } else {
                    record.setAuthorsString(record.getAuthorsString() + "; " + authorRecord.author.author);
                }
            }
            for (Title title : record.getTitles()) {
                buffer = buffer + title.title;
            }
            buffer =
                    buffer +
                            record.title_add_data +
                            record.edition +
                            record.edition_add_data +
                            record.publication_place +
                            record.publisher_name +
                            record.publication_date +
                            record.size +
                            record.url +
                            record.url_date +
                            record.note +
                            record.identificator +
                            record.key_title +
                            record.content_type +
                            record.access_mean +
                            record.content_page +
                            record.serial_note;
            if (buffer.contains(search))
                records.add(record);
        }
        mainTable.setItems(records);
    }

    protected void fillTable() throws IOException {
        exportList.clear();
        records.clear();
        for (Record record : recordDao) {
            //try {
                record.setMainTitle(record.getTitles().iterator().next().title);
                Boolean firstRun = true;
                for (AuthorRecord authorRecord : record.getAuthorRecords()) {
                    if (firstRun) {
                        record.setAuthorsString(authorRecord.author.author);
                        firstRun = false;
                    } else {
                        record.setAuthorsString(record.getAuthorsString() + "; " + authorRecord.author.author);
                    }
                }
                records.add(record);
            //}
            //catch (IllegalStateException e)
            //{

            //}
        }
        mainTable.setItems(records);
        connectionSource.close();
    }

    private void dbConnect() throws SQLException {
        connectionSource = MainApplication.connectionSource;
        recordDao = MainApplication.recordDao;
        titleDao = MainApplication.titleDao;
        authorDao = MainApplication.authorDao;
        authorRecordDao = MainApplication.authorRecordDao;
        publicationTypeDao = MainApplication.publicationTypeDao;
    }

    private Stage newWindow(String fxml, String windowTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root1 = loader.load();
        if (Objects.equals(windowTitle, "Добавить новый ресурс")) {
            AddRecordController addRecordController = loader.getController();
            addRecordController.setParentController(this);
        }
        if (Objects.equals(windowTitle, "Экспорт")) {
            SingleExportController singleExportController = loader.getController();
            singleExportController.setRecordsIndex(exportList);
        }
        if (Objects.equals(windowTitle, "Редактирование")) {
            AddRecordController addRecordController = loader.getController();
            addRecordController.setParentController(this);
            addRecordController.fillFields(choice);
        }
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root1));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return stage;
    }

    public void deleteButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        for (Record record : exportList) {
            titleDao.delete(record.getTitles());
            for (AuthorRecord authorRecord : record.authorRecords) {
                if (authorRecordDao.queryForEq("author_id", authorRecord.author).size() == 1) {
                    authorDao.delete(authorRecord.author);
                }
            }
            authorRecordDao.delete(record.getAuthorRecords());
            recordDao.delete(record);
        }
        fillTable();
    }
}
