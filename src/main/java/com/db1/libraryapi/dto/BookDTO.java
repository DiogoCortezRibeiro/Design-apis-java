package com.db1.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    private Long id;

}
