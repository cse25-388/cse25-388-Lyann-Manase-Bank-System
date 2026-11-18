import dao.AccountDAO;
import dao.DatabaseConnection;
import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;

public class BankApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // 1. Create SQLite tables if they don't exist yet
        DatabaseConnection.initializeDatabase();

        // 2. Insert 13 sample records (5 customers + 8 accounts) on first launch
        AccountDAO.seedSampleData();

        // 3. Open the JavaFX Login screen
        new LoginView().show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}