package com.andrei.resmerita.library_mangement_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Author", description = "Schema to hold Author and Book information")
public class AuthorDTO {

    @Schema(description = "Name of the author", example = "J.K. Rowling")
    @NotEmpty(message = "Name cannot be null or empty")
    private String name;

    @Schema(description = "Biography of the author", example = "British author, best known for the Harry Potter series.")
    @NotEmpty(message = "Biography cannot be null or empty")
    private String bio;

    @Schema(description = "Books written by the author")
    @Valid
    private Set<@Valid BookDTO> books;
}