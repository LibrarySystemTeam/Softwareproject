package library.service;

import library.domain.User;
import library.repository.LoanRepository;
import library.repository.UserRepository;

/**
 * Service responsible for managing user-related operations such as:
 * <ul>
 *     <li>Unregistering users (Sprint 4 requirement)</li>
 *     <li>Checking borrowing restrictions related to user accounts</li>
 * </ul>
 *
 * The {@code UserService} enforces all business rules associated with
 * unregistration, including admin authentication, active loan checks,
 * and fine balance validation.
 *
 * This class plays a key role in:
 * <ul>
 *     <li>Sprint 4 – User Management</li>
 *     <li>Borrowing Restrictions workflow in Sprint 2 &amp; 4</li>

 * </ul>
 *
 * @author Lojain 
 * @version 1.0
 */
public class UserService {

    private UserRepository userRepo;
    private LoanRepository loanRepo;
    private AdminService adminService;

    /**
     * Constructs a UserService with required repositories and admin service.
     *
     * @param userRepo Repository for storing user data.
     * @param loanRepo Repository for accessing user loans.
     * @param adminService Service handling admin authentication.
     */
    public UserService(UserRepository userRepo, LoanRepository loanRepo, AdminService adminService) {
        this.userRepo = userRepo;
        this.loanRepo = loanRepo;
        this.adminService = adminService;
    }

    /**
     * Unregisters a user from the system after validating all business rules.
     * <p>
     * Unregistration rules (Sprint 4):
     * <ul>
     *     <li>Only admins may unregister users.</li>
     *     <li>User must exist in the system.</li>
     *     <li>User must have no active loans.</li>
     *     <li>User must have no unpaid fines.</li>
     * </ul>
     *
     * @param userId ID of the user to unregister.
     * @throws IllegalStateException if:
     *         <ul>
     *             <li>Admin is not logged in</li>
     *             <li>User does not exist</li>
     *             <li>User has active loans</li>
     *             <li>User has unpaid fines</li>
     *         </ul>
     */
    public void unregisterUser(String userId) {

        if (!adminService.isLoggedIn()) {
            throw new IllegalStateException("Only admins can unregister users.");
        }

        User user = userRepo.findById(userId);
        if (user == null) {
            throw new IllegalStateException("User not found.");
        }

        boolean hasActiveLoan = loanRepo.getAllLoans().stream()
                .anyMatch(l -> l.getUser().getId().equals(userId));

        if (hasActiveLoan) {
            throw new IllegalStateException("User has active loans.");
        }

        if (user.getFineBalance() > 0) {
            throw new IllegalStateException("User has unpaid fines.");
        }

        userRepo.removeUser(user);
    }
}
