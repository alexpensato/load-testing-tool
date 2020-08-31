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
    private Sort sort;
    @JsonProperty("pageNumber")
    private int pageNumber;
    @JsonProperty("pageSize")
    private int pageSize;
    @JsonProperty("offset")
    private int offset;
    @JsonProperty("unpaged")
    private boolean unpaged;
    @JsonProperty("paged")
    private boolean paged;
}
