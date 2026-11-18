package view;

import controller.AccountController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Account;
import model.Customer;
import java.util.List;

public class DashboardView {
    private final Customer customer;
    private final AccountController controller = new AccountController();
    private final ListView<String> accountListView = new ListView<>();
    private List<Account> currentAccounts;

    // Receives the logged-in Customer from LoginView
    public DashboardView(Customer customer) {
        this.customer = customer;
    }

    // Builds and displays the main Customer Account Dashboard
    public void show(Stage stage) {
        // Top header bar with customer welcome message and Logout button
        Label welcome = new Label("Welcome, " + customer.getName() + " (ID: " + customer.getCustomerId() + ")");
        welcome.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> new LoginView().show(stage));

        HBox topBar = new HBox(20, welcome, logoutBtn);

        // Input and buttons for Deposits, Withdrawals, and Monthly Interest
        TextField amountField = new TextField();
        amountField.setPromptText("Amount (BWP)");

        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button interestBtn = new Button("Pay Monthly Interest");

        // Status label to show success messages (green) or validation errors (red)
        Label messageLabel = new Label();

        // Deposit button action
        depositBtn.setOnAction(e -> {
            try {
                Account selected = getSelectedAccount();
                controller.deposit(selected, amountField.getText());
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Deposited BWP " + amountField.getText() + " into " + selected.getAccountNumber());
                amountField.clear();
                refreshAccounts();
            } catch (Exception ex) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(ex.getMessage());
            }
        });

        // Withdraw button action
        withdrawBtn.setOnAction(e -> {
            try {
                Account selected = getSelectedAccount();
                controller.withdraw(selected, amountField.getText());
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Withdrew BWP " + amountField.getText() + " from " + selected.getAccountNumber());
                amountField.clear();
                refreshAccounts();
            } catch (Exception ex) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(ex.getMessage());
            }
        });

        // Pay Monthly Interest button action
        interestBtn.setOnAction(e -> {
            try {
                Account selected = getSelectedAccount();
                double earned = controller.applyMonthlyInterest(selected);
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(String.format("Paid BWP %.2f interest to %s", earned, selected.getAccountNumber()));
                refreshAccounts();
            } catch (Exception ex) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(ex.getMessage());
            }
        });

        HBox txControls = new HBox(10, amountField, depositBtn, withdrawBtn, interestBtn);

        // Controls for Opening a New Bank Account
        ComboBox<String> typeBox = new ComboBox<>(FXCollections.observableArrayList("Savings", "Investment", "Cheque"));
        typeBox.setValue("Savings");

        TextField newAccNum = new TextField();
        newAccNum.setPromptText("Account No (e.g., SA-9001)");

        TextField initDeposit = new TextField();
        initDeposit.setPromptText("Initial Deposit");

        TextField branchField = new TextField();
        branchField.setPromptText("Branch");

        TextField compNameField = new TextField();
        compNameField.setPromptText("Employer Name (Cheque)");

        TextField compAddrField = new TextField();
        compAddrField.setPromptText("Employer Address (Cheque)");

        Button openAccBtn = new Button("Open Account");
        openAccBtn.setOnAction(e -> {
            try {
                controller.openNewAccount(customer, typeBox.getValue(), newAccNum.getText(),
                        initDeposit.getText(), branchField.getText(), compNameField.getText(), compAddrField.getText());
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Opened new " + typeBox.getValue() + " account!");
                newAccNum.clear();
                initDeposit.clear();
                refreshAccounts();
            } catch (Exception ex) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(ex.getMessage());
            }
        });

        VBox openBox = new VBox(8, new Label("Open New Account:"),
                new HBox(8, typeBox, newAccNum, initDeposit, branchField),
                new HBox(8, compNameField, compAddrField, openAccBtn));

        // Combine all sections into the main vertical dashboard layout
        VBox root = new VBox(15, topBar, new Label("Your Accounts (Select an account below):"),
                accountListView, txControls, openBox, messageLabel);
        root.setPadding(new Insets(20));

        // Load the customer's accounts from SQLite when the screen opens
        refreshAccounts();

        stage.setTitle("Customer Account Dashboard");
        stage.setScene(new Scene(root, 740, 520));
        stage.show();
    }

    // Helper method to get whichever account the user clicked in the list
    private Account getSelectedAccount() {
        int idx = accountListView.getSelectionModel().getSelectedIndex();
        if (idx < 0 || currentAccounts == null || idx >= currentAccounts.size()) {
            throw new IllegalArgumentException("Please select an account from the list first.");
        }
        return currentAccounts.get(idx);
    }

    // Reloads the customer's latest account balances from the SQLite database
    private void refreshAccounts() {
        try {
            currentAccounts = controller.loadCustomerAccounts(customer.getCustomerId());
            accountListView.getItems().clear();
            for (Account acc : currentAccounts) {
                accountListView.getItems().add(String.format("%s | %s | Balance: BWP %.2f | Branch: %s",
                        acc.getAccountNumber(), acc.getClass().getSimpleName(), acc.getBalance(), acc.getBranch()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}