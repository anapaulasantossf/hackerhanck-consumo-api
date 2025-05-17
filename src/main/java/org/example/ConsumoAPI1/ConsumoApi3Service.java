package org.example.ConsumoAPI1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConsumoApi3Service {

    public static void consumoApi() {
        int page = 1;
        int totalPage = 1;

        List<Double> arrayCreditos = new ArrayList<>();
        List<Double> arrayDebitos = new ArrayList<>();

        while (page <= totalPage) {
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
                            double amount = Double.parseDouble(dataObject.getString("amount").replaceAll("\\$","").replaceAll(",",""));
                            String txnType = dataObject.getString("txnType");
                            if (txnType.equals("credit")){
                                arrayCreditos.add(amount);
                            }
                            if (txnType.equals("debit")){
                                arrayDebitos.add(amount);
                            }
                        }
                    }
                }
                page++;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //System.out.println(arrayCreditos.get(arrayCreditos.size()-1));
        //Collections.sort(arrayCreditos);

        //Collections.sort(arrayDebitos);
        //System.out.println(arrayDebitos.get(arrayDebitos.size()-1));

        System.out.println(Collections.max(arrayCreditos));
        System.out.println(Collections.max(arrayDebitos));
    }
}
