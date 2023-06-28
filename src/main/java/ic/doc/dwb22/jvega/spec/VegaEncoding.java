package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VegaEncoding {
    private VegaEncodingProperties enter;
    private VegaEncodingProperties update;
    private VegaEncodingProperties exit;
    private VegaEncodingProperties hover;
}
