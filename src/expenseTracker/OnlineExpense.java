package expenseTracker;

import java.time.LocalDate;

public class OnlineExpense extends Expense {

    public OnlineExpense(int id, LocalDate date, String category,
                         int amountRupees, String note) {

        super(id, date, category, amountRupees, note);
    }

    @Override
    public String getPaymentMode() {
        return "ONLINE";
    }
}