package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the Book entity.
 * <p>
 * This interface provides access to CRUD operations for Book objects,
 * leveraging Spring Data JPA functionalities.
 * <p>
 * By extending JpaRepository, we automatically gain methods such as:
 * - save()
 * - findById()
 * - findAll()
 * - delete()
 * - and others
 * <p>
 * Future custom queries can be defined here, e.g.:
 * List<Book> findByAuthor(String author);
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}
