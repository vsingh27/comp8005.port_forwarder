package portfwd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("portfwd.fxml"));
        primaryStage.setTitle("Port Forwarder");
        primaryStage.setScene(new Scene(root, 730.0D, 492.0D));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
