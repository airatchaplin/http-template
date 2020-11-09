package com.epam.izh.rd.online.service;

import com.epam.izh.rd.online.entity.Pokemon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;

public class PokemonFightingService implements PokemonFightingClubService {

    private short damage;


    /**
     * Инициирует бой между двумя покемонами, должен использовать метод doDamage
     *
     * @param p1 атакующий покемон
     * @param p2 защищающийся покемон
     * @return победителя
     */

    @Override
    public Pokemon doBattle(Pokemon p1, Pokemon p2) {
        int count = 1;
        Pokemon from = p1.getPokemonId() > p2.getPokemonId() ? p1 : p2;
        Pokemon to = p1.getPokemonId() < p2.getPokemonId() ? p1 : p2;

        System.out.println("<<< Бой между покемонами : " + from.getPokemonName() + " и " + to.getPokemonName() + " >>> ");
        System.out.println("<<< Первым начинает " + from.getPokemonName() + " >>> ");

        while (true) {

            System.out.println("<<< " + count + " Раунд! >>>");
            doDamage(from, to);
            System.out.println(from.getPokemonName() + " наносит " + damage + " урона");

            System.out.println("Оставщееся хп: " + from.getPokemonName() + " = " + from.getHp() + " \n " +
                    "              " + to.getPokemonName() + " = " + to.getHp());
            if (to.getHp() <= 0) {
                System.out.println("<<< У " + to.getPokemonName() + " не осталось хп! Победу одерживает " + from.getPokemonName() + "!!! >>>");
                return from;
            } else if (to.getHp() > 0) {
                doDamage(to, from);
                System.out.println(to.getPokemonName() + " наносит " + damage + " урона");
                System.out.println("Оставщееся хп :" + from.getPokemonName() + "=" + from.getHp() + " " + to.getPokemonName() + "=" + to.getHp());
                if (from.getHp() <= 0) {
                    System.out.println("<<< У " + from.getPokemonName() + " не осталось хп! Победу одерживает " + to.getPokemonName() + "!!!");
                    return to;
                }
            }
            count++;
        }
    }

    @Override
    public void showWinner(Pokemon winner) {
        PokemonFetchingService pokemonFetchingService = new PokemonLoadService();
        byte[] pokemonImage = pokemonFetchingService.getPokemonImage(winner.getPokemonName());
        String path = "src/winner.png";
        String imageURL = new String(pokemonImage);
        try {
            BufferedImage img = ImageIO.read(new URL(imageURL));
            File file = new File(path);
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doDamage(Pokemon from, Pokemon to) {
        damage = (short) (from.getAttack() - from.getAttack() * to.getDefense() / 100);
        to.setHp((short) (to.getHp() - damage));
    }
}
