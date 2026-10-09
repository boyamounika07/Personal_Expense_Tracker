package expenseTracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ExpenseService {

    private ArrayList<Expense> expenses;
    private Budget budget;

    private String[] categories = {
        "Food",
        "Travel",
        "Study",
        "Other"
    };

    public ExpenseService(ArrayList<Expense> expenses, Budget budget) {
        this.expenses = expenses;
        this.budget = budget;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public Budget getBudget() {
        return budget;
    }

    public String[] getCategories() {
        return categories;
    }

    public List<Expense> filterByCategory(String category) {

        ArrayList<Expense> result = new ArrayList<>();

        for (Expense expense : expenses) {

            if (expense.getCategory().equalsIgnoreCase(category)) {
                result.add(expense);
            }
        }

        return result;
    }

    public long getTotalSpending() {

        long total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmountRupees();
        }

        return total;
    }

    public long[] getTotals() {

        long[] totals = new long[categories.length];

        for (Expense expense : expenses) {

            for (int i = 0; i < categories.length; i++) {

                if (categories[i].equals(expense.getCategory())) {
                    totals[i] += expense.getAmountRupees();
                }
            }
        }

        return totals;
    }

    public int getUsedCategoryCount() {

        HashSet<String> usedCategories = new HashSet<>();

        for (Expense expense : expenses) {
            usedCategories.add(expense.getCategory());
        }

        return usedCategories.size();
    }

    public void showSummary() {

        long total = getTotalSpending();
        long remaining = budget.getAmountRupees() - total;
        long[] totals = getTotals();

        System.out.println("\n========== SUMMARY ==========");

        System.out.println("Expense count: " + expenses.size());
        System.out.println("Total spending: " + total + " rupees");
        System.out.println("Budget: " + budget.getAmountRupees() + " rupees");

        if (remaining >= 0) {
            System.out.println("Remaining: " + remaining + " rupees");
        } else {
            System.out.println("Over budget by: " + (-remaining) + " rupees");
        }

        System.out.println("\nCategory Totals:");

        for (int i = 0; i < categories.length; i++) {
            System.out.println(categories[i] + ": " + totals[i]);
        }

        System.out.println("Categories used: " + getUsedCategoryCount());

        System.out.println("==============================");
    }
}