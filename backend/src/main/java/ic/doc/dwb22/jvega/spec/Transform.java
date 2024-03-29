package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.source.tree.Tree;
import ic.doc.dwb22.jvega.spec.transforms.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PieTransform.class, name = "pie"),
        @JsonSubTypes.Type(value = FormulaTransform.class, name = "formula"),
        @JsonSubTypes.Type(value = FilterTransform.class, name = "filter"),
        @JsonSubTypes.Type(value = FoldTransform.class, name = "fold"),
        @JsonSubTypes.Type(value = StackTransform.class, name = "stack"),
        @JsonSubTypes.Type(value = AggregateTransform.class, name = "aggregate"),
        @JsonSubTypes.Type(value = LookupTransform.class, name = "lookup"),
        @JsonSubTypes.Type(value = LinkPathTransform.class, name = "linkpath"),
        @JsonSubTypes.Type(value = CollectTransform.class, name = "collect"),
        @JsonSubTypes.Type(value = WordCloudTransform.class, name = "wordcloud"),
        @JsonSubTypes.Type(value = ProjectTransform.class, name = "project"),
        @JsonSubTypes.Type(value = StratifyTransform.class, name = "stratify"),
        @JsonSubTypes.Type(value = TreemapTransform.class, name = "treemap"),
})
public interface Transform {
    // This interface is used only as a marker to provide a general type.
    // Transforms currently have no shared methods
}
