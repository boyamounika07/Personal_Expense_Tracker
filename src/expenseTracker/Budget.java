package expenseTracker;

public class Budget {

    private int amountRupees;

    public Budget(int amountRupees) {
        setAmountRupees(amountRupees);
    }

    public int getAmountRupees() {
        return amountRupees;
    }

    public void setAmountRupees(int amountRupees) {

        if (amountRupees < 1 || amountRupees > 1000000) {
            throw new IllegalArgumentException(
                "Budget must be between 1 and 1000000 rupees."
            );
        }

        this.amountRupees = amountRupees;
    }
}