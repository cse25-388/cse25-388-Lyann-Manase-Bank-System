package view;

import controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;

public class LoginView {
    // Connects the login screen to LoginController (no business logic in the View)
    private final LoginController controller = new LoginController();

    // Builds and displays the Login window
    public void show(Stage stage) {
        // Screen heading
        Label title = new Label("Botswana Banking System - Login");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Input box where the customer types their Customer ID
        TextField idField = new TextField();
        idField.setPromptText("Customer ID (e.g., 101)");

        // Masked input box where the customer types their PIN
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN (e.g., 1234)");

        // Red text label that displays an error if login fails
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        // Login button sends the typed ID and PIN to LoginController
        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> {
            try {
                // Ask LoginController to verify the ID and PIN in SQLite
                Customer customer = controller.login(idField.getText(), pinField.getText());
                
                // If login succeeds, open the Customer Account Dashboard screen
                new DashboardView(customer).show(stage);
            } catch (Exception ex) {
                // Display the error message returned by LoginController
                statusLabel.setText(ex.getMessage());
            }
        });

        // Stack all UI elements vertically with 12px spacing and 25px outer padding
        VBox layout = new VBox(12, title, idField, pinField, loginBtn, statusLabel);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);

        // Set up and show the window
        stage.setTitle("Banking System Login");
        stage.setScene(new Scene(layout, 360, 260));
        stage.show();
    }
}
