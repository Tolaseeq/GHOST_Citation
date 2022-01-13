package ru.mai.activetest.Models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable(tableName = "records")
public class Record {
    @DatabaseField (generatedId = true, canBeNull = false, unique = true)
    public Integer record_id;
    @ForeignCollectionField(eager = false)
    public ForeignCollection<Title> titles;
    String mainTitle;
    @DatabaseField
    public String title_add_data;
    @ForeignCollectionField(eager = false)
    public ForeignCollection<AuthorRecord> authorRecords;
    String authorsString;
    @DatabaseField
    public String edition;
    @DatabaseField
    public String edition_add_data;
    @DatabaseField
    public String publication_place;
    @DatabaseField (defaultValue = "[б.и.]")
    public String publisher_name;
    @DatabaseField
    public String publication_date;
    @DatabaseField
    public String size;
    @DatabaseField
    public String url;
    @DatabaseField
    public String url_date;
    @DatabaseField
    public String note;
    @DatabaseField
    public String identificator;
    @DatabaseField
    public String key_title;
    @DatabaseField
    public String content_type;
    @DatabaseField
    public String access_mean;
    @DatabaseField
    public String content_page;
    @DatabaseField
    public String serial_note;

    public Record(){};
}
