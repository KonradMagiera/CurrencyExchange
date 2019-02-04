package userinterface;

import algorithm.BestPathAlgorithm;
import dataservice.CurrencyDictionary;
import dataservice.DataReader;
import dataservice.ExchangeDictionary;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class InterfaceController implements Initializable {

    @FXML
    private TextField moneyValue;
    @FXML
    private ChoiceBox<String> startingCurrency;
    @FXML
    private ChoiceBox<String> endingCurrency;
    @FXML
    private Label pathDisplay;
    @FXML
    private ImageView arrowImage;
    @FXML
    private Button exchangeButton;
    @FXML
    private BorderPane mainPane;
    private ExchangeDictionary exchanges;
    private CurrencyDictionary currencies;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        moneyValue.setPromptText("1000.00");
        moneyValue.setText("1000.00");
        startingCurrency.setDisable(true);
        endingCurrency.setDisable(true);
        exchangeButton.setDisable(true);
        pathDisplay.setAlignment(Pos.CENTER);
        pathDisplay.setWrapText(true);
        Image image = new Image("userinterface/arr.png");
        arrowImage.setImage(image);
        String css = InterfaceController.class.getResource("/userinterface/menu.css").toExternalForm();
        mainPane.getStylesheets().add(css);
    }

    @FXML
    private void loadFile(ActionEvent event) {
        currencies = new CurrencyDictionary();
        DataReader reader = new DataReader(stage, currencies);
        startingCurrency.getItems().removeAll(startingCurrency.getItems());
        endingCurrency.getItems().removeAll(endingCurrency.getItems());
        File openedFile = reader.getFileFromFileChooser();
        exchanges = reader.loadData(openedFile);
        if (exchanges != null) {
            for (int i = 0; i < currencies.getNumberOfCurrencies(); i++) {
                startingCurrency.getItems().addAll(currencies.findNameByProgramId(i));
                endingCurrency.getItems().addAll(currencies.findNameByProgramId(i));
            }
            pathDisplay.setText("");
            if (currencies.getNumberOfCurrencies() > 0) {
                startingCurrency.setValue(currencies.findNameByProgramId(0));
                endingCurrency.setValue(currencies.findNameByProgramId(0));
            }
            startingCurrency.setDisable(false);
            endingCurrency.setDisable(false);
            exchangeButton.setDisable(false);
        }

    }

    @FXML
    private void exchangeCurrency(ActionEvent event) {
        double money = 1000;
        if (!"".equals(moneyValue.getText())) {
            money = roundMoney(Double.parseDouble(moneyValue.getText()));
        }
        BestPathAlgorithm bestPath = new BestPathAlgorithm(exchanges, currencies, money);
        String path = bestPath.findPath(currencies.findId(startingCurrency.getValue()), currencies.findId(endingCurrency.getValue()));
        pathDisplay.setText(path);
    }

    /**
     * pass reference to main stage to lock pop up windows
     *
     * @param stage main stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private double roundMoney(double money) {
        double roundMoney = money * 100;
        roundMoney = Math.round(roundMoney);
        return roundMoney / 100;
    }
}
