package com.ekene.onlinebookstore.service;

import com.ekene.onlinebookstore.book.service.BookServiceImpl;
import com.ekene.onlinebookstore.cache.SystemCache;
import com.ekene.onlinebookstore.book.model.entity.Book;
import com.ekene.onlinebookstore.book.model.payload.BookDto;
import com.ekene.onlinebookstore.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BookServiceImplTest.class)
public class BookServiceImplTest {

    private BookServiceImpl bookService;

    @Mock
    private SystemCache systemCache;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookRepository, systemCache);
    }

    @Test
    void testSaveBook() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Book Title");
        bookDto.setAuthor("author2@example.com");

        Book book = new Book();
        book.setAuthor("author2@example.com");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto savedBookDto = bookService.saveBook(bookDto);

        assertNotNull(savedBookDto);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        Long id = 978L;
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Updated Book Title");
        bookDto.setAuthor("author2@example.com");

        Book existingBook = new Book();
        Book updatedBook = new Book();
        updatedBook.setAuthor(bookDto.getAuthor());

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        BookDto updatedBookDto = bookService.updateBook(id, bookDto);

        assertNotNull(updatedBookDto);
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testGetBook() {
        Long id = 978L;
        Book book = new Book();
        book.setAuthor("");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getBook(id);

        assertNotNull(bookDto);
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllBooks() {
        int page = 0;
        int size = 10;

        String isbn = "978-0123456789";
        Book book1 = new Book();
        book1.setIsbn(isbn);
        book1.setAuthor("");
        book1.setAvailable(true);

        String isbn2 = "978-0123456789";
        Book book2 = new Book();
        book2.setIsbn(isbn2);
        book2.setAuthor("");
        book2.setAvailable(false);

        PageRequest pageRequest = PageRequest.of(page, size);
        List<Book> books = Arrays.asList(book1);
        Page<Book> bookPage = new PageImpl<>(books);

        when(bookRepository.findAllByAvailableTrue(pageRequest)).thenReturn(bookPage);

        Page<BookDto> bookDtoPage = bookService.getAvailableBooks(page, size);

        assertNotNull(bookDtoPage);
        assertEquals(1, bookDtoPage.getContent().size());
        verify(bookRepository, times(1)).findAllByAvailableTrue(pageRequest);
    }

    @Test
    void testDeleteBook() {
        Long id = 978L;
        Book book = new Book();

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        String result = bookService.deleteBook(id);

        assertEquals("Book successfully deleted!", result);
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, times(1)).delete(book);
    }
}
