package com.epam.izh.rd.online.service;

import com.epam.izh.rd.online.entity.Pokemon;
import com.epam.izh.rd.online.factory.PokemonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PokemonLoadService implements PokemonFetchingService {

    @Override
    public Pokemon fetchByName(String name) throws IllegalArgumentException {

        String json = "";

        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "User");

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                json += scanner.nextLine();
            }
            connection.disconnect();
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode jsonNodeStats = jsonNode.get("stats");

        Pokemon pokemon = getHpAttackDefense(jsonNodeStats);
        pokemon.setPokemonId(jsonNode.get("id").asLong());
        pokemon.setPokemonName(jsonNode.get("name").asText());


        return pokemon;
    }

    public static Pokemon getHpAttackDefense(JsonNode jsonNode) {
        Pokemon pokemon = new Pokemon();
        List<JsonNode> hpAttakDefense = new ArrayList<>();
        for (JsonNode jn : jsonNode) {
            hpAttakDefense.add(jn.get("base_stat"));
        }
        for (int i = 0; i < 3; i++) {
            pokemon.setHp(hpAttakDefense.get(0).shortValue());
            pokemon.setAttack(hpAttakDefense.get(1).shortValue());
            pokemon.setDefense(hpAttakDefense.get(2).shortValue());
        }
        return pokemon;
    }


    @Override
    public byte[] getPokemonImage(String name) throws IllegalArgumentException {
        String json = "";

        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "User");

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                json += scanner.nextLine();
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;


        try {
            jsonNode = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode jsonNodeImage = jsonNode.get("sprites");
        JsonNode jsonNodeFront = jsonNodeImage.get("front_default");
        byte[] bytes = new byte[jsonNodeFront.size()];

        try {
            bytes = jsonNodeFront.asText().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return bytes;
    }
}