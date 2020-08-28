package org.pensatocode.loadtest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    @JsonProperty("id")
    public int id;
    @JsonProperty("title")
    public String title;
    @JsonProperty("author")
    public String author;
    @JsonProperty("isbn")
    public String isbn;
}