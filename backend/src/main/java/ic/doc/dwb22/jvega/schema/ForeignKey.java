package ic.doc.dwb22.jvega.schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ForeignKey {
    private String fkName;
    private String fkTableName;
    private List<String> fkColumnNames;
    private String pkTableName;
    private List<String> pkColumnNames;

    public ForeignKey(String fkName, String fkTableName, String pkTableName) {
        this.fkName = fkName;
        this.fkTableName = fkTableName;
        this.fkColumnNames = new ArrayList<>();
        this.pkTableName = pkTableName;
        this.pkColumnNames = new ArrayList<>();
    }

    public String getFkTableName() {
        return fkTableName;
    }

    public List<String> getFkColumnNames() {
        return new ArrayList<>(fkColumnNames);
    }

    public String getPkTableName() {
        return pkTableName;
    }

    public List<String> getPkColumnNames() {
        return new ArrayList<>(pkColumnNames);
    }

    public void addForeignKeyColumn(String columnName) {
        fkColumnNames.add(columnName);
    }

    public void addPrimaryKeyColumn(String columnName) {
        pkColumnNames.add(columnName);
    }

    @Override
    public String toString() {
        return "Foreign Key (" + fkName + ") between: " + fkTableName + "::" + fkColumnNames + " -> " + pkTableName + "::" + pkColumnNames;
    }
}
