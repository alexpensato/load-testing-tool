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
    public List<T> content = null;
    @JsonProperty("pageable")
    public Pageable pageable;
    @JsonProperty("totalPages")
    public int totalPages;
    @JsonProperty("last")
    public boolean last;
    @JsonProperty("totalElements")
    public int totalElements;
    @JsonProperty("first")
    public boolean first;
    @JsonProperty("size")
    public int size;
    @JsonProperty("number")
    public int number;
    @JsonProperty("sort")
    public Sort sort;
    @JsonProperty("numberOfElements")
    public int numberOfElements;
    @JsonProperty("empty")
    public boolean empty;

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
