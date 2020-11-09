package com.epam.izh.rd.online;

import com.epam.izh.rd.online.entity.Pokemon;
import com.epam.izh.rd.online.service.PokemonFetchingService;
import com.epam.izh.rd.online.service.PokemonFightingClubService;
import com.epam.izh.rd.online.service.PokemonFightingService;
import com.epam.izh.rd.online.service.PokemonLoadService;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokemonFightingServiseTest {

    static Pokemon pikachu = new Pokemon();
    static Pokemon slowpoke = new Pokemon();
    static PokemonFetchingService pokemonFetchingService = new PokemonLoadService();
    static PokemonFightingClubService pokemonFightingClubService = new PokemonFightingService();

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        setupStub();

    }

    @BeforeEach
    public void fetchingPokemons() {

        pikachu = pokemonFetchingService.fetchByName("pikachu");
        slowpoke = pokemonFetchingService.fetchByName("slowpoke");
    }

    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    public static void setupStub() {
        wireMockServer.stubFor(get(urlEqualTo("/pokeapi.co/api/v2/pokemon/pikachu")).atPriority(1)
                .willReturn(aResponse().withHeader("User-Agent", "User")
                        .withStatus(200)
                        .withBodyFile("pikachu.json")));
        wireMockServer.stubFor(get(urlEqualTo("/pokeapi.co/api/v2/pokemon/slowpoke")).atPriority(2)
                .willReturn(aResponse().withHeader("User-Agent", "")
                        .withStatus(200)
                        .withBodyFile("slowpoke.json")));
        wireMockServer.stubFor(get(urlEqualTo("/raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png")).atPriority(3)
                .willReturn(aResponse().withHeader("User-Agent", "")
                        .withStatus(200)
                        .withBodyFile("25.png")));
        wireMockServer.stubFor(get(urlEqualTo("/raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/79.png")).atPriority(4)
                .willReturn(aResponse().withHeader("User-Agent", "")
                        .withStatus(200)
                        .withBodyFile("79.png")));
    }

    @Test
    public void doBattleTest() {
        assertThat(slowpoke, CoreMatchers.is(pokemonFightingClubService.doBattle(pikachu, slowpoke)));
    }


    @Test
    public void doDamageTest() {
        pokemonFightingClubService.doDamage(pikachu, slowpoke);
        assertEquals(70, slowpoke.getHp());
    }
}