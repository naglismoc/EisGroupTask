package com.company;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Main m = new Main();

        boolean workWithOneDate = false; //change to true to get one day data.
        String startDate = "2018-12-17";
        String endDate = "2018-12-19";
        String currencyCode = "USD";//leave empty if want to see all currencies
        boolean showAllDataFromTo = false;//shows all days data
        int value = 7;
        Scanner myObj = new Scanner(System.in);
        //String scannerRepare = myObj.nextLine();
        String dBUrl = "https://www.lb.lt/lt/currency/daylyexport/?csv=1&class=Eu&type=day&date_day=";
        LocalDate now = LocalDate.now();
        List<List<String>> dataListsList;
        boolean workWithOneDate2 = workWithOneDate;
        LocalDate start;
        LocalDate end;
        String scannerRepare;
        while(value!=10) {
            System.out.println("----------------------------");
            switch (value) {
                case 1:
                    System.out.println("settings window.");
                    System.out.println("do you want to get one date data? true/false");
                    workWithOneDate2 = myObj.nextBoolean();
                    value = 6;
                    break;
                case 2:
                    System.out.println("do you want to see all data between dates? (true) or you want to see difference between selected dates?(false)");
                    showAllDataFromTo = myObj.nextBoolean();
                     scannerRepare = myObj.nextLine();
                    value = 6;
                    break;
                case 3:
                     scannerRepare = myObj.nextLine();
                    System.out.println("choose currency (currency code - USD,AUD etc) Enter for Not to choose currency");
                    currencyCode = myObj.nextLine();
                    value=6;
                    break;
                case 4:
                    System.out.println("enter start date. Format yyyy-mm-dd");
                    scannerRepare = myObj.nextLine();
                    startDate = myObj.nextLine();
                    if (!workWithOneDate2) {
                        System.out.println("enter end date. Format yyyy-mm-dd");
                        endDate = myObj.nextLine();
                    }
                    value = 6;
                    break;
                case 5:
                    try {
                        start = LocalDate.parse(startDate);
                        end = LocalDate.parse(endDate);
                    } catch (Exception e) {
                        System.out.println("Wrong date format or its not a date");
                        return;
                    }
                    if (end.compareTo(now) > 0) {
                        System.out.println("Chosen end date is in the future. Setting it to current day.");
                        end = now;
                    }
                    workWithOneDate2 = m.setToworkWithOneDay(end, start, now);

                    if (end.isBefore(LocalDate.of(2014, 10, 1)) || start.isBefore(LocalDate.of(2014, 10, 1))) {
                        System.out.println("Data you are trying to access wasnt dated than. Please check the dates");
                        return;
                    }

                    if (showAllDataFromTo) {
                        dataListsList = m.dataFromToAllIncluded(start, end, dBUrl, workWithOneDate2, currencyCode);
                        m.printsOutData2(dataListsList);
                    } else {
                        dataListsList = m.dataFromToOnly(start, end, dBUrl, currencyCode);
                        m.printsOutData(dataListsList);
                    }
                    workWithOneDate2 = workWithOneDate;
                    value =6;
                    break;
                case 6:
                    System.out.println("change how many dates to use press 1");
                    System.out.println("change data range to from-to press 2");
                    System.out.println("change/disable currency press 3");
                    System.out.println("change date press 4");
                    System.out.println("start - press 5");
                    System.out.println("press 7 to quit");
                     value = myObj.nextInt();
                    break;
                case 7: value=10;
                break;
            }
        }
    }

    private void printsOutData2(List<List<String>> dataListsList) {
        System.out.println("Valiutos pavadinimas, Valiutos kodas, Santykis, Data.");
        for (List<String> strings : dataListsList) {
            for (String string : strings) {
                System.out.println(string);
            }
        }
    }

    /**
     * prints out data with currency differences
     */
    private void printsOutData(List<List<String>> dataListsList) {
        BigDecimal tmpCurrencyValue = new BigDecimal(0);
        for (int i = 0; i < dataListsList.size(); i++) {
            for (int c = 0; c < dataListsList.get(i).size(); c++) {
                String rawData = dataListsList.get(i).get(c);

                String[] list = rawData.split(";");
                BigDecimal currencyValue = inelegantWayToGetNumber(list);

                System.out.print(list[3] + ":  " + list[0] + ". " + list[2]);
                if (i > 0) {
                    System.out.print(". difference: " + (currencyValue.subtract(tmpCurrencyValue)));
                }
                System.out.println();
                tmpCurrencyValue = currencyValue;
            }
        }
    }

    /**
     * method for currency value to be converted string to number
     *
     * @param list
     * @return currencyValue
     */
    private BigDecimal inelegantWayToGetNumber(String[] list) {

        String stringPiece = "";
        int count = 0;
        for (int h = 1; h < list[2].length() - 1; h++) {
            if (Character.isDigit(list[2].charAt(h))) {
                stringPiece += list[2].charAt(h);
            } else {
                count = h;
            }
        }
        count = list[2].length() - count - 2;
        int in = Integer.valueOf(stringPiece);
        BigDecimal currencyValue = BigDecimal.valueOf(in);
        for (int g = 0; g < count; g++) {
            currencyValue = currencyValue.divide(BigDecimal.TEN);
        }
        return currencyValue;
    }

    /**
     * some protections for bad input grouped by return value
     *
     * @param end
     * @param start
     * @param now
     * @return true/false
     */
    private boolean setToworkWithOneDay(LocalDate end, LocalDate start, LocalDate now) {
        boolean workWithOneDay = false;
        if (end.isBefore(start)) {
            System.out.println("start data is later or equal to end date. Switching to 'workWithOneDay' mode.");
            workWithOneDay = true;
        }
        if (end.equals(start)) {
            System.out.println("start data is later or equal to end date. Switching to 'workWithOneDay' mode.");
            workWithOneDay = true;
        }
        if (start.compareTo(now) > -1) {
            System.out.println("Chosen start date is equals to current day. Switching to 'workWithOneDay' mode.");
            workWithOneDay = true;
        }
        return workWithOneDay;
    }

    /**
     * main method that returns list<list<String> of data with all days currencies.
     *
     * @param start
     * @param end
     * @param dBUrl
     * @param workWithOneDay
     * @param currencyCode
     * @return
     */
    private List<List<String>> dataFromToAllIncluded(LocalDate start, LocalDate end, String dBUrl, boolean workWithOneDay, String currencyCode) {
        Main m = new Main();
        List<List<String>> dataListsList = new ArrayList();
        if (!workWithOneDay) {
            dataListsList = (m.iterateThroughDays(start, end, dBUrl, currencyCode));
        } else {
            GetUrlContent guc = new GetUrlContent();
            dataListsList.add(guc.getUrlContents(dBUrl + start, currencyCode));
        }
        return dataListsList;
    }

    /**
     * @param start start date
     * @param end   and  dare
     * @param dBUrl start of url
     * @return String. All data of days in interval
     */
    private List<List<String>> iterateThroughDays(LocalDate start, LocalDate end, String dBUrl, String currencyCode) {
        GetUrlContent guc = new GetUrlContent();
        LocalDate next = start.minusDays(1);
        List<List<String>> dataListsList = new ArrayList();
        String data = "";
        while ((next = next.plusDays(1)).isBefore(end.plusDays(1))) {
            if (next.getMonthValue() == 1 && next.getDayOfMonth() == 1) {
                System.out.println("its first day in the year. We will skip it");
                continue;
            }
            if (next.getMonthValue() == 12 && (next.getDayOfMonth() == 24 || next.getDayOfMonth() == 25)) {
                System.out.println("its christmas. We will skip it");
                continue;
            }
            if ((next.getDayOfWeek().toString().equals("SUNDAY") || next.getDayOfWeek().toString().equals("SATURDAY"))) {
                System.out.println("Chosen day belongs to weekend, there's no new data. We will skip it");
                continue;
            }
            dataListsList.add(guc.getUrlContents(dBUrl + next, currencyCode));
        }
        return dataListsList;
    }

    public List<List<String>> dataFromToOnly(LocalDate start, LocalDate end, String dBUrl, String currencyCode) {
        GetUrlContent guc = new GetUrlContent();
        List<List<String>> dataListsList = new ArrayList();
        dataListsList.add(guc.getUrlContents(dBUrl + start, currencyCode));
        dataListsList.add(guc.getUrlContents(dBUrl + end, currencyCode));
        return dataListsList;

    }

}