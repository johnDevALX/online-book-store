package com.ekene.onlinebookstore.utils;

import com.ekene.onlinebookstore.book.model.entity.Book;
import com.ekene.onlinebookstore.book.model.payload.BookDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonMapper {
    public static BookDto convertBookToDto(Book book){
        BookDto.BookDtoBuilder builder = BookDto.builder();

        builder.id(book.getId())
                .genre(book.getGenre())
                .isbn(book.getIsbn())
                .available(book.getAvailable())
                .description(book.getDescription())
                .title(book.getTitle())
                .inventory(book.getInventory())
                .language(book.getLanguage())
                .numberOfPages(book.getNumberOfPages())
                .publisher(book.getPublisher())
                .author(book.getAuthor())
                .rating(book.getRating())

                .publicationDate(book.getPublicationDate());

        return builder.build();
    }

    public static Book convertBookDtoToEntity(BookDto book){
        Book.BookBuilder builder = Book.builder();

        builder.genre(book.getGenre());
        builder.isbn(book.getIsbn());
        builder.available(book.getAvailable());
        builder.description(book.getDescription());
        builder.title(book.getTitle());
        builder.inventory(book.getInventory());
        builder.language(book.getLanguage());
        builder.numberOfPages(book.getNumberOfPages());
        builder.publisher(book.getPublisher());
        builder.rating(book.getRating());
        builder.author(book.getAuthor());
        builder.publicationDate(book.getPublicationDate());

        return builder.build();
    }

}
