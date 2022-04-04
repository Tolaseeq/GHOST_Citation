package ru.mai.activetest;

import com.j256.ormlite.dao.CloseableIterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import ru.mai.activetest.Models.Author;
import ru.mai.activetest.Models.AuthorRecord;
import ru.mai.activetest.Models.Record;
import ru.mai.activetest.Models.Title;

import java.sql.SQLException;

import static ru.mai.activetest.Translit.toTranslit;

public class SingleExportController {
    public TextArea exportField;
    public ComboBox formatBox;
    public Button submitButton;
    private Record record;

    protected void setRecordIndex(Record _record) {
        record = _record;
    }

    @FXML
    public void initialize() {
        ObservableList<String> formatList = FXCollections.observableArrayList();
        formatList.add("ГОСТ Р 7.0.100-2018");
        formatList.add("Chicago");
        formatList.add("Harvard");
        formatBox.setItems(formatList);
        formatBox.setValue(formatList.get(0));
        new AutoCompleteComboBoxListener<>(formatBox);
    }

    public String formatAuthorName(Boolean mode, String author) {
        String result;
        if (author.contains(".")) {
            result = author.split(" ")[1] + " " + author.split(" ")[2] + " " + author.split(" ")[0];
        } else {
            if (mode) {
                result = author.split(" ")[0] + " "
                        + author.split(" ")[1].charAt(0) + ". "
                        + author.split(" ")[2].charAt(0) + ". ";
            } else {
                result = author.split(" ")[1].charAt(0) + "."
                        + author.split(" ")[2].charAt(0) + ". "
                        + author.split(" ")[0];
            }
        }
        return result;
    }

    public String formatAuthorEng (String author) {
        String result;
        result = toTranslit(author.split(" ")[1]) + " " + toTranslit(author.split(" ")[0]);
        //result = toTranslit(result);
        return result;
    }

    public void okButtonClick(ActionEvent actionEvent) throws SQLException {
        switch (formatBox.getValue().toString()) {
            case "ГОСТ Р 7.0.100-2018":
                exportField.setText(gostFormatter());
                break;
            case "Chicago":
                exportField.setText(chicagoFormatter());
                break;
            case "Harvard":
                //TODO
                break;
        }
    }

    private String gostFormatter() throws SQLException {
        String result = null;
        switch (record.getPublicationType().getPublication_type()) {
            case "Книга":
                switch (record.getAuthorRecords().size()) {
                    case 1:
                        result = record.getAuthorRecords().iterator().next().author.getAuthor();
                        result = (new StringBuilder(result)).insert(result.indexOf(" "), ",").toString();
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data();
                        result = result + "/ " + formatAuthorName(false, record.getAuthorRecords().iterator().next().author.getAuthor());
                        if (!record.getEdition().isEmpty())
                            result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize();
                        break;
                    case 2:
                    case 3:
                        result = record.getAuthorRecords().iterator().next().author.getAuthor();
                        result = (new StringBuilder(result)).insert(result.indexOf(" "), ",").toString();
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data() + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        if (!record.getEdition().isEmpty())
                            result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize();
                        break;
                    case 4:
                        result = record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data() + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        if (!record.getEdition().isEmpty())
                            result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize();
                        break;
                }
                break;
            case "Статья":
                switch (record.getAuthorRecords().size()) {
                    case 1:
                        result = record.getAuthorRecords().iterator().next().author.getAuthor();
                        result = (new StringBuilder(result)).insert(result.indexOf(" "), ",").toString();
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data();
                        result = result + "/ " + formatAuthorName(false, record.getAuthorRecords().iterator().next().author.getAuthor());
                        result = result + " .- Текст: " + record.getContent_type();
                        result = result + " // " + record.getSerial_note();
                        result = result + ". - " + record.getPublication_place() + ", " + record.getPublication_date();
                        result = result + ". - " + record.getEdition_add_data();
                        result = result + ". - " + record.getSize();
                        break;
                    case 2:
                    case 3:
                        result = record.getAuthorRecords().iterator().next().author.getAuthor();
                        result = (new StringBuilder(result)).insert(result.indexOf(" "), ",").toString();
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data() + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        result = result + " .- Текст: " + record.getContent_type();
                        result = result + " // " + record.getSerial_note();
                        result = result + ". - " + record.getPublication_place() + ", " + record.getPublication_date();
                        result = result + ". - " + record.getEdition_add_data();
                        result = result + ". - " + record.getSize();
                        break;
                    case 4:
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data() + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        result = result + " .- Текст: " + record.getContent_type();
                        result = result + " // " + record.getSerial_note();
                        result = result + ". - " + record.getPublication_place() + ", " + record.getPublication_date();
                        result = result + ". - " + record.getEdition_add_data();
                        result = result + ". - " + record.getSize();
                        break;
                }
                if (record.getUrl() != null) {
                    result = result + ".- URL: " + record.getUrl() + " (дата обращения: " + record.getUrl_date() + ").";
                }
                break;
            case "URL":
                result = record.getTitles().iterator().next().title;
                result = result + ": " + record.getTitle_add_data() + ".- URL: ";
                result = result + record.url + (" (дата обращения: ") + record.url_date + ").";
                break;
            case "Автореферат/диссертация":
                result = formatAuthorName(true, record.getAuthorRecords().iterator().next().getAuthor().getAuthor())
                        + " " + record.getTitles().iterator().next().getTitle()
                        + ": специальность " + record.getEdition()
                        + " \"" + record.getEdition_add_data() + "\""
                        + " : " + record.getTitle_add_data()
                        + "/ " + record.getAuthorRecords().iterator().next().getAuthor().getAuthor()
                        + "; " + record.getPublisher_name()
                        + ".- " + record.getPublication_place() + ", " + record.getPublication_date()
                        + ".- " + record.getSize()
                        + ".- Библиогр.: " + record.getContent_page()
                        + ".- Текст: непосредственный";
                break;
        }
        while (result.contains("..")) {
            result = new StringBuffer(result).deleteCharAt(result.indexOf("..") + 1).toString();
        }
        while (result.contains(".-.-")) {
            result = new StringBuffer(result).deleteCharAt(result.indexOf(".-.-") + 1).toString();
        }
        return result;
    }

