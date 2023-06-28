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
public class VegaScaleDomain {
    private String data;
    private Object field;
    private List<Object> fields;
    private Object sort; // Most likely to be Boolean but Object valued in case a sort object is passed

    public static VegaScaleDomain simpleDomain(String dataSetName, String field) {
        VegaScaleDomain domain = new VegaScaleDomain();
        domain.data = dataSetName;
        domain.field = field;
        return domain;
    }
}
