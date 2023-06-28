package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VegaDataset {

    private String name;
    private String url;

    public static VegaDataset urlDataset(String name, String url) {
        VegaDataset data = new VegaDataset();
        data.name = name;
        data.url = url;
        return data;
    }
}