    private String chicagoFormatter() throws SQLException {
        String result = null;
        switch (record.getPublicationType().getPublication_type()) {
            case "Книга":
                switch (record.getAuthorRecords().size()) {
                    case 1:
                        result = formatAuthorEng(record.getAuthorRecords().iterator().next().getAuthor().getAuthor());
                        result = result + " " + record.getTitles().iterator().next().getTitle();
                        result = result + ": " + record.getTitle_add_data();
                        result = result + "/ " + formatAuthorName(false, record.getAuthorRecords().iterator().next().author.getAuthor());
                        if (record.getEdition() != null && !record.getEdition().isEmpty())
                            result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize();
                        break;
                    case 2:
                    case 3:
                        result = record.getAuthorRecords().iterator().next().author.getAuthor();
                        result = (new StringBuilder(result)).insert(result.indexOf(" "), ",").toString();
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data() + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        if (!record.getEdition().isEmpty())
                            result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize();
                        break;
                    case 4:
                        result = record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data() + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        if (!record.getEdition().isEmpty())
                            result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize();
                        break;
                }
                break;
            case "Статья":
                switch (record.getAuthorRecords().size()) {
                    case 1:
                        result = record.getAuthorRecords().iterator().next().author.getAuthor();
                        result = (new StringBuilder(result)).insert(result.indexOf(" "), ",").toString();
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data();
                        result = result + "/ " + formatAuthorName(false, record.getAuthorRecords().iterator().next().author.getAuthor());
                        result = result + " .- Текст: " + record.getContent_type();
                        result = result + " // " + record.getSerial_note();
                        result = result + ". - " + record.getPublication_place() + ", " + record.getPublication_date();
                        result = result + ". - " + record.getEdition_add_data();
                        result = result + ". - " + record.getSize();
                        break;
                    case 2:
                    case 3:
                        result = record.getAuthorRecords().iterator().next().author.getAuthor();
                        result = (new StringBuilder(result)).insert(result.indexOf(" "), ",").toString();
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data() + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        result = result + " .- Текст: " + record.getContent_type();
                        result = result + " // " + record.getSerial_note();
                        result = result + ". - " + record.getPublication_place() + ", " + record.getPublication_date();
                        result = result + ". - " + record.getEdition_add_data();
                        result = result + ". - " + record.getSize();
                        break;
                    case 4:
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data() + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        result = result + " .- Текст: " + record.getContent_type();
                        result = result + " // " + record.getSerial_note();
                        result = result + ". - " + record.getPublication_place() + ", " + record.getPublication_date();
                        result = result + ". - " + record.getEdition_add_data();
                        result = result + ". - " + record.getSize();
                        break;
                }
                if (record.getUrl() != null) {
                    result = result + ".- URL: " + record.getUrl() + " (дата обращения: " + record.getUrl_date() + ").";
                }
                break;
            case "URL":
                result = record.getTitles().iterator().next().title;
                result = result + ": " + record.getTitle_add_data() + ".- URL: ";
                result = result + record.url + (" (дата обращения: ") + record.url_date + ").";
                break;
            case "Автореферат/диссертация":
                result = formatAuthorName(true, record.getAuthorRecords().iterator().next().getAuthor().getAuthor())
                        + " " + record.getTitles().iterator().next().getTitle()
                        + ": специальность " + record.getEdition()
                        + " \"" + record.getEdition_add_data() + "\""
                        + " : " + record.getTitle_add_data()
                        + "/ " + record.getAuthorRecords().iterator().next().getAuthor().getAuthor()
                        + "; " + record.getPublisher_name()
                        + ".- " + record.getPublication_place() + ", " + record.getPublication_date()
                        + ".- " + record.getSize()
                        + ".- Библиогр.: " + record.getContent_page()
                        + ".- Текст: непосредственный";
                break;
        }
        while (result.contains("..")) {
            result = new StringBuffer(result).deleteCharAt(result.indexOf("..") + 1).toString();
        }
        while (result.contains(".-.-")) {
            result = new StringBuffer(result).deleteCharAt(result.indexOf(".-.-") + 1).toString();
        }
        return result;
    }
}
