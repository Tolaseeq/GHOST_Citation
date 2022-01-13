package ru.mai.activetest.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable(tableName = "author_records")
public class AuthorRecord {
    @DatabaseField (generatedId = true, canBeNull = false, unique = true)
    public Integer authorRecord_id;
    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = "author_id")
    public Author author;
    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = "record_id")
    public Record record;

    public AuthorRecord(){};
}