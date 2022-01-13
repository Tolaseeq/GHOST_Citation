package ru.mai.activetest.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable(tableName = "titles")
public class Title {
    @DatabaseField (generatedId = true, canBeNull = false, unique = true)
    public Integer title_id;
    @DatabaseField
    public String title;
    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = "record_id")
    public Record record;

    public Title(){};
}
