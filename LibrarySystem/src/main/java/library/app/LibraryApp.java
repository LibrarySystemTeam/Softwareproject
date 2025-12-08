package library.app;

import library.domain.Book;
import library.domain.CD;
import library.domain.Loan;
import library.domain.LoanCD;
import library.domain.User;

import library.repository.BookRepository;
import library.repository.CDRepository;
import library.repository.LoanRepository;
import library.repository.UserRepository;

import library.service.AdminService;
import library.service.BookService;
import library.service.LoanService;
import library.service.UserService;
import library.service.ReminderService;

import library.communication.EmailNotificationSender;

import java.util.List;
import java.util.Scanner;

/**
 * Main entry point for the Library Management System application.
 * <p>
 * This class represents the Presentation Layer (CLI-based UI) of the system.
 * It orchestrates interactions between users and service layers across all sprints.
 *
 * <b>Responsibilities:</b>
 *
 * <ul>
 *     <li>Display menus and handle console input</li>
 *     <li>Delegate operations to appropriate service classes</li>
 *     <li>Connect and initialize all repositories and services</li>
 * </ul>
 *
 *<h2>Features Implemented by Sprint:</h2>
 *
 *
 * <b>Sprint 1 – Basic Library Functions</b>
 *
 * <ul>
 *     <li>Admin login</li>
 *     <li>Add books</li>
 *     <li>Search books</li>
 * </ul>
 *
 *<b>Sprint 2 – Borrowing Logic</b>
 *
 * <ul>
 *     <li>Borrow book (28 days)</li>
 *     <li>View overdue books</li>
 *     <li>Calculate fines</li>
 *     <li>Pay fines</li>
 * </ul>
 *
 * <b>Sprint 3 – Notifications</b>
 * <ul>
 *     <li>Send email reminders using Observer Pattern</li>
 * </ul>
 *
 * <b>Sprint 4 – User Management</b>
 * <ul>
 *     <li>Unregister user with validation</li>
 *     <li>Active loan and fine restrictions</li>
 * </ul>
 *
 * <b>Sprint 5 – Media Extensions</b>
 * <ul>
 *     <li>Add CDs</li>
 *     <li>Borrow CDs (7 days)</li>
 *     <li>Calculate CD fines (20 NIS/day)</li>
 *     <li>Show overdue mixed media</li>
 * </ul>
 *
 * The class keeps running until the user selects the "Exit" option.
 *
 * @author Lojain
 *
 * @version 1.0
 */
/**
 * Default constructor for LibraryApp.
 * Initializes the application and prepares the main execution environment.
 */
public class LibraryApp {

