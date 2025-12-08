package library.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.communication.NotificationSender;
import library.domain.Loan;
import library.domain.User;
import library.repository.LoanRepository;

public class ReminderService {

    private LoanRepository loanRepo;
    private LoanService loanService;
    private NotificationSender sender;

    public ReminderService(LoanRepository loanRepo, LoanService loanService, NotificationSender sender) {
        this.loanRepo = loanRepo;
        this.loanService = loanService;
        this.sender = sender;
    }

    public void sendOverdueReminders() {

        List<Loan> allLoans = loanRepo.getAllLoans();
        Map<User, Integer> overdueCount = new HashMap<>();

        // أولاً: حساب عدد الكتب المتأخرة لكل مستخدم
        for (Loan loan : allLoans) {
            if (loanService.isOverdue(loan)) {
                overdueCount.put(
                    loan.getUser(),
                    overdueCount.getOrDefault(loan.getUser(), 0) + 1
                );
            }
        }

        // ثانياً: إرسال رسالة لكل مستخدم لديه تأخيرات
        for (Map.Entry<User, Integer> entry : overdueCount.entrySet()) {

            User user = entry.getKey();
            int count = entry.getValue();

            StringBuilder msg = new StringBuilder();
            msg.append("Dear ").append(user.getName()).append(",\n\n");
            msg.append("You have ").append(count).append(" overdue book(s):\n\n");

            // تفاصيل كل كتاب متأخر
            for (Loan loan : allLoans) {
                if (loan.getUser().getId().equals(user.getId()) && loanService.isOverdue(loan)) {

                    long overdueDays = loanService.getOverdueDays(loan);
                    int fine = loanService.calculateFine(loan);

                    msg.append("📚 Title: ").append(loan.getBook().getTitle()).append("\n");
                    msg.append("📅 Due Date: ").append(loan.getDueDate()).append("\n");
                    msg.append("⏳ Days Overdue: ").append(overdueDays).append("\n");
                    msg.append("💰 Current Fine: ").append(fine).append(" NIS\n");
                    msg.append("-------------------------------------\n");
                }
            }

            msg.append("\nPlease return the overdue items as soon as possible.\n");
            msg.append("Library System");

            // إرسال الرسالة إلى الإيميل
            sender.send(user.getEmail(), msg.toString());
        }
    }
}
