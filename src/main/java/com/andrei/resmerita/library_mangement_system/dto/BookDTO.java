package com.andrei.resmerita.library_mangement_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Book", description = "Schema to hold Book information")
public class BookDTO {

    @Schema(description = "Title of the book", example = "Harry Potter and the Philosopher's Stone")
    @NotEmpty(message = "Title cannot be null or empty")
    private String title;

    @Schema(description = "Description of the book", example = "The first book in the Harry Potter series.")
    private String description;

    @Schema(description = "ISBN of the book", example = "9780747532699")
    @NotEmpty(message = "ISBN cannot be null or empty")
    private String isbn;

    @Schema(description = "Author of the book")
    @Valid
    private AuthorDTO author;
}
