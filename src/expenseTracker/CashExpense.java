package expenseTracker;

import java.time.LocalDate;

public class CashExpense extends Expense {

    public CashExpense(int id, LocalDate date, String category,
                       int amountRupees, String note) {

        super(id, date, category, amountRupees, note);
    }

    @Override
    public String getPaymentMode() {
        return "CASH";
    }
}