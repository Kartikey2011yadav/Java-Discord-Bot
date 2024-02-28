package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JokeServices {
    public String GetJokes() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest. newBuilder()
                .uri(URI.create("https://api.chucknorris.io/jokes/random"))
                .GET()
                .build();

        HttpResponse<String> response= client.send(request, HttpResponse.BodyHandlers.ofString());
        String Joke = null;
        if(response.statusCode() == 200){
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node= objectMapper.readTree(response.body());
            Joke= node.get("value").asText();
        }
        return Joke;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        JokeServices j = new JokeServices();
        String joke = j.GetJokes();
        String ModifyJ = joke.replaceAll("Chuck Norris","Tanishq The Smuggler");
        String ModifyJm = ModifyJ.replaceAll("Chuck","Tanishq");
        System.out.println(ModifyJm);
    }
}

