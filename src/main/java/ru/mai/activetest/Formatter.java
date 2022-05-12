package ru.mai.activetest;

import javafx.scene.control.Alert;
import ru.mai.activetest.Models.AuthorRecord;
import ru.mai.activetest.Models.Record;
import ru.mai.activetest.Models.Title;

import static ru.mai.activetest.Translit.toTranslit;

public class Formatter {
    Alert alert = new Alert(Alert.AlertType.ERROR);

    public String gostFormatter(Record record) {
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
                        result = result + ". - с. " + record.getSize();
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
                        + ".- " + record.getSize()  + " с.";
                if (record.getContent_page() != null)
                    result = result + ".- Библиогр.: " + "с. " + record.getContent_page();
                result = result + ".- Текст: непосредственный";
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

    public String chicagoFormatter(Record record) {
        String result = null;
        for (AuthorRecord authorRecord: record.getAuthorRecords())
        {
            if (authorRecord.getAuthor().getAuthor().contains(".")) {
                alert.setTitle("Внимание!");
                alert.setHeaderText("Ошибка формата!");
                alert.setContentText("Формат Chicago не поддерживает сокращения в ФИО авторов!");
                alert.showAndWait();
                return "";
            }
            if (record.getTitles().size() < 2) {
                alert.setTitle("Внимание!");
                alert.setHeaderText("Ошибка формата!");
                alert.setContentText("Отсутствует англоязычное название!");
                alert.showAndWait();
                return "";
            }
        }
        switch (record.getPublicationType().getPublication_type()) {
            case "Книга":
                for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                    if (i == 0)
                        result = formatAuthorChicago(true, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    if (i + 1 == record.getAuthorRecords().size())
                        result = result + " and " + formatAuthorChicago(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    else if (i != 0)
                        result = result + ", " + formatAuthorChicago(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                }
                result = result + ". " + record.getTitles().toArray(new Title[0])[1].getTitle();
                result = result + ". " + toTranslit(record.getPublication_place());
                result = result + ": " + toTranslit(record.getPublisher_name()) + ", " + record.getPublication_date() + ".";
                break;
            case "Статья":
                for (int i = 0; i < record.getAuthorRecords().size(); i++) {
                    if (i == 0)
                        result = formatAuthorChicago(true, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    if (i + 1 == record.getAuthorRecords().size() && record.getAuthorRecords().size() != 1)
                        result = result + " and " + formatAuthorChicago(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    else if (i != 0)
                        result = result + ", " + formatAuthorChicago(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
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
                        result = formatAuthorChicago(true, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    if (i + 1 == record.getAuthorRecords().size() && record.getAuthorRecords().size() != 1)
                        result = result + " and " + formatAuthorChicago(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                    else if (i != 0)
                        result = result + ", " + formatAuthorChicago(false, record.getAuthorRecords().toArray(new AuthorRecord[0])[i].getAuthor().getAuthor());
                }
                result = result + ". \"" + record.getTitles().toArray(new Title[0])[1].getTitle() + ".\"";
                result = result + " Dissertation, " + toTranslit(record.getPublisher_name()) + ", " + toTranslit(record.getPublication_place()) + ", " + record.getPublication_date();
                if (record.getContent_page() != null)
                    result = result + ", " + record.getContent_page();
                result = result  + ".";
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

    public String harvardFormatter(Record record) {
        String result = null;
        if (record.getTitles().size() < 2) {
            alert.setTitle("Внимание!");
            alert.setHeaderText("Ошибка формата!");
            alert.setContentText("Отсутствует англоязычное название!");
            alert.showAndWait();
            return "";
        }
        switch (record.getPublicationType().getPublication_type()) {
            case "Книга":
                switch (record.getAuthorRecords().size())
                {
                    case 1:
                        result = formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[0].getAuthor().getAuthor());
                        break;
                    case 2:
                        result = formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[0].getAuthor().getAuthor())+ " and " + formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[1].getAuthor().getAuthor());
                        break;
                    default:
                        result = formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[0].getAuthor().getAuthor() + " et al. ");
                        break;
                }
                result = result + " (" + record.publication_date + ") ";
                result = result + record.getTitles().toArray(new Title[0])[1].getTitle() + ". ";
                result = result + toTranslit(record.publication_place) + ": " + toTranslit(record.publisher_name) + ".";
                break;
            case "Статья":
                switch (record.getAuthorRecords().size())
                {
                    case 1:
                        result = formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[0].getAuthor().getAuthor());
                        break;
                    case 2:
                        result = formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[0].getAuthor().getAuthor())+ " and " + formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[1].getAuthor().getAuthor());
                        break;
                    default:
                        result = formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[0].getAuthor().getAuthor() + " et al. ");
                        break;
                }
                result = result + " (" + record.publication_date + ") ";
                result = result + "'" +record.getTitles().toArray(new Title[0])[1].getTitle() + "', ";
                result = result + toTranslit(record.getSerial_note());
                result = result + ", no. " + record.getEdition_add_data();
                result = result + ", pp. " + record.getSize() + ".";
                if (record.url != null && record.url_date != null)
                {
                    result = result + "Available at: " + record.url + " (Accessed: " + record.url_date.split("\\.")[1] + "." + record.url_date.split("\\.")[0] + "." + record.url_date.split("\\.")[2] + ". " + ").";
                }
                break;
            case "URL":
                if (record.publication_date == null || record.publication_date.isEmpty())
                {
                    result = record.getTitles().toArray(new Title[0])[1].getTitle();
                    result = result + " (no date) Available at: ";
                    result = result + record.url;
                    result = result + " (Accessed: " + record.url_date.split("\\.")[1] + "." + record.url_date.split("\\.")[0] + "." + record.url_date.split("\\.")[2] + ". " + ").";
                }
                else {
                    result = record.getTitles().toArray(new Title[0])[1].getTitle();
                    result = result + " (" + record.publication_date + ") Available at: ";
                    result = result + record.url;
                    result = result + " (Accessed: " + record.url_date.split("\\.")[1] + "." + record.url_date.split("\\.")[0] + "." + record.url_date.split("\\.")[2] + ". " + ").";
                }
                break;
            case "Автореферат/диссертация":
                switch (record.getAuthorRecords().size())
                {
                    case 1:
                        result = formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[0].getAuthor().getAuthor());
                        break;
                    case 2:
                        result = formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[0].getAuthor().getAuthor())+ " and " + formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[1].getAuthor().getAuthor());
                        break;
                    default:
                        result = formatAuthorHarvard(record.getAuthorRecords().toArray(new AuthorRecord[0])[0].getAuthor().getAuthor() + " et al. ");
                        break;
                }
                result = result + " (" + record.publication_date + "). ";
                result = result + record.getTitles().toArray(new Title[0])[1].getTitle() + ". ";
                result = result + "Dissertation. " + toTranslit(record.getPublication_place()) + ": " + toTranslit(record.getPublisher_name()) + ".";
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

    public String formatAuthorChicago(Boolean mode, String author) {
        String result;
        if (mode)
            result = toTranslit(author.split(" ")[0]) + ", " + toTranslit(author.split(" ")[1]);
        else
            result = toTranslit(author.split(" ")[1]) + " " + toTranslit(author.split(" ")[0]);
        return result;
    }

    public String formatAuthorHarvard(String author) {
        String result = null;
        if (author.contains(".")) {
            result = toTranslit(author);
        } else {
            result = toTranslit(author);
            result = result.split(" ")[0] + ", "
                    + result.split(" ")[1].charAt(0) + ". "
                    + result.split(" ")[2].charAt(0) + ".";
        }
        return result;
    }
}
