package ic.doc.dwb22.jvega.schema;

import java.util.ArrayList;
import java.util.List;

public class ForeignKey {
    private final String fkTableName;
    private final List<String> fkColumnNames;
    private final String pkTableName;
    private final List<String> pkColumnNames;

    public ForeignKey(String fkTableName, List<String> fkColumnNames, String pkTableName, List<String> pkColumnNames) {
        this.fkTableName = fkTableName;
        this.fkColumnNames = new ArrayList<>(fkColumnNames);
        this.pkTableName = pkTableName;
        this.pkColumnNames = new ArrayList<>(pkColumnNames);
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

    @Override
    public String toString() {
        return "ForeignKey between: " + fkTableName + "::" + fkColumnNames + " -> " + pkTableName + "::" + pkColumnNames;
    }
}
