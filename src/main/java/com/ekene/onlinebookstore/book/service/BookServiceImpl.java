package com.ekene.onlinebookstore.book.service;

import com.ekene.onlinebookstore.cache.SystemCache;
import com.ekene.onlinebookstore.exception.BookNotAvailableException;
import com.ekene.onlinebookstore.exception.BookNotFound;
import com.ekene.onlinebookstore.book.model.entity.Book;
import com.ekene.onlinebookstore.book.model.payload.BookDto;
import com.ekene.onlinebookstore.book.repository.BookRepository;
import com.ekene.onlinebookstore.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final SystemCache systemCache;

    @Override
    public BookDto saveBook(BookDto bookDto) {
        Book saved = bookRepository.save(JsonMapper.convertBookDtoToEntity(bookDto));
        systemCache.addBook(saved);
        return JsonMapper.convertBookToDto(saved);
    }

    @Override
    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFound("Book not found with id: " + id));


        existingBook.setTitle(bookDto.getTitle() != null ? bookDto.getTitle() : existingBook.getTitle());
        existingBook.setPublisher(bookDto.getPublisher() != null ? bookDto.getPublisher() : existingBook.getPublisher());
        existingBook.setIsbn(bookDto.getIsbn() != null ? bookDto.getIsbn() : existingBook.getIsbn());
        existingBook.setPublicationDate(bookDto.getPublicationDate() != null ? bookDto.getPublicationDate() : existingBook.getPublicationDate());
        existingBook.setGenre(bookDto.getGenre() != null ? bookDto.getGenre() : existingBook.getGenre());
        existingBook.setLanguage(bookDto.getLanguage() != null ? bookDto.getLanguage() : existingBook.getLanguage());
        existingBook.setNumberOfPages(bookDto.getNumberOfPages() != null ? bookDto.getNumberOfPages() : existingBook.getNumberOfPages());
        existingBook.setDescription(bookDto.getDescription() != null ? bookDto.getDescription() : existingBook.getDescription());
        existingBook.setRating(bookDto.getRating() != null ? bookDto.getRating() : existingBook.getRating());
        existingBook.setAuthor(bookDto.getAuthor().isEmpty() ? existingBook.getAuthor() : bookDto.getAuthor()
        );
        existingBook.setAvailable(bookDto.getAvailable() != null ? bookDto.getAvailable() : existingBook.getAvailable());
        existingBook.setInventory(bookDto.getInventory() != null ? bookDto.getInventory() : existingBook.getInventory());

        systemCache.addBook(existingBook);
        Book savedBook = bookRepository.save(existingBook);
        return JsonMapper.convertBookToDto(savedBook);
    }

    @Override
    public BookDto getBook(Long id) {
        Book book = systemCache.getBook(id);
        if (Objects.isNull(book)){
            book = bookRepository.findById(id).orElseThrow(BookNotAvailableException::new);
        }
        return JsonMapper.convertBookToDto(book);
    }

    @Override
    public Page<BookDto> getAvailableBooks(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> all = bookRepository.findAllByAvailableTrue(pageRequest);

        List<BookDto> bookDtos = all.getContent()
                .stream()
                .map(JsonMapper::convertBookToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookDtos, pageRequest, all.getTotalElements());
    }

    @Override
    public String deleteBook(Long id) {
        systemCache.deleteBook(id);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFound::new);
        bookRepository.delete(book);
        return "Book successfully deleted!";
    }
}
