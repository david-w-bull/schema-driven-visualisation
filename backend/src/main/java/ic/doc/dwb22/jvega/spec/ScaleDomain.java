package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScaleDomain {
    private String data;
    private Object field;
    private List<Object> fields;
    private Object sort; // Most likely to be Boolean but Object valued in case a sort object is passed

    public static ScaleDomain simpleDomain(String dataSetName, String field) {
        ScaleDomain domain = new ScaleDomain();
        domain.data = dataSetName;
        domain.field = field;
        return domain;
    }
}
