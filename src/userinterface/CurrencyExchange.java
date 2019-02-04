package userinterface;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CurrencyExchange extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/userinterface/layout.fxml"));
        BorderPane pane = loader.load();
        Scene scene = new Scene(pane, 587, 390);
        InterfaceController mainController = loader.getController();
        mainController.setStage(primaryStage);
        primaryStage.setTitle("Currency Exchange");
        primaryStage.getIcons().add(new Image("userinterface/logo.png"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
