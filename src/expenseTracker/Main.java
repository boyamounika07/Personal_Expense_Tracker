package expenseTracker;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    static FileManager fileManager;
    static ExpenseService service;

    static int nextId = 1;

    public static void main(String[] args) {

        // Create FileManager
        fileManager = new FileManager();

        // Load existing expenses
        ArrayList<Expense> expenses =
                fileManager.loadExpenses();

        // Load budget
        int budgetAmount =
                fileManager.loadBudget();

        Budget budget =
                new Budget(budgetAmount);

        // Create service
        service =
                new ExpenseService(expenses, budget);

        // Find next expense ID
        findNextId(expenses);

        System.out.println(
                "===== PERSONAL EXPENSE TRACKER =====");

        showMenu();

        scanner.close();
    }

    // Find next ID
    static void findNextId(ArrayList<Expense> expenses) {

        int maxId = 0;

        for (Expense expense : expenses) {

            if (expense.getId() > maxId) {
                maxId = expense.getId();
            }
        }

        nextId = maxId + 1;
    }

    // Main menu
    static void showMenu() {

        while (true) {

            System.out.println("\n========== MENU ==========");
            System.out.println("1. Add expense");
            System.out.println("2. View all expenses");
            System.out.println("3. View expenses by category");
            System.out.println("4. Set budget");
            System.out.println("5. View summary");
            System.out.println("6. Save report");
            System.out.println("0. Exit");
            System.out.println("==========================");

            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    addExpense();
                    break;

                case "2":
                    viewAllExpenses();
                    break;

                case "3":
                    viewByCategory();
                    break;

                case "4":
                    setBudget();
                    break;

                case "5":
                    service.showSummary();
                    break;

                case "6":
                    saveReport();
                    break;

                case "0":
                    System.out.println(
                            "Thank you for using Expense Tracker!");
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please try again.");
            }
        }
    }

    // Add expense
    static void addExpense() {

        System.out.println("\n===== ADD EXPENSE =====");

        LocalDate date = readDate();

        String category = readCategory();

        int amount = readAmount();

        String paymentMode = readPaymentMode();

        String note = readNote();

        Expense expense;

        if (paymentMode.equals("CASH")) {

            expense = new CashExpense(
                    nextId,
                    date,
                    category,
                    amount,
                    note
            );

        } else {

            expense = new OnlineExpense(
                    nextId,
                    date,
                    category,
                    amount,
                    note
            );
        }

        service.addExpense(expense);

        fileManager.saveExpenses(
                service.getExpenses()
        );

        System.out.println(
                "Expense " + nextId + " saved.");

        nextId++;
    }

    // Read date
    static LocalDate readDate() {

        while (true) {

            System.out.print(
                    "Date (yyyy-MM-dd): ");

            String input = scanner.nextLine();

            try {

                return LocalDate.parse(input);

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Try again.");
            }
        }
    }

    // Read category
    static String readCategory() {

        while (true) {

            System.out.println("1. Food");
            System.out.println("2. Travel");
            System.out.println("3. Study");
            System.out.println("4. Other");

            System.out.print("Choose category: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    return "Food";

                case "2":
                    return "Travel";

                case "3":
                    return "Study";

                case "4":
                    return "Other";

                default:
                    System.out.println(
                            "Invalid category.");
            }
        }
    }

    // Read amount
    static int readAmount() {

        while (true) {

            System.out.print(
                    "Amount in rupees: ");

            String input = scanner.nextLine();

            try {

                int amount =
                        Integer.parseInt(input);

                if (amount >= 1 &&
                    amount <= 1000000) {

                    return amount;
                }

                System.out.println(
                        "Amount must be between 1 and 1000000.");

            } catch (NumberFormatException e) {

                System.out.println(
                        "Enter whole number only.");
            }
        }
    }

    // Read payment
    static String readPaymentMode() {

        while (true) {

            System.out.println("1. Cash");
            System.out.println("2. Online");

            System.out.print("Choose payment: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                return "CASH";
            }

            if (choice.equals("2")) {
                return "ONLINE";
            }

            System.out.println(
                    "Invalid payment choice.");
        }
    }

    // Read note
    static String readNote() {

        while (true) {

            System.out.print("Short note: ");

            String note =
                    scanner.nextLine().trim();

            if (!note.isEmpty() &&
                !note.contains("|")) {

                return note;
            }

            System.out.println(
                    "Note cannot be empty or contain |.");
        }
    }

    // View all expenses
    static void viewAllExpenses() {

        ArrayList<Expense> expenses =
                service.getExpenses();

        if (expenses.isEmpty()) {

            System.out.println(
                    "No records found.");

            return;
        }

        for (Expense expense : expenses) {

            printExpense(expense);
        }
    }

    // View by category
    static void viewByCategory() {

        String category = readCategory();

        List<Expense> result =
                service.filterByCategory(category);

        if (result.isEmpty()) {

            System.out.println(
                    "No records found.");

            return;
        }

        for (Expense expense : result) {

            printExpense(expense);
        }
    }

    // Display expense
    static void printExpense(Expense expense) {

        System.out.println("-----------------------------");

        System.out.println(
                "ID: " + expense.getId());

        System.out.println(
                "Date: " + expense.getDate());

        System.out.println(
                "Category: " + expense.getCategory());

        System.out.println(
                "Amount: " +
                expense.getAmountRupees() +
                " rupees");

        System.out.println(
                "Payment: " +
                expense.getPaymentMode());

        System.out.println(
                "Note: " +
                expense.getNote());

        System.out.println("-----------------------------");
    }

    // Set budget
    static void setBudget() {

        System.out.print(
                "Enter new budget: ");

        int amount =
                Integer.parseInt(scanner.nextLine());

        service.getBudget()
                .setAmountRupees(amount);

        fileManager.saveBudget(amount);

        System.out.println(
                "Budget saved successfully.");
    }

    // Save report
    static void saveReport() {

        long[] totals =
                service.getTotals();

        long total =
                service.getTotalSpending();

        long budget =
                service.getBudget()
                        .getAmountRupees();

        String[] categories =
                service.getCategories();

        StringBuilder report =
                new StringBuilder();

        report.append(
                "PERSONAL EXPENSE TRACKER REPORT\n");

        report.append(
                "Expense count: " +
                service.getExpenses().size() +
                "\n");

        report.append(
                "Total spending: " +
                total +
                " rupees\n");

        report.append(
                "Budget: " +
                budget +
                " rupees\n");

        for (int i = 0;
             i < categories.length;
             i++) {

            report.append(
                    categories[i] +
                    ": " +
                    totals[i] +
                    " rupees\n");
        }

        report.append(
                "Categories used: " +
                service.getUsedCategoryCount());

        fileManager.saveReport(
                report.toString());

        System.out.println(
                "Report saved successfully.");
    }
}