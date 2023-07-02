package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Facet {
    private String name;
    private String data;
    private Object field;
    private List<Object> groupby;
    private Object aggregate;

    public static Facet simpleFacet(String name, String data, String groupby) {
        Facet facet = new Facet.BuildFacet()
                .withName(name)
                .withData(data)
                .withGroupBy(groupby)
                .build();
        return facet;
    }
    public static class BuildFacet {
        private String name;
        private String data;
        private Object field;
        private List<Object> groupby;
        private Object aggregate;

        public BuildFacet withName(String name) {
            this.name = name;
            return this;
        }

        public BuildFacet withData(String data) {
            this.data = data;
            return this;
        }

        public BuildFacet withField(Object field) {
            this.field = field;
            return this;
        }

        public BuildFacet withGroupBy(String groupby) {
            if(this.groupby == null) {
                this.groupby = new ArrayList<>();
            }
            this.groupby.add(groupby);
            return this;
        }

        public BuildFacet withAggregate(Object aggregate) {
            this.aggregate = aggregate;
            return this;
        }

        public Facet build() {
            return new Facet(name, data, field, groupby, aggregate);
        }
    }
}
