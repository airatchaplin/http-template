package com.epam.izh.rd.online;

import com.epam.izh.rd.online.entity.Pokemon;
import com.epam.izh.rd.online.service.PokemonFightingService;
import com.epam.izh.rd.online.service.PokemonLoadService;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public class Http {


    public static void main(String[] args){

        PokemonLoadService pokemonLoadService = new PokemonLoadService();
        PokemonFightingService fighting = new PokemonFightingService();

        Pokemon slowpoke = pokemonLoadService.fetchByName("slowpoke");
        Pokemon pikachu = pokemonLoadService.fetchByName("pikachu");

        Pokemon winner = fighting.doBattle(pikachu,slowpoke);
        fighting.showWinner(winner);


    }
}


