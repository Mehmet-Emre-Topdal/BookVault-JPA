package com.example.bookVault.specification;

import com.example.bookVault.entity.Author;
import com.example.bookVault.entity.Book;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> titleContains(String title) {
        return (root, query, cb) ->
            title == null ? null : cb.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Book> authorNameEquals(String authorName) {
        return (root, query, cb) -> {
            if (authorName == null) return null;
            Join<Book, Author> author = root.join("author", JoinType.LEFT);
            return cb.equal(author.get("name"), authorName);
        };
    }

    public static Specification<Book> minPageCount(Integer minPage) {
        return (root, query, cb) ->
            minPage == null ? null : cb.greaterThanOrEqualTo(root.get("pageCount"), minPage);
    }
}
