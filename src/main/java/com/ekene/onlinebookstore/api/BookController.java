package com.ekene.onlinebookstore.api;

import com.ekene.onlinebookstore.exception.BookNotAvailableException;
import com.ekene.onlinebookstore.exception.BookNotFound;
import com.ekene.onlinebookstore.book.model.payload.BookDto;
import com.ekene.onlinebookstore.book.service.BookService;
import com.ekene.onlinebookstore.utils.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController extends BaseController {
    private final BookService bookService;


    @PostMapping()
    public ResponseEntity<?> saveBook(@RequestBody BookDto bookDto) {
        return getAppResponse(HttpStatus.CREATED, "saved successfully", bookService.saveBook(bookDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        try {
            return getAppResponse(HttpStatus.OK, "Updated successfully", bookService.updateBook(id, bookDto));
        } catch (BookNotFound ex){
            return new ResponseEntity<>("Book with this id " + id + " number not found", HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        try {
            return getAppResponse(HttpStatus.OK, "Retrieved successfully", bookService.getBook(id));
        } catch (BookNotAvailableException ex){
            return new ResponseEntity<>("Book with this id " + id + " number not found and/or currently not available", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return getAppResponse(HttpStatus.OK, "All books retrieved successfully", bookService.getAvailableBooks(page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            return getAppResponse(HttpStatus.OK, "book Deletion is successfully", bookService.deleteBook(id));
        } catch (BookNotFound ex){
            return new ResponseEntity<>("Book with this id " + id + " number not found", HttpStatus.BAD_REQUEST);
        }
    }
}
