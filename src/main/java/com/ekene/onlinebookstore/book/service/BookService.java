package com.ekene.onlinebookstore.book.service;

import com.ekene.onlinebookstore.book.model.payload.BookDto;
import org.springframework.data.domain.Page;

public interface BookService {
    BookDto saveBook(BookDto bookDto);

    BookDto updateBook(Long id, BookDto bookDto);

    BookDto getBook(Long id);

    Page<BookDto> getAvailableBooks(int page, int size);

    String deleteBook(Long id);
}
