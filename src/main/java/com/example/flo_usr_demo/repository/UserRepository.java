package com.example.flo_usr_demo.repository;

import com.example.flo_usr_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for {@link User} entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find users by first name containing the given fragment (case-insensitive).
     *
     * @param firstName the fragment of the first name to search for
     * @return list of users matching the criteria
     */
    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Find users by last name containing the given fragment (case-insensitive).
     *
     * @param lastName the fragment of the last name to search for
     * @return list of users matching the criteria
     */
    List<User> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Find users by first name and last name fragments (both case-insensitive).
     *
     * @param firstName the fragment of the first name to search for
     * @param lastName  the fragment of the last name to search for
     * @return list of users matching the criteria
     */
    List<User> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    /**
     * Find all users whose email ends with the specified domain (case-insensitive).
     * Uses native SQL for direct database access.
     *
     * @param domain the email domain to search for, e.g. "@gmail.com"
     * @return list of users whose email ends with the given domain
     */
    @Query(value = "SELECT * FROM users WHERE email ILIKE %:domain", nativeQuery = true)
    List<User> findByEmailDomain(@Param("domain") String domain);
}
