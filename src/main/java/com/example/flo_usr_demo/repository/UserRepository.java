package com.example.flo_usr_demo.repository;

import com.example.flo_usr_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
