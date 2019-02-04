package dataservice;

import java.util.ArrayList;

public class CurrencyDictionary {

    private final ArrayList<Integer> listOfIds;
    private final ArrayList<String> currencies;
    private final int MISSING_ID = -1;
    private final int USED_ID = 1;
    private final int USED_NAME = 2;

    public CurrencyDictionary() {
        listOfIds = new ArrayList<>();
        currencies = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currencies.size(); i++) {
            sb.append(listOfIds.get(i)).append(" ").append(currencies.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Method adds items to dictionary. Checks if parameters weren't used.
     *
     * @param idInFile id of currencie read from file
     * @param name short name of the currency
     * @return information if there was an error
     */
    public int addCurrency(int idInFile, String name) {
        if (isIdUsed(idInFile)) {
            return USED_ID;
        }
        if (isNameUsed(name)) {
            return USED_NAME;
        }
        listOfIds.add(idInFile);
        currencies.add(name);
        return 0;
    }

    /**
     * Method return name of the currency if id is not greater than size of the
     * dictionary.
     *
     * @param id id of currency in list
     * @return name of currency or null
     */
    public String findNameByProgramId(int id) {
        if (currencies.size() < id) {
            return null;
        }
        return currencies.get(id);
    }

    /**
     * Method finds id of currency with given name.
     *
     * @param name currency name
     * @return id of currency or -1
     */
    public int findId(String name) {
        for (int i = 0; i < currencies.size(); i++) {
            if (currencies.get(i).equals(name)) {
                return i;
            }
        }
        return MISSING_ID;
    }

    /**
     * Method gives number of currencies
     *
     * @return size of array with currencies
     */
    public int getNumberOfCurrencies() {
        return currencies.size();
    }

    private boolean isIdUsed(int idInFile) {
        for (int i = 0; i < listOfIds.size(); i++) {
            if (listOfIds.get(i) == idInFile) {
                return true;
            }
        }
        return false;
    }

    private boolean isNameUsed(String name) {
        for (int i = 0; i < currencies.size(); i++) {
            if (currencies.get(i).equals(name)) {
                return true;
            }
        }
        return false;
    }
}
