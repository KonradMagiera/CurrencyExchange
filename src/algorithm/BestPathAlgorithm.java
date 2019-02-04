package algorithm;

import dataservice.CurrencyDictionary;
import dataservice.ExchangeDictionary;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BestPathAlgorithm {

    private ExchangeDictionary exchanges;
    private CurrencyDictionary currencies;
    private double money;
    private ArrayList<ArrayList<Integer>> paths;
    private final int CONSTANT_TAX;
    private final int PERCENTAGE_TAX;

    public BestPathAlgorithm(ExchangeDictionary exchanges, CurrencyDictionary currencies, double money) {
        this.PERCENTAGE_TAX = 1;
        this.CONSTANT_TAX = 0;
        this.exchanges = exchanges;
        this.currencies = currencies;
        paths = new ArrayList<>();
        this.money = money;
    }

    /**
     * Method goes through adjacencyList and search for path from starting
     * currency to final currency.
     *
     * @param start chosen starting currency
     * @param end chosen ending currency
     * @return path from start to end in String with starting and ending value
     */
    public String findPath(int start, int end) {
        Queue<ArrayList<Integer>> queue = new LinkedList<>();
        ArrayList<Integer> path = new ArrayList<>();
        path.add(start);
        queue.add(path);
        while (!queue.isEmpty()) {
            path = queue.poll();
            int last = path.get(path.size() - 1);

            if (last == end) {
                paths.add(path);
                if (start == end && !"Nie odnaleziono korzystnego arbitrażu.".equals(printArbitraryPath())) {
                    return printArbitraryPath();
                }
            }

            for (int i = 0; i < exchanges.adjacencySize(last); i++) {
                if (!isVisited(exchanges.getConnectionFromAdjacencyList(last, i), path, start, end)) {
                    ArrayList<Integer> newPath = new ArrayList(path);
                    newPath.add(exchanges.getConnectionFromAdjacencyList(last, i));
                    queue.add(newPath);
                }
            }
        }
        return printPath(start, end);
    }

    private boolean isVisited(int currency, ArrayList<Integer> path, int start, int end) {
        int size = path.size();
        int iterator = 0;

        if (start == end) {
            iterator = 1;
        }
        for (int i = iterator; i < size; i++) {
            if (path.get(i) == currency) {
                return true;
            }
        }
        return false;
    }

    private String printPath(int firstCurrencyId, int secondCurrencyId) {
        int longestPath = -1;
        double highestValue = 0;

        for (int i = 0; i < paths.size(); i++) {
            double currentMoney = money;
            for (int j = 0; j < paths.get(i).size() - 1; j++) {
                int from = paths.get(i).get(j);
                int to = paths.get(i).get(j + 1);
                currentMoney = calculateEarnings(exchanges.getExchangeInfo(from, to), currentMoney);
            }
            if (currentMoney >= highestValue) {
                longestPath = i;
                highestValue = currentMoney;
            }
        }
        return buildString(longestPath, firstCurrencyId, secondCurrencyId, highestValue);
    }

    private String printArbitraryPath() {
        int longestPath = paths.size() - 1;
        double finalMoney = money;
        for (int i = 0; i < paths.get(longestPath).size() - 1; i++) {
            int from = paths.get(longestPath).get(i);
            int to = paths.get(longestPath).get(i + 1);
            finalMoney = calculateEarnings(exchanges.getExchangeInfo(from, to), finalMoney);
        }
        return buildString(longestPath, 0, 0, finalMoney);
    }

    private String buildString(int longestPath, int firstCurrencyId, int secondCurrencyId, double highestValue) {
        StringBuilder sb = new StringBuilder();
        if (longestPath != -1 && firstCurrencyId != secondCurrencyId) {

            int size = paths.get(longestPath).size();
            sb.append(money).append(" ").append(currencies.findNameByProgramId(paths.get(longestPath).get(0))).append(" -> ");
            for (int i = 1; i < size - 1; i++) {
                sb.append(currencies.findNameByProgramId(paths.get(longestPath).get(i))).append(" -> ");
            }
            sb.append(highestValue).append(" ").append(currencies.findNameByProgramId(paths.get(longestPath).get(size - 1)));
        } else if (longestPath != -1 && firstCurrencyId == secondCurrencyId) {
            if (money < highestValue) {
                int size = paths.get(longestPath).size();
                sb.append(money).append(" ").append(currencies.findNameByProgramId(paths.get(longestPath).get(0))).append(" -> ");
                for (int i = 1; i < size - 1; i++) {
                    sb.append(currencies.findNameByProgramId(paths.get(longestPath).get(i))).append(" -> ");
                }
                sb.append(highestValue).append(" ").append(currencies.findNameByProgramId(paths.get(longestPath).get(size - 1)));
            } else {
                sb.append("Nie odnaleziono korzystnego arbitrażu.");
            }
        } else {
            sb.append("Nie odnaleziono ścieżki.");
        }
        return sb.toString();
    }

    private double calculateEarnings(double[] exchangeInfo, double currentMoney) {
        double newValue = currentMoney;
        if (exchangeInfo[1] == CONSTANT_TAX) {
            newValue = currentMoney * exchangeInfo[0] - exchangeInfo[2];
        } else if (exchangeInfo[1] == PERCENTAGE_TAX) {
            newValue = currentMoney * exchangeInfo[0] * (1 - exchangeInfo[2]);
        }
        return roundMoney(newValue);
    }

    private double roundMoney(double money) {
        double roundMoney = money * 100;
        roundMoney = Math.round(roundMoney);
        return roundMoney / 100;
    }
}
