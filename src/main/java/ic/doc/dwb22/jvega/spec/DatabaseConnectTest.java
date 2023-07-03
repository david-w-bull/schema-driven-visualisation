package ic.doc.dwb22.jvega.spec;

import io.github.MigadaTang.*;
import io.github.MigadaTang.common.RDBMSType;
import io.github.MigadaTang.exception.DBConnectionException;
import io.github.MigadaTang.exception.ParseException;
import io.github.MigadaTang.transform.Reverse;
import java.io.IOException;
import java.sql.SQLException;

public class DatabaseConnectTest {
    public void reverseEngineer()
            throws SQLException, ParseException, DBConnectionException, IOException {
        ER.initialize();
        Reverse reverse = new Reverse();
        Schema schema = reverse.relationSchemasToERModel(RDBMSType.POSTGRESQL, "localhost"
                , "5432", "jvegatest", "david", "dReD@pgs5b!");
        System.out.println(schema.toJSON());
    }

}
