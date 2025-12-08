package library.repository;

import java.util.ArrayList;
import java.util.List;

import library.domain.User;

/**
 * Repository responsible for managing user records in the library system.
 * <p>
 * Provides basic CRUD-like operations for:
 * <ul>
 *     <li>Registering users</li>
 *     <li>Searching for users by ID</li>
 *     <li>Removing users</li>
 *     <li>Retrieving all registered users</li>
 * </ul>
 *
 * This class is primarily used in:
 * <ul>
 *     <li>Sprint 2 – Borrowing (adding users dynamically)</li>
 *     <li>Sprint 4 – User unregistration and validation</li>
 *     <li>Sprint 5 – CD borrowing (user lookups)</li>
 * </ul>
 *
 * It represents the persistence layer for the {@link User} domain entity.
 *
 * @author Lojain 
 * @version 1.0
 */
public class UserRepository {

    /** Internal collection holding all registered users. */
    private List<User> users = new ArrayList<>();

    /**
     * Adds a new user to the repository.
     *
     * @param user the {@link User} to add.
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Searches for a user by their unique ID.
     *
     * @param id the identifier of the user.
     * @return the matching {@link User}, or {@code null} if not found.
     */
    public User findById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes a user from the repository.
     *
     * @param user the {@link User} to remove.
     */
    public void removeUser(User user) {
        users.remove(user);
    }

    /**
     * Returns a list of all users currently registered.
     *
     * @return list of {@link User} objects.
     */
    public List<User> getAllUsers() {
        return users;
    }
}
