package ru.mai.activetest;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.mai.activetest.Models.*;

public class MainWindowController implements Initializable {

    public Label searchErrorLabel;
    public Text test_text;
    Dao<Record, Integer> recordDao;
    Dao<Title, Integer> titleDao;
    Dao<Author, Integer> authorDao;
    Dao<AuthorRecord, Integer> authorRecordDao;
    Dao<PublicationType, Integer> publicationTypeDao;
    ObservableList<Record> records = FXCollections.observableArrayList();
    ConnectionSource connectionSource;

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
    private TableColumn<Record, String> editionColumn;

    @FXML
    private TableColumn<Record, String> yearColumn;

    @FXML
    private Button searchButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField searchField;

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
        titleColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("mainTitle"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("authorsString"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("edition"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("publication_date"));
    }

    @FXML
    public void clickItem(MouseEvent mouseEvent) throws IOException {
        mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //System.out.println("Clicked on " + (mainTable.getSelectionModel().getSelectedCells().get(0)).getRow());
                //System.out.println("Clicked on " + (mainTable.getSelectionModel().getSelectedItem().record_id));
                try {
                    Stage stage = newWindow("SingleExport-view.fxml", "Export");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void addButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = newWindow("AddRecord-view.fxml", "Add a new record");
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
        records.clear();
        for (Record record : recordDao) {
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
        }
        mainTable.setItems(records);
        connectionSource.close();
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
        //TableUtils.createTable(connectionSource, PublicationType.class);
    }

    private Stage newWindow(String fxml, String windowTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root1 = loader.load();
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root1));
        stage.show();
        if (Objects.equals(windowTitle, "Add a new record")) {
            AddRecordController addRecordController = loader.getController();
            addRecordController.setParentController(this);
        }
        else {
            SingleExportController singleExportController = loader.getController();
            singleExportController.setRecordIndex(mainTable.getSelectionModel().getSelectedItem());
        }
        return stage;
    }

    public void deleteButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        for (Record record : recordDao) {
            if (mainTable.getSelectionModel().getSelectedItem().getRecord_id() == record.getRecord_id()) {
                titleDao.delete(record.getTitles());
                for (AuthorRecord authorRecord : record.authorRecords) {
                    if (authorRecordDao.queryForEq("author_id", authorRecord.author).size() == 1) {
                        authorDao.delete(authorRecord.author);
                    }
                }
                authorRecordDao.delete(record.getAuthorRecords());
                recordDao.delete(record);
            }
        }
        fillTable();
    }
}
