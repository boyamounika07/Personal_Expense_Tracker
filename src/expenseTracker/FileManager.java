package expenseTracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileManager {

    private static final Path DATA_FOLDER =
            Paths.get("data");

    private static final Path EXPENSE_FILE =
            DATA_FOLDER.resolve("expenses.txt");

    private static final Path BUDGET_FILE =
            DATA_FOLDER.resolve("budget.txt");

    private static final Path REPORT_FILE =
            DATA_FOLDER.resolve("report.txt");

    public FileManager() {

        try {
            Files.createDirectories(DATA_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(
                "Could not create data folder.", e
            );
        }
    }

    public ArrayList<Expense> loadExpenses() {

        ArrayList<Expense> expenses = new ArrayList<>();

        if (!Files.exists(EXPENSE_FILE)) {
            return expenses;
        }

        try (BufferedReader reader =
                Files.newBufferedReader(
                    EXPENSE_FILE,
                    StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split("\\|", -1);

                if (fields.length != 6) {
                    throw new RuntimeException(
                        "Invalid expense record: " + line
                    );
                }

                int id = Integer.parseInt(fields[0]);
                LocalDate date = LocalDate.parse(fields[1]);
                String category = fields[2];
                int amount = Integer.parseInt(fields[3]);
                String paymentMode = fields[4];
                String note = fields[5];

                Expense expense;

                if (paymentMode.equals("CASH")) {

                    expense = new CashExpense(
                        id, date, category, amount, note
                    );

                } else if (paymentMode.equals("ONLINE")) {

                    expense = new OnlineExpense(
                        id, date, category, amount, note
                    );

                } else {

                    throw new RuntimeException(
                        "Invalid payment mode: " + paymentMode
                    );
                }

                expenses.add(expense);
            }

        } catch (IOException | NumberFormatException e) {

            throw new RuntimeException(
                "Error reading expenses.txt", e
            );
        }

        return expenses;
    }

    public void saveExpenses(ArrayList<Expense> expenses) {

        Path tempFile =
                DATA_FOLDER.resolve("expenses.tmp");

        try (BufferedWriter writer =
                Files.newBufferedWriter(
                    tempFile,
                    StandardCharsets.UTF_8)) {

            for (Expense expense : expenses) {

                writer.write(
                    expense.getId() + "|" +
                    expense.getDate() + "|" +
                    expense.getCategory() + "|" +
                    expense.getAmountRupees() + "|" +
                    expense.getPaymentMode() + "|" +
                    expense.getNote()
                );

                writer.newLine();
            }

        } catch (IOException e) {

            throw new RuntimeException(
                "Error saving expenses.", e
            );
        }

        try {

            Files.move(
                tempFile,
                EXPENSE_FILE,
                StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                "Could not replace expenses file.", e
            );
        }
    }

    public int loadBudget() {

        if (!Files.exists(BUDGET_FILE)) {
            return 2000;
        }

        try (BufferedReader reader =
                Files.newBufferedReader(
                    BUDGET_FILE,
                    StandardCharsets.UTF_8)) {

            String line = reader.readLine();

            if (line == null || line.trim().isEmpty()) {
                return 2000;
            }

            return Integer.parseInt(line.trim());

        } catch (IOException | NumberFormatException e) {

            throw new RuntimeException(
                "Error reading budget.txt", e
            );
        }
    }

    public void saveBudget(int amount) {

        Path tempFile =
                DATA_FOLDER.resolve("budget.tmp");

        try (BufferedWriter writer =
                Files.newBufferedWriter(
                    tempFile,
                    StandardCharsets.UTF_8)) {

            writer.write(String.valueOf(amount));

        } catch (IOException e) {

            throw new RuntimeException(
                "Error saving budget.", e
            );
        }

        try {

            Files.move(
                tempFile,
                BUDGET_FILE,
                StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                "Could not replace budget file.", e
            );
        }
    }

    public void saveReport(String report) {

        try (BufferedWriter writer =
                Files.newBufferedWriter(
                    REPORT_FILE,
                    StandardCharsets.UTF_8)) {

            writer.write(report);

        } catch (IOException e) {

            throw new RuntimeException(
                "Error saving report.", e
            );
        }
    }
}