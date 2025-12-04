package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the User entity.
 *
 * This interface exposes CRUD operations for User objects,
 * through Spring Data JPA.
 *
 * Extending JpaRepository provides built-in methods such as:
 * - save()
 * - findAll()
 * - findById()
 * - delete()
 * - count()
 *
 * No custom methods are required for basic persistence,
 * but additional queries can be added if future features require them.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
