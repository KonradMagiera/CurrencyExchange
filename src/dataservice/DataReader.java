package dataservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import userinterface.ErrorWindow;

public class DataReader {

    private CurrencyDictionary currencies;
    private ExchangeDictionary exchanges;
    private Stage stage;
    private ErrorWindow error;
    private final int CONSTANT_TAX;
    private final int PERCENTAGE_TAX;
    private int lines;
    private int isError;

    public DataReader(Stage stage, CurrencyDictionary currencies) {
        this.isError = 0;
        this.lines = 0;
        this.PERCENTAGE_TAX = 1;
        this.CONSTANT_TAX = 0;
        error = new ErrorWindow();
        this.currencies = currencies;
        this.stage = stage;
    }

    /**
     * Method reads from file choosen by a user from his PC
     *
     * @param openedFile file to read
     * @return instance of ExchangeDictionary
     */
    public ExchangeDictionary loadData(File openedFile) {
        if (openedFile != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(openedFile), "Cp1250"));

                String line = br.readLine();
                try {
                    String[] firstLine = line.split("\\s+");
                    if (firstLine[0].indexOf('#') < 0) {
                        readCurrency(firstLine);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {

                }
                lines++;
                while ((line = br.readLine()) != null) {
                    try {
                        String[] parsedLine = line.split("\\s+");
                        if (parsedLine[0].indexOf('#') >= 0) {
                            lines++;
                            break;
                        }

                        if (parsedLine.length > 1) {
                            readCurrency(parsedLine);
                        } else {
                            error.append("*Waluta lub jej id nie zostały podane; wiersz: " + lines + "\n");
                            isError = 1;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    lines++;
                }

                exchanges = new ExchangeDictionary(currencies.getNumberOfCurrencies());
                while ((line = br.readLine()) != null) {
                    try {
                        String[] parsedLine = line.split("\\s+");
                        if (parsedLine[0].indexOf('#') >= 0) {
                            lines++;
                            continue;
                        }
                        if (parsedLine.length > 5) {
                            readExchange(parsedLine);
                        } else {
                            error.append("*Kursy walut muszą zawierać sześć kluczowych informacji; wiersz: " + lines + "\n");
                            isError = 1;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    lines++;
                }

            } catch (IOException ex) {
                error.append("*Nie udało się otworzyć pliku\n");
                isError = 1;
            }

        }
        if (isError > 0) {
            error.display();
        }

        return exchanges;
    }

    /**
     * Method runs FileChooser to choose a file to load
     *
     * @return opened File
     */
    public File getFileFromFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT files", "*.txt"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Load currency information");
        fileChooser.setInitialFileName("Currencies");
        File openedFile = fileChooser.showOpenDialog(stage);
        return openedFile;
    }

    private void readCurrency(String[] parsedLine) {
        int id = -1;
        try {
            id = Integer.parseInt(parsedLine[0]);

        } catch (NumberFormatException e) {
            error.append("*Id waluty nie jest liczbą całkowitą; wiersz: " + lines + "\n");
            isError = 1;
            return;
        }
        if (parsedLine[1].length() != 3) {
            error.append("*Kod waluty zgodnie z normą ISO 4217 musi składać się z 3 liter; wiersz: " + lines + "\n");
            isError = 1;
            return;
        }
        if (id >= 0) {
            int ifFailed = currencies.addCurrency(id, parsedLine[1].toUpperCase());
            if (ifFailed == 1) {
                error.append("*Każda waluta musi mieć unikalne id; wiersz: " + lines + "\n");
                isError = 1;
            } else if (ifFailed == 2) {
                error.append("*Pojawiły się waluty o tym samym 3 literowym kodzie; wiersz: " + lines + "\n");
                isError = 1;
            }
        } else {
            error.append("*Id musi być całkowitą liczbą nieujemną; wiersz: " + lines + "\n");
            isError = 1;

        }
    }

    private void readExchange(String[] parsedLine) {
        int id = -1;
        double value = 0.0;
        int tax = -1;
        double taxValue = -1;

        try {
            id = Integer.parseInt(parsedLine[0]);
        } catch (NumberFormatException e) {
            error.append("*Id kursu waluty nie jest liczbą; wiersz: " + lines + "\n");
            isError = 1;
            return;
        }

        try {
            if (parsedLine[3].contains(",")) {
                StringBuilder stringBuilder = new StringBuilder(parsedLine[3]);
                stringBuilder.setCharAt(stringBuilder.indexOf(","), '.');
                parsedLine[3] = stringBuilder.toString();
            }
            value = Double.parseDouble(parsedLine[3]);
        } catch (NumberFormatException e) {
            error.append("*Wartość kursu nie jest liczbą; wiersz: " + lines + "\n");
            isError = 1;
            return;
        }

        try {
            taxValue = Double.parseDouble(parsedLine[5]);
        } catch (NumberFormatException e) {
            error.append("*Wartość opłaty za wymianę nie jest liczbą; wiersz: " + lines + "\n");
            isError = 1;
            return;
        }

        if (id >= 0) {
            int firstCurrencyId = currencies.findId(parsedLine[1]);
            int secondCurrencyId = currencies.findId(parsedLine[2]);

            if (firstCurrencyId == -1 || secondCurrencyId == -1) {
                error.append("*Wystąpiła waluta nie podana w pierwszej części pliku; wiersz: " + lines + "\n");
                isError = 1;
                return;
            } else if (firstCurrencyId == secondCurrencyId) {
                error.append("W pliku pojawiła się próba przeliczenia waluty na samą siebie; wiersz: " + lines + "\n");
                isError = 1;
                return;
            }
            if ("PROC".equals(parsedLine[4].toUpperCase())) {
                tax = PERCENTAGE_TAX;
            } else if ("STAŁA".equals(parsedLine[4].toUpperCase())) {
                tax = CONSTANT_TAX;
            } else {
                error.append("*Typ opłaty nie zawierał słowa kluczowego PROC lub STAŁA; wiersz: " + lines + "\n");
                isError = 1;
                return;
            }

            int ifFailed = exchanges.addCurrencyExchange(id, firstCurrencyId, secondCurrencyId, value, tax, taxValue);
            if (ifFailed == 1) {
                error.append("*Podana wymiana walut już istnieje; wiersz: " + lines + "\n");
                isError = 1;
            } else if (ifFailed == 2) {
                error.append("*Id wymiany waluty nie jest unikalne; wiersz: " + lines + "\n");
                isError = 1;
            }
        } else {
            error.append("*Id wymiany walut nie jest całkowitą liczbą nieujmeną; wiersz: " + lines + "\n");
            isError = 1;
        }
    }
}
