package ic.doc.dwb22.jvega.spec.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.Transform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectTransform implements Transform {
    private String type = "project";
    private List<String> fields;
    private List<String> aliases;

    public static ProjectTransform simpleProject(List<String> fields, List<String> aliases) {
        ProjectTransform transform = new ProjectTransform.BuildTransform()
                .withFields(fields)
                .withAliases(aliases)
                .build();
        return transform;
    }

    public static class BuildTransform {
        private String type = "project";
        private List<String> fields;
        private List<String> aliases;

        public BuildTransform withType(String type) {
            this.type = type;
            return this;
        }

        public BuildTransform withFields(List<String> fields) {
            this.fields = fields;
            return this;
        }

        public BuildTransform withField(String field) {
            if(this.fields == null) {
                this.fields = new ArrayList<>();
            }
            this.fields.add(field);
            return this;
        }

        public BuildTransform withAliases(List<String> aliases) {
            this.aliases = aliases;
            return this;
        }

        public BuildTransform withAlias(String alias) {
            if(this.aliases == null) {
                this.aliases = new ArrayList<>();
            }
            this.aliases.add(alias);
            return this;
        }

        public ProjectTransform build() {
            return new ProjectTransform(type, fields, aliases);
        }
    }
}