    /**
     * Main program loop. Displays the UI menu and delegates user actions
     * to the corresponding service classes.
     *
     * @param args CLI arguments (not used).
     */
    // Default constructor (required for Javadoc clean output)

	
    public static void main(String[] args) {

     /*  Scanner sc = new Scanner(System.in);

        // Sprint 1
        AdminService adminService = new AdminService();
        BookRepository bookRepo = new BookRepository();
        BookService bookService = new BookService(bookRepo);

        // Sprint 2
        LoanRepository loanRepo = new LoanRepository();
        LoanService loanService = new LoanService(loanRepo);

        // Sprint 4
        UserRepository userRepo = new UserRepository();
        UserService userService = new UserService(userRepo, loanRepo, adminService);

        // Sprint 5
        CDRepository cdRepo = new CDRepository();

        System.out.println("=== Welcome to the Library System ===");

        while (true) {

            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Admin Login");
            System.out.println("2. Add Book");
            System.out.println("3. Search Book");
            System.out.println("4. Logout");
            System.out.println("5. Exit");

            
            System.out.println("6. Borrow Book");
            System.out.println("7. Show Overdue Items");
            System.out.println("8. Calculate Fine");
            System.out.println("9. Pay Fine");

            
            System.out.println("10. Send Reminder Emails");

            
            System.out.println("11. Unregister User");

          
            System.out.println("12. Add CD");
            System.out.println("13. Borrow CD");
            System.out.println("14. Register New User (Admin Only)");

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // ======================================================
                //  SPRINT 1
                // ======================================================
                case 1:
                    System.out.print("Username: ");
                    String u = sc.nextLine();
                    System.out.print("Password: ");
                    String p = sc.nextLine();
                    System.out.println(adminService.login(u, p) ? "Login successful!" : "Invalid credentials.");
                    break;

                case 2:
                    if (!adminService.isLoggedIn()) {
                        System.out.println("Please login as admin first.");
                        break;
                    }

                    System.out.print("Book Title: ");
                    String title = sc.nextLine();
                    System.out.print("Author: ");
                    String author = sc.nextLine();
                    System.out.print("ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.print("Quantity: ");
                    int qty = sc.nextInt();
                    sc.nextLine();

                    Book newBook = new Book(title, author, isbn);
                    newBook.setQuantity(qty);

                    bookRepo.add(newBook);

                    System.out.println("Book added with quantity = " + qty);
                    break;

                case 3:
                    System.out.print("Keyword: ");
                    String key = sc.nextLine();
                    List<Book> found = bookService.search(key);

                    if (found.isEmpty())
                        System.out.println("No books found.");
                    else
                        found.forEach(b -> System.out.println(
                                b.getTitle() + " | " + b.getAuthor() +
                                " | ISBN: " + b.getIsbn() +
                                " | Available: " +
                                (b.getQuantity() - b.getBorrowedCount()) +
                                " / " + b.getQuantity()
                        ));
                    break;

                case 4:
                    adminService.logout();
                    System.out.println("Logged out.");
                    break;

                case 5:
                    System.out.println("Goodbye!");
                    return;

                // ======================================================
                //  SPRINT 2
                // ======================================================
                case 6:
                	System.out.print("User Name: ");
                	String uname = sc.nextLine();

                	System.out.print("User ID: ");
                	String uid = sc.nextLine();

                	System.out.print("Email: ");
                	String email = sc.nextLine();

                	User user = new User(uname, uid, email);
                	userRepo.addUser(user);


                    System.out.print("Book ISBN: ");
                    String bIsbn = sc.nextLine();
                    Book book = bookRepo.getAll().stream()
                            .filter(b -> b.getIsbn().equals(bIsbn))
                            .findFirst()
                            .orElse(null);

                    if (book == null) {
                        System.out.println("Book not found.");
                        break;
                    }

                    try {
                        Loan loan = loanService.borrowBook(user, book);
                        System.out.println("Borrowed! Due: " + loan.getDueDate());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 7:
                    System.out.println("--- Overdue Items (Books + CDs) ---");

                    loanService.getOverdueBookLoans().forEach(l ->
                            System.out.println("BOOK: " + l.getBook().getTitle() +
                            		" |ID: " + l.getUser().getId()+
                                    " | User: " + l.getUser().getName() +
                                    " | Due: " + l.getDueDate())
                    );

                    loanService.getOverdueCDLoans().forEach(l ->
                            System.out.println("CD: " + l.getCD().getTitle() +
                                    " | User: " + l.getUser().getName() +
                                    " | Due: " + l.getDueDate())
                    );
                    break;

                case 8:
                    System.out.print("Media ID (ISBN or CD-ID): ");
                    String mid = sc.nextLine();

                    // ============================
                    // HANDLE BOOK FINES (ALL LOANS)
                    // ============================
                    List<Loan> bookLoans = loanRepo.getAllBookLoans().stream()
                            .filter(l -> l.getBook().getIsbn().equals(mid))
                            .toList();

                    if (!bookLoans.isEmpty()) {

                        for (Loan l : bookLoans) {
                            int fine = loanService.calculateFine(l);
                            long overdueDays = loanService.getOverdueDays(l);

                            System.out.println("\n--- Fine Details (BOOK) ---");
                            System.out.println("User: " + l.getUser().getName() + " (ID: " + l.getUser().getId() + ")");
                            System.out.println("Title: " + l.getBook().getTitle());
                            System.out.println("Due Date: " + l.getDueDate());
                            System.out.println("Overdue Days: " + overdueDays);
                            System.out.println("Fine Amount: " + fine + " NIS");
                            System.out.println("Note: You must pay the full fine to regain borrowing rights.");
                            System.out.println("---------------------------------------");
                        }
                        break; // stop here
                    }

                    // ============================
                    // HANDLE CD FINES
                    // ============================
                    List<LoanCD> cdLoans = loanRepo.getAllCDLoans().stream()
                            .filter(l -> l.getCD().getId().equals(mid))
                            .toList();

                    if (!cdLoans.isEmpty()) {

                        for (LoanCD l : cdLoans) {
                            int fine = loanService.calculateFine(l);
                            long overdueDays = loanService.getOverdueDays(l);

                            System.out.println("\n--- Fine Details (CD) ---");
                            System.out.println("User: " + l.getUser().getName() + " (ID: " + l.getUser().getId() + ")");
                            System.out.println("Title: " + l.getCD().getTitle());
                            System.out.println("Due Date: " + l.getDueDate());
                            System.out.println("Overdue Days: " + overdueDays);
                            System.out.println("Fine Amount: " + fine + " NIS");
                            System.out.println("---------------------------------------");
                        }
                        break; // stop here
                    }

                    // no book and no CD found
                    System.out.println("No loan found.");
                    break;


                case 9:
                    System.out.print("User ID: ");
                    String payId = sc.nextLine();
                    User payUser = userRepo.findById(payId);

                    if (payUser == null) {
                        System.out.println("User not found.");
                        break;
                    }

                    System.out.print("Amount: ");
                    int amt = sc.nextInt();
                    sc.nextLine();

                    // تحديث رصيد الغرامة فقط بالدفع
                    loanService.payFine(payUser, amt);

                    System.out.println("Fine updated.");
                    break;


                // ======================================================
                //  SPRINT 3
                // ======================================================
                case 10:
                    System.out.print("Enter User ID to send reminder: ");
                    String rid = sc.nextLine();
                    User target = userRepo.findById(rid);

                    if (target == null) {
                        System.out.println("User not found.");
                        break;
                    }

                    // Get all overdue loans for this user
                    List<Loan> overdueLoans = loanRepo.getAllBookLoans().stream()
                            .filter(l -> l.getUser().getId().equals(target.getId()) && loanService.isOverdue(l))
                            .toList();

                    if (overdueLoans.isEmpty()) {
                        System.out.println("This user has no overdue items.");
                        break;
                    }

                    // ----- Build detailed email message -----
                    StringBuilder msg2 = new StringBuilder();
                    msg2.append("Dear ").append(target.getName()).append(",\n\n");
                    msg2.append("You have ").append(overdueLoans.size()).append(" overdue book(s):\n\n");

                    for (Loan loan : overdueLoans) {
                        long overdueDays = loanService.getOverdueDays(loan);
                        int fine = loanService.calculateFine(loan);

                        msg2.append("📚 Title: ").append(loan.getBook().getTitle()).append("\n");
                        msg2.append("📅 Due Date: ").append(loan.getDueDate()).append("\n");
                        msg2.append("⏳ Days Overdue: ").append(overdueDays).append("\n");
                        msg2.append("💰 Current Fine: ").append(fine).append(" NIS\n");
                        msg2.append("---------------------------------------\n");
                    }

                    msg2.append("\nPlease return the overdue items as soon as possible.\n");
                    msg2.append("Library System");

                    // ----- Send Email -----
                    EmailNotificationSender sender2 = new EmailNotificationSender();
                    sender2.send(target.getEmail(), msg2.toString());

                    System.out.println("Reminder email sent.");

                    break;



                // ======================================================
                //  SPRINT 4
                // ======================================================
                case 11:
                    System.out.println("=== Registered Users ===");
                    userRepo.getAllUsers().forEach(uobj ->
                        System.out.println(uobj.getName() + " | ID: " + uobj.getId() +
                                          " | Fines: " + uobj.getFineBalance())
                    );

                    System.out.print("User ID to remove: ");
                    String delId = sc.nextLine();

                    try {
                        userService.unregisterUser(delId);
                        System.out.println("User removed.");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                // ======================================================
                //  SPRINT 5
                // ======================================================
                case 12:
                    if (!adminService.isLoggedIn()) {
                        System.out.println("Admin login required.");
                        break;
                    }
                    System.out.print("CD Title: ");
                    String ct = sc.nextLine();
                    System.out.print("Artist: ");
                    String ca = sc.nextLine();
                    System.out.print("CD ID: ");
                    String cid = sc.nextLine();

                    CD newCD = new CD(ct, ca, cid);
                    cdRepo.add(newCD);
                    System.out.println("CD added.");
                    break;

                case 13:
                	System.out.print("User Name: ");
                	String ucdName = sc.nextLine();

                	System.out.print("User ID: ");
                	String ucdId = sc.nextLine();

                	System.out.print("Email: ");
                	String ucdEmail = sc.nextLine();

                	User uCD = new User(ucdName, ucdId, ucdEmail);
                	userRepo.addUser(uCD);


                    System.out.print("CD ID: ");
                    String cdID = sc.nextLine();

                    CD cdToBorrow = cdRepo.findById(cdID);
                    if (cdToBorrow == null) {
                        System.out.println("CD not found.");
                        break;
                    }

                    try {
                        LoanCD loanCD = loanService.borrowCD(uCD, cdToBorrow);
                        System.out.println("Borrowed CD! Due: " + loanCD.getDueDate());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 14:
                    if (!adminService.isLoggedIn()) {
                        System.out.println("Admin login required.");
                        break;
                    }

                    System.out.print("User Name: ");
                    String newName = sc.nextLine();

                    System.out.print("User ID: ");
                    String newId = sc.nextLine();

                    System.out.print("Email: ");
                    String newEmail = sc.nextLine();

                    User newUser = new User(newName, newId, newEmail);

                    userRepo.addUser(newUser);

                    System.out.println("User registered successfully.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }*/
    }
}
