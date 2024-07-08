package com.andrei.resmerita.library_mangement_system.controller;

import com.andrei.resmerita.library_mangement_system.dto.BookDTO;
import com.andrei.resmerita.library_mangement_system.exception.ErrorResponseDto;
import com.andrei.resmerita.library_mangement_system.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CRUD REST APIs for Books in Library Management System", description = "CRUD REST APIs in Library Management System to CREATE, UPDATE, FETCH AND DELETE book details")
@RestController
@RequestMapping("/books")
@Validated
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Create Book", description = "REST API to create new Book inside Library Management System")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"), @ApiResponse(responseCode = "400", description = "HTTP Status Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @PostMapping("/create")
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.saveBook(bookDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @Operation(summary = "Update Book", description = "REST API to update Book inside Library Management System")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"), @ApiResponse(responseCode = "404", description = "HTTP Status Not Found", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @PutMapping("/update/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String id, @Valid @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @Operation(summary = "Delete Book", description = "REST API to delete Book inside Library Management System")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"), @ApiResponse(responseCode = "404", description = "HTTP Status Not Found", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Fetch All Books", description = "REST API to fetch all Books inside Library Management System")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"), @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/getAllBooks")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(summary = "Fetch Book By Id", description = "REST API to fetch a Book by Id inside Library Management System")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"), @ApiResponse(responseCode = "404", description = "HTTP Status Not Found", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/getBook/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable String id) {
        BookDTO book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
}
