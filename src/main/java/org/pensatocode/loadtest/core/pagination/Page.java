package org.pensatocode.loadtest.core.pagination;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Page<T> {
    @JsonProperty("content")
    private List<T> content = null;
    @JsonProperty("pageable")
    private Pageable pageable;
    @JsonProperty("totalPages")
    private int totalPages;
    @JsonProperty("last")
    private boolean last;
    @JsonProperty("totalElements")
    private int totalElements;
    @JsonProperty("first")
    private boolean first;
    @JsonProperty("size")
    private int size;
    @JsonProperty("number")
    private int number;
    @JsonProperty("sort")
    private Sort sort;
    @JsonProperty("numberOfElements")
    private int numberOfElements;
    @JsonProperty("empty")
    private boolean empty;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
