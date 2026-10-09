package expenseTracker;

import java.time.LocalDate;

public abstract class Expense {

    private int id;
    private LocalDate date;
    private String category;
    private int amountRupees;
    private String note;

    public Expense(int id, LocalDate date, String category,
                   int amountRupees, String note) {

        this.id = id;
        this.date = date;
        this.category = category;
        this.amountRupees = amountRupees;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public int getAmountRupees() {
        return amountRupees;
    }

    public String getNote() {
        return note;
    }

    public abstract String getPaymentMode();
}