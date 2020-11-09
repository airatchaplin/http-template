package com.epam.izh.rd.online;

import com.epam.izh.rd.online.entity.Pokemon;
import com.epam.izh.rd.online.service.PokemonFetchingService;
import com.epam.izh.rd.online.service.PokemonLoadService;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.FileNotFoundException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PokemonLoadServiceTest {

    private static final PokemonFetchingService fetchingService = new PokemonLoadService();
    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        setupStub();

    }

    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    public static void setupStub() {
        wireMockServer.stubFor(get(urlEqualTo("/pokeapi.co/api/v2/pokemon/pikachu"))
                .willReturn(aResponse().withHeader("User-Agent", "User")
                        .withStatus(200)
                        .withBodyFile("pikachu.json")));
        wireMockServer.stubFor(get(urlEqualTo("/pokeapi.co/api/v2/pokemon/slowpoke"))
                .willReturn(aResponse().withHeader("User-Agent", "User")
                        .withStatus(200)
                        .withBodyFile("slowpoke.json")));
        wireMockServer.stubFor(get(urlEqualTo("/raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png"))
                .willReturn(aResponse().withHeader("User-Agent", "User")
                        .withStatus(200)
                        .withBodyFile("25.png")));
        wireMockServer.stubFor(get(urlEqualTo("/raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/79.png"))
                .willReturn(aResponse().withHeader("User-Agent", "User")
                        .withStatus(200)
                        .withBodyFile("79.png")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"pikachu", "slowpoke"})
    public void fetchByNameTest(String name) {
        Pokemon pokemon = fetchingService.fetchByName(name);
        assertEquals(pokemon.getPokemonName(), name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"piCachu", "hello"})
    public void fetchByNameExceptionTest(String name) {
        assertEquals(FileNotFoundException.class, FileNotFoundException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"pikachu", "slowpoke"})
    public void getPokemonImageTest(String name) {
        PokemonLoadService pokemonLoadService = new PokemonLoadService();
        byte[] image = pokemonLoadService.getPokemonImage(name);
        assertNotNull(image);
    }

}