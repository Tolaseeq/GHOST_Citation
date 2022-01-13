package ru.mai.activetest.Models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable(tableName = "authors")
public class Author {
    @DatabaseField (generatedId = true, canBeNull = false, unique = true)
    public Integer author_id;
    @DatabaseField
    public String author;
    @ForeignCollectionField(eager = false)
    public ForeignCollection<AuthorRecord> authorRecords;

    public Author(){};
}