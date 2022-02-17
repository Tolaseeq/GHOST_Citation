package ru.mai.activetest.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable(tableName = "publication_types")
public class PublicationType {
    @DatabaseField (generatedId = true, canBeNull = false, unique = true)
    public Integer publication_id;
    @DatabaseField
    public String publication_type;

    public PublicationType(){};
}
