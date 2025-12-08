package library.repository;

import java.util.ArrayList;
import java.util.List;

import library.domain.CD;

/**
 * Repository responsible for storing and managing {@link CD} entities.
 * <p>
 * This class provides persistence-like operations used in:
 * <ul>
 *     <li>Sprint 5 – CD media extension</li>
 *     <li>Borrowing CDs and tracking availability</li>
 * </ul>
 *
 * Functions provided:
 * <ul>
 *     <li>Add new CDs</li>
 *     <li>Find CDs by their unique ID</li>
 *     <li>Retrieve a list of all CDs</li>
 * </ul>
 *
 * This repository acts as an in-memory storage mechanism,
 * similar to BookRepository and UserRepository.
 *
 *@author Lojain
 * @version 1.0
 */
public class CDRepository {

    /** Internal list storing all CD objects. */
    private List<CD> cds = new ArrayList<>();

    /**
     * Adds a new CD to the repository.
     *
     * @param cd the {@link CD} instance to add.
     */
    public void add(CD cd) {
        cds.add(cd);
    }

    /**
     * Finds a CD by its unique identifier.
     *
     * @param id the ID of the CD.
     * @return the matching {@link CD}, or {@code null} if no match is found.
     */
    public CD findById(String id) {
        return cds.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns all CDs stored in the repository.
     *
     * @return list of {@link CD} objects.
     */
    public List<CD> getAll() {
        return cds;
    }
}
