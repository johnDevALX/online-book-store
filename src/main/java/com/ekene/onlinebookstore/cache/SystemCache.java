package com.ekene.onlinebookstore.cache;

import com.ekene.onlinebookstore.book.model.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.EvictionMode;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SystemCache {

    /**
     * Saves the recent books and patron to the cache
     * @Param
     *
     */

    private final RedissonClient redissonClient;

    @SneakyThrows
    public void addBook(Book book) {
        if (Objects.isNull(book)) {
            return;
        }
        getBookMap().setMaxSize(500, EvictionMode.LRU);
        getBookMap().put(book.getId(), book, 24, TimeUnit.HOURS);
    }


    @SneakyThrows
    public Book getBook(Long id) {
        return getBookMap().get(id);
    }
    @SneakyThrows
    public void deleteBook(Long id) {
        getBookMap().remove(id);
    }
    private RMapCache<Long, Book> getBookMap() {
        var bookCache = String.join(".", "online.book.store.recent.books");
        return this.redissonClient.getMapCache(bookCache);
    }
}
