package ru.mai.activetest;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.support.ConnectionSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.mai.activetest.Models.Author;
import ru.mai.activetest.Models.AuthorRecord;
import ru.mai.activetest.Models.Record;
import ru.mai.activetest.Models.Title;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static ru.mai.activetest.Translit.toTranslit;

public class SingleExportController {
    public TextArea exportField;
    public ComboBox formatBox;
    public Button submitButton;
    public ComboBox exportTypeBox;
    public HBox wayBox;
    public Label wayLabel;
    public TextField wayField;
    public Button chooseFileButton;
    private ArrayList<Record> exportList;
    private String bibList = "";
    private File directory;
    Alert alert = new Alert(Alert.AlertType.ERROR);

    protected void setRecordsIndex (ArrayList<Record> _exportList) {exportList=_exportList;}
    @FXML
    public void initialize() {
        ObservableList<String> formatList = FXCollections.observableArrayList();
        formatList.add("ГОСТ Р 7.0.100-2018");
        formatList.add("Chicago");
        formatList.add("Harvard");
        formatBox.setItems(formatList);
        formatBox.setValue(formatList.get(0));
        ObservableList<String> exportTypeList = FXCollections.observableArrayList();
        exportTypeList.add("Поле окна");
        exportTypeList.add("Файл Word");
        exportTypeBox.setItems(exportTypeList);
        exportTypeBox.setValue(exportTypeList.get(0));
        new AutoCompleteComboBoxListener<>(formatBox);
        typeChange();
    }

    public void typeChange() {
        switch (exportTypeBox.getValue().toString()) {
            case ("Поле окна"):
                wayBox.setVisible(false);
                wayField.setVisible(false);
                wayBox.setVisible(false);
                exportField.setVisible(true);
                break;
            case ("Файл Word"):
                wayBox.setVisible(true);
                wayField.setVisible(true);
                wayBox.setVisible(true);
                exportField.setVisible(false);
                break;
        }
    }

    public void okButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        if (exportTypeBox.getValue().equals("Файл Word") && ((wayField.getText() == null || wayField.getText().equals(""))))
        {
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Путь к директории не может быть пустым!");
            alert.setContentText("Выберите директорию экспорта!");
            alert.showAndWait();
            return;
        }
        if (exportTypeBox.getValue().equals("Файл Word") && new File(directory.getAbsolutePath() + "\\export.docx").exists())
        {
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Файл уже существует!");
            alert.setContentText("export.docx уже существует в указанной директории");
            alert.showAndWait();
            return;
        }
        bibList = "";
        exportField.clear();
        Formatter formatter = new Formatter();
        int rowNumber = 1;
        switch (formatBox.getValue().toString()) {
            case "ГОСТ Р 7.0.100-2018":
                for (Record record: exportList)
                {
                    bibList = bibList + rowNumber + ". " + formatter.gostFormatter(record) + "\n";
                    rowNumber++;
                }
                break;
            case "Chicago":
                for (Record record: exportList)
                {
                    bibList = bibList + rowNumber + ". " + formatter.chicagoFormatter(record) + "\n";
                    rowNumber++;
                }
                break;
            case "Harvard":
                for (Record record: exportList)
                {
                    bibList = bibList + rowNumber + ". " + formatter.harvardFormatter(record) + "\n";
                    rowNumber++;
                }
                break;
        }
        if (exportTypeBox.getValue() == "Поле окна")
            exportField.setText(bibList);
        else {
            XWPFDocument document = new XWPFDocument();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(wayField.getText() + "\\export.docx");
            } catch (IOException e) {
                alert.setTitle("Внимание!");
                alert.setHeaderText("Ошибка экспорта:");
                alert.setContentText("Проверьте правильность указанного пути");
                alert.showAndWait();
                return;
            }
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            for (String row: bibList.split("\n")) {
                run.setText(row);
                run.addBreak();
            }
            try {
                document.write(fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                alert.setTitle("Внимание!");
                alert.setHeaderText("Ошибка экспорта:");
                alert.setContentText("Проверьте правильность указанного пути");
                alert.showAndWait();
                return;
            }
            try {
                fileOutputStream.close();
                alert.setTitle("Экспорт выполнен!");
                alert.setHeaderText("Нажмите ОК для продолжения");
                alert.setContentText("В указанной директории создан файл export.docx");
                alert.showAndWait();
                return;
            } catch (IOException e) {
                alert.setTitle("Внимание!");
                alert.setHeaderText("Критическая ошибка!");
                alert.setContentText("Закрытие программы...");
                alert.showAndWait();
                return;
            }
        }
    }

    public void chooseFileButtonClick(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Обзор");
        directory = directoryChooser.showDialog(chooseFileButton.getScene().getWindow());
        if (new File(directory.getAbsolutePath() + "\\export.docx").exists())
        {
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Файл уже существует!");
            alert.setContentText("export.docx уже существует в указанной директории");
            alert.showAndWait();
            wayField.setText("");
            return;
        }
        wayField.setText(directory.getAbsolutePath());
    }
}
