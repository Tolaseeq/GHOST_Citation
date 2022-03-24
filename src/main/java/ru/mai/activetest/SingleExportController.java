package ru.mai.activetest;

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

public class SingleExportController {
    public TextArea exportField;
    public ComboBox formatBox;
    public Button submitButton;
    private Record record;

    protected void setRecordIndex (Record _record)
    {
        record = _record;
    }

    @FXML
    public void initialize() {
        ObservableList<String> formatList = FXCollections.observableArrayList();
        formatList.add("ГОСТ Р 7.0.100-2018");
        formatList.add("Chicago");
        formatList.add("Harvard");
        formatBox.setItems(formatList);
        new AutoCompleteComboBoxListener<>(formatBox);
    }

    public void okButtonClick(ActionEvent actionEvent) {
        switch (formatBox.getValue().toString()) {
            case "ГОСТ Р 7.0.100-2018":
                exportField.setText(gostFormatter());
                break;
            case "Chicago":
                //TODO
                break;
            case "Harvard":
                //TODO
                break;
        }
    }

    private String gostFormatter ()
    {
        String result = null;
        switch (record.getPublicationType().getPublication_type()) {
            case "Книга":
                switch (record.getAuthorRecords().size()) {
                    case 1:
                        result = record.getAuthorRecords().iterator().next().author.getAuthor();
                        result = (new StringBuilder(result)).insert(result.indexOf(" "), ",").toString();
                        result = result + " " + record.getTitles().iterator().next().title;
                        result = result + ": " + record.getTitle_add_data();
                        String[] buffer = record.getAuthorRecords().iterator().next().getAuthor().getAuthor().split(" ");
                        result = result + "/ " + record.getAuthorRecords().iterator().next().getAuthor().getAuthor().split(" ")[1] + " " + record.getAuthorRecords().iterator().next().getAuthor().getAuthor().split(" ")[2] + " " + record.getAuthorRecords().iterator().next().getAuthor().getAuthor().split(" ")[0];
                        result = result + ". - " + record.getEdition();
                        result = result + ". - " + record.getPublication_place() + ": " + record.getPublisher_name() + ", " + record.getPublication_date() + ". - " + record.getSize();
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                }
                break;
            case "Статья":
                //TODO
                break;
            case "URL":
                //TODO
                break;
            case "Автореферат/диссертация":
                //TODO
                break;
        }

        return result;
    }
}
