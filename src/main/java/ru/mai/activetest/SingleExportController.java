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

    public String formatAuthorEng(Boolean mode, String author) {
        String result;
        if (mode)
            result = toTranslit(author.split(" ")[0]) + ", " + toTranslit(author.split(" ")[1]);
        else
            result = toTranslit(author.split(" ")[1]) + " " + toTranslit(author.split(" ")[0]);
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
                        result = formatAuthorName(true, record.getAuthorRecords().iterator().next().getAuthor().getAuthor());
                        result = result + " " + record.getTitles().iterator().next().title;
                        if (record.getTitle_add_data() != null && !record.getTitle_add_data().isEmpty()) {
                            result = result + ": " + record.getTitle_add_data();
                        }
                        result = result + "/ " + formatAuthorName(false, record.getAuthorRecords().iterator().next().author.getAuthor());
                        if (record.getEdition() != null && !record.getEdition().isEmpty())
                            result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize() + " с.";
                        break;
                    case 2:
                    case 3:
                        result = formatAuthorName(true, record.getAuthorRecords().iterator().next().getAuthor().getAuthor());
                        result = result + " " + record.getTitles().iterator().next().title;
                        if (record.getTitle_add_data() != null && !record.getTitle_add_data().isEmpty()) {
                            result = result + ": " + record.getTitle_add_data();
                        }
                        result = result + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        if (record.getEdition() != null && !record.getEdition().isEmpty())
                            result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize() + " с.";
                        break;
                    case 4:
                        result = record.getTitles().iterator().next().title;
                        if (record.getTitle_add_data() != null && !record.getTitle_add_data().isEmpty()) {
                            result = result + ": " + record.getTitle_add_data();
                        }
                        result = result + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        if (record.getEdition() != null && !record.getEdition().isEmpty())
                            result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize() + " с.";
                        break;
                }
                break;
            case "Статья":
                switch (record.getAuthorRecords().size()) {
                    case 1:
                        result = formatAuthorName(true, record.getAuthorRecords().iterator().next().getAuthor().getAuthor());
                        result = result + " " + record.getTitles().iterator().next().title;
                        if (record.getTitle_add_data() != null && !record.getTitle_add_data().isEmpty()) {
                            result = result + ": " + record.getTitle_add_data();
                        }
                        result = result + "/ " + formatAuthorName(false, record.getAuthorRecords().iterator().next().author.getAuthor());
                        result = result + " .- Текст: " + record.getContent_type();
                        result = result + " // " + record.getSerial_note();
                        result = result + ". - " + record.getPublication_date();
                        result = result + ". - №" + record.getEdition_add_data();
                        result = result + ". - " + record.getSize() + " с.";
                        break;
                    case 2:
                    case 3:
                        result = formatAuthorName(true, record.getAuthorRecords().iterator().next().getAuthor().getAuthor());
                        result = result + " " + record.getTitles().iterator().next().title;
                        if (record.getTitle_add_data() != null && !record.getTitle_add_data().isEmpty()) {
                            result = result + ": " + record.getTitle_add_data();
                        }
                        result = result + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        result = result + " .- Текст: " + record.getContent_type();
                        result = result + " // " + record.getSerial_note();
                        result = result + ". - " + record.getPublication_date();
                        result = result + ". - №" + record.getEdition_add_data();
                        result = result + ". - " + record.getSize() + " с.";
                        break;
                    case 4:
                        result = result + " " + record.getTitles().iterator().next().title;
                        if (record.getTitle_add_data() != null && !record.getTitle_add_data().isEmpty()) {
                            result = result + ": " + record.getTitle_add_data();
                        }
                        result = result + "/ ";
                        for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                            if (i > 0)
                                result = result + ", ";
                            result = result + formatAuthorName(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                        }
                        result = result + " .- Текст: " + record.getContent_type();
                        result = result + " // " + record.getSerial_note();
                        result = result + ". - " + record.getPublication_date();
                        result = result + ". - №" + record.getEdition_add_data();
                        result = result + ". - " + record.getSize() + " с.";
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
                        + ".- " + record.getSize()  + " с."
                        + ".- Библиогр.: " + "с. " + record.getContent_page()
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
                for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                    if (i == 0)
                        result = formatAuthorEng(true, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    if (i + 1 == record.getAuthorRecords().size())
                        result = result + " and " + formatAuthorEng(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    else if (i != 0)
                        result = result + ", " + formatAuthorEng(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                }
                result = result + ". " + record.getTitles().toArray(new Title[0])[1].getTitle();
                result = result + ". " + toTranslit(record.getPublication_place());
                result = result + ": " + toTranslit(record.getPublisher_name()) + ", " + record.getPublication_date() + ".";
                break;
            case "Статья":
                for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                    if (i == 0)
                        result = formatAuthorEng(true, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    if (i + 1 == record.getAuthorRecords().size() && record.getAuthorRecords().size() != 1)
                        result = result + " and " + formatAuthorEng(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    else if (i != 0)
                        result = result + ", " + formatAuthorEng(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                }
                result = result + ". \"" + record.getTitles().toArray(new Title[0])[1].getTitle() + ".\"";
                result = result + " " + toTranslit(record.getSerial_note());
                result = result + ", no. " + record.getEdition_add_data();
                result = result + " (" + record.getPublication_date() + ")";
                result = result + ": " + record.getSize() + ".";
                break;
            case "URL":
                result = record.getTitles().toArray(new Title[0])[1].getTitle();
                result = result + ". \"" + toTranslit(record.getTitle_add_data()) + ".\" ";
                result = result + record.url_date.split("\\.")[1] + "." + record.url_date.split("\\.")[0] + ", " + record.url_date.split("\\.")[2] + ". ";
                result = result + record.url + ".";
                break;
            case "Автореферат/диссертация":
                for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                    if (i == 0)
                        result = formatAuthorEng(true, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    if (i + 1 == record.getAuthorRecords().size() && record.getAuthorRecords().size() != 1)
                        result = result + " and " + formatAuthorEng(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    else if (i != 0)
                        result = result + ", " + formatAuthorEng(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                }
                result = result + ". \"" + record.getTitles().toArray(new Title[0])[1].getTitle() + ".\"";
                result = result + " Dissertation, " + toTranslit(record.getPublisher_name()) + ", " + toTranslit(record.getPublication_place()) + ", " + record.getPublication_date();
                result = result + ", " + record.getContent_page() + ".";
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
