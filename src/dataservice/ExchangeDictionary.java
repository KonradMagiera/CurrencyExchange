package dataservice;

import java.util.ArrayList;

public class ExchangeDictionary {

    private double[][][] currencyExchangeInfo;
    private ArrayList<Integer> listOfIds;
    private ArrayList<ArrayList<Integer>> adjacencyList;
    private final int EXCHANGE_EXIST;
    private final int USED_ID;

    public ExchangeDictionary(int numberOfCurrencies) {
        this.USED_ID = 2;
        this.EXCHANGE_EXIST = 1;
        currencyExchangeInfo = new double[numberOfCurrencies][numberOfCurrencies][3];
        listOfIds = new ArrayList<>();
        adjacencyList = new ArrayList<>();
        for (int i = 0; i < numberOfCurrencies; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currencyExchangeInfo.length; i++) {
            for (int j = 0; j < currencyExchangeInfo.length; j++) {
                sb.append(currencyExchangeInfo[i][j][0]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n");
        for (int i = 0; i < adjacencyList.size(); i++) {
            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                sb.append(adjacencyList.get(i).get(j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Method add cyrrency exchange to dictionary. Returns 1 if exchange exists,
     * 2 if id is used or 0 added.
     *
     * @param id id of exchange in file
     * @param firstCurrencyId id of starting currency
     * @param secondCurrencyId id of ending currency
     * @param value price of currency
     * @param typeOfPay CONSTANT OR PERCENTAGE
     * @param payValue tag rate
     * @return error info
     */
    public int addCurrencyExchange(int id, int firstCurrencyId, int secondCurrencyId, double value, int typeOfPay, double payValue) {
        if (wasExchangeGiven(firstCurrencyId, secondCurrencyId)) {
            return EXCHANGE_EXIST;
        }
        if (isIdUsed(id)) {
            return USED_ID;
        }
        currencyExchangeInfo[firstCurrencyId][secondCurrencyId][0] = value;
        if (payValue == 0.0) {
            currencyExchangeInfo[firstCurrencyId][secondCurrencyId][1] = 0;
        } else {
            currencyExchangeInfo[firstCurrencyId][secondCurrencyId][1] = typeOfPay;
        }
        currencyExchangeInfo[firstCurrencyId][secondCurrencyId][2] = payValue;
        listOfIds.add(id);
        adjacencyList.get(firstCurrencyId).add(secondCurrencyId);
        return 0;
    }

    /**
     * Method creates vector with price, kind of tax and value of tax.
     *
     * @param firstCurrencyId from
     * @param secondCurrencyId to
     * @return vecotr with 3 elements or null
     */
    public double[] getExchangeInfo(int firstCurrencyId, int secondCurrencyId) {
        try {
            double[] values = {currencyExchangeInfo[firstCurrencyId][secondCurrencyId][0],
                currencyExchangeInfo[firstCurrencyId][secondCurrencyId][1],
                currencyExchangeInfo[firstCurrencyId][secondCurrencyId][2]};
            return values;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Method gives number of currencies that you can exchange to from specific
     * currencie.
     *
     * @param currencyId specific currencie
     * @return number of possible exchanges
     */
    public int adjacencySize(int currencyId) {
        return adjacencyList.get(currencyId).size();
    }

    /**
     * Method checks connections for firstCurrencyId and returns connedction on
     * positon secondCurrency.
     *
     * @param firstCurremcyId exchange from
     * @param secondCurrency pointer to check what currency is there
     * @return secondCurrencyId
     */
    public int getConnectionFromAdjacencyList(int firstCurremcyId, int secondCurrency) {
        return adjacencyList.get(firstCurremcyId).get(secondCurrency);
    }

    private boolean wasExchangeGiven(int firstCurrencyId, int secondCurrencyId) {
        return currencyExchangeInfo[firstCurrencyId][secondCurrencyId][0] != 0.0;
    }

    private boolean isIdUsed(int idInFile) {
        for (int i = 0; i < listOfIds.size(); i++) {
            if (listOfIds.get(i) == idInFile) {
                return true;
            }
        }
        return false;
    }
}
