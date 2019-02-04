package userinterface;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorWindow {

    private StringBuilder errorMessage;

    public ErrorWindow() {
        errorMessage = new StringBuilder("W pliku wystąpiły następujące błędy:\n");
    }

    /**
     * Method displays new window with error message/s
     */
    public void display() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Error");
        window.getIcons().add(new Image("userinterface/error.png"));

        Label errorLabel = buildLabel();
        Button closeButton = buildButton(window);
        ImageView imageView = buildImageView();

        AnchorPane layout = new AnchorPane();
        layout.setPrefSize(400, 400);
        layout.getChildren().addAll(errorLabel, closeButton, imageView);

        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }

    /**
     * Method adds error message to StringBuilder for further display
     *
     * @param error message to append
     */
    public void append(String error) {
        errorMessage.append(error);
    }

    private Label buildLabel() {
        Label errorLabel = new Label();
        errorLabel.setText(errorMessage.toString());
        errorLabel.setMaxWidth(430);
        errorLabel.setMaxHeight(500);
        errorLabel.setWrapText(true);
        errorLabel.setAlignment(Pos.TOP_LEFT);
        AnchorPane.setTopAnchor(errorLabel, 25.0);
        AnchorPane.setLeftAnchor(errorLabel, 40.0);
        AnchorPane.setRightAnchor(errorLabel, 25.0);
        return errorLabel;
    }

    private Button buildButton(Stage window) {
        Button closeButton = new Button("Close");
        AnchorPane.setLeftAnchor(closeButton, 140.0);
        AnchorPane.setRightAnchor(closeButton, 140.0);
        AnchorPane.setBottomAnchor(closeButton, 25.0);
        closeButton.setOnAction(e -> window.close());
        return closeButton;
    }

    private ImageView buildImageView() {
        ImageView imageView = new ImageView("userinterface/error.png");
        AnchorPane.setLeftAnchor(imageView, 5.0);
        AnchorPane.setTopAnchor(imageView, 25.0);
        return imageView;
    }

}
