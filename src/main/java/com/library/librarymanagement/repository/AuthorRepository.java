package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the Author entity.
 * <p>
 * This interface provides CRUD operations for Author objects,
 * leveraging Spring Data JPA.
 * <p>
 * Extending JpaRepository automatically exposes:
 * - save(), findById(), findAll(), delete(), etc.
 * <p>
 * No additional methods are required for basic CRUD,
 * but they can be added here if needed in the future.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
