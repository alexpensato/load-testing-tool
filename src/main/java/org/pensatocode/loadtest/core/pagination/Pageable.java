package org.pensatocode.loadtest.core.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pageable {
    @JsonProperty("sort")
    public Sort sort;
    @JsonProperty("pageNumber")
    public int pageNumber;
    @JsonProperty("pageSize")
    public int pageSize;
    @JsonProperty("offset")
    public int offset;
    @JsonProperty("unpaged")
    public boolean unpaged;
    @JsonProperty("paged")
    public boolean paged;
}
