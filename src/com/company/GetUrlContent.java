package com.company;

import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetUrlContent {
    /**this method tries to initiate connection with server get data. if succeeds, starts other methods that reads it, than returns array
     *
     * @param theUrl
     * @param currencyCode
     * @return List<String> dataList
     */
    public static List<String> getUrlContents(String theUrl, String currencyCode) {
        List<String> dataList = new ArrayList();
        GetUrlContent guc = new GetUrlContent();

        try {
            URL url = new URL(theUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!urlConnection.getURL().toString().equals(theUrl)) {
                System.out.println("bad url.");
                return null;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            if (currencyCode.length() > 0) {
                dataList = guc.bufferedReadAddByCurrency(bufferedReader, currencyCode);
            } else {
                dataList = guc.bufferedReadAddAllData(bufferedReader);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }

    /**reads all data and adds it in array.
     *
     * @param bufferedReader
     * @return  List<String> dataList
     * @throws IOException
     */
    private List<String> bufferedReadAddAllData(BufferedReader bufferedReader) throws IOException {
        List<String> dataList = new ArrayList();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            dataList.add(line);
        }
        return dataList;
    }

    /**reads all data, adds lines to array that contains right currency
     *
     * @param bufferedReader
     * @param currencyCode
     * @return
     * @throws IOException
     */
    private List<String> bufferedReadAddByCurrency(BufferedReader bufferedReader, String currencyCode) throws IOException {
        List<String> dataList = new ArrayList();
        String line;
        while ((line = bufferedReader.readLine()) != null) {

            if (line.contains(currencyCode)) {
                dataList.add(line);
            }
        }
        return dataList;
    }
}
