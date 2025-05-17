package org.example.ConsumoAPI1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi1Service {

    public static void consumoApi1() {
        int page = 1;
        int totalPage = 1;
        double amountCreditMaior = 0;
        double amountDebitMaior = 0;

        while (page <= totalPage){

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonmock.hackerrank.com/api/transactions?page="+page))
                    .GET()
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject json = new JSONObject(response.body());
                if (page == 1) {
                    totalPage = json.getInt("total_pages");
                }

                JSONArray data = json.getJSONArray("data");

                for (int i=0; i<data.length(); i++){
                    JSONObject dataObject = data.getJSONObject(i);
                    String userName = dataObject.getString("userName");
                    if (userName.equals("Bob Martin")) {
                        JSONObject location = dataObject.getJSONObject("location");
                        String city = location.getString("city");
                        if (city.equals("Bourg")) {
                            String txnType = dataObject.getString("txnType");
                            if (txnType.equals("credit")){
                                double amount = Double.parseDouble(dataObject.getString("amount").replaceAll("\\$","").replaceAll(",",""));
                                if (amountCreditMaior < amount) {
                                    amountCreditMaior = amount;
                                }
                            }
                            if (txnType.equals("debit")){
                                double amount = Double.parseDouble(dataObject.getString("amount").replaceAll("\\$","").replaceAll(",",""));
                                if (amountDebitMaior < amount) {
                                    amountDebitMaior = amount;
                                }
                            }
                        }
                    }
                }
                page++;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(amountCreditMaior);
        System.out.println(amountDebitMaior);
    }
}
