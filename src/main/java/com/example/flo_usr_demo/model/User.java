package com.example.flo_usr_demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

/**
 * Using Javadoc to generate automatic documentation.
 * Represents a user in the application.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    /**
     * Default no-arg constructor
     */
    public User() {
    }

    /**
     * All-args constructor
     *
     * @param id         the unique identifier
     * @param firstName  user's first name
     * @param lastName   user's last name
     * @param email      user's email address
     * @param address    user's postal address
     */
    public User(Long id, String firstName, String lastName, String email, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    // ----------------------------------------------------------------
    // Getters and setters
    // ----------------------------------------------------------------

    /** @return the user’s ID */
    public Long getId() {
        return id;
    }

    /** @param id the user’s ID to set */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return the user’s first name */
    public String getFirstName() {
        return firstName;
    }

    /** @param firstName the user’s first name to set */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** @return the user’s last name */
    public String getLastName() {
        return lastName;
    }

    /** @param lastName the user’s last name to set */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** @return the user’s email */
    public String getEmail() {
        return email;
    }

    /** @param email the user’s email to set */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return the user’s address */
    public String getAddress() {
        return address;
    }

    /** @param address the user’s address to set */
    public void setAddress(String address) {
        this.address = address;
    }

    // ----------------------------------------------------------------
    // equals, hashCode, toString
    // ----------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User other = (User) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
