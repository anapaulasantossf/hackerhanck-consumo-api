package org.example.ConsumoAPI1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConsumoApi4Service {

    private static final String BASE_URL = "https://jsonmock.hackerrank.com/api/transactions?page=";
    private static final String TARGET_USER = "Bob Martin";
    private static final String TARGET_CITY = "Bourg";

    public static void consumoApi() {
        final HttpClient client = HttpClient.newHttpClient();
        int page = 1;
        int totalPages = 1;

        final List<Double> creditos = new ArrayList<>();
        final List<Double> debitos = new ArrayList<>();

        while (page <= totalPages) {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + page))
                    .GET()
                    .build();

            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                final JSONObject json = new JSONObject(response.body());

                if (page == 1) {
                    totalPages = json.getInt("total_pages");
                }

                final JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    final JSONObject obj = data.getJSONObject(i);
                    if (!TARGET_USER.equals(obj.getString("userName"))) continue;

                    final JSONObject location = obj.getJSONObject("location");
                    if (!TARGET_CITY.equals(location.getString("city"))) continue;

                    final double amount = parseAmount(obj.getString("amount"));
                    final String txnType = obj.getString("txnType");

                    if ("credit".equalsIgnoreCase(txnType)) {
                        creditos.add(amount);
                    } else if ("debit".equalsIgnoreCase(txnType)) {
                        debitos.add(amount);
                    }
                }
                page++;

            } catch (IOException | InterruptedException e) {
                System.err.println("Erro ao consumir a API na página " + page + ": " + e.getMessage());
                return;
            }
        }

        if (!creditos.isEmpty()) {
            System.out.println("Maior crédito: " + Collections.max(creditos));
        } else {
            System.out.println("Nenhum crédito encontrado.");
        }

        if (!debitos.isEmpty()) {
            System.out.println("Maior débito: " + Collections.max(debitos));
        } else {
            System.out.println("Nenhum débito encontrado.");
        }
    }

    private static double parseAmount(String amountStr) {
        return Double.parseDouble(amountStr.replace("$", "").replace(",", ""));
    }
}
